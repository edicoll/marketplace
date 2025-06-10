package com.example.projectmarketplace

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider



class LoginActivity : AppCompatActivity() {

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var firebaseAuth: FirebaseAuth


    private val oneTapSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        try {
            val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
            handleSignInResult(credential)
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Sign-in failed", e)
            Toast.makeText(this, "Sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        oneTapClient = Identity.getSignInClient(this)

        // zahtjev za prijavu
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id)) //  web client
                    .setFilterByAuthorizedAccounts(false) // dopusti više Google računa
                    .build())
            .setAutoSelectEnabled(true) // automatski izbor ako je samo jedan račun dostupan
            .build()

        var signInButton = findViewById<Button>(R.id.SignIn)
        signInButton.setOnClickListener { view: View? ->
            Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show()
            startOneTapSignIn()
        }
    }

    private fun startOneTapSignIn() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    oneTapSignInLauncher.launch(
                        IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    )
                } catch (e: Exception) {
                    Log.e("GoogleSignIn", "Couldn't start One Tap UI", e)
                    Toast.makeText(this, "Error starting sign-in", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                // one tap nije dostupan, možda korisnik nema Google račune na uređaju
                Log.e("GoogleSignIn", "One Tap UI not available", e)
                Toast.makeText(this, "Please install Google Play services", Toast.LENGTH_SHORT).show()
            }
    }

    private fun handleSignInResult(credential: SignInCredential) {
        val idToken = credential.googleIdToken
        if (idToken != null) {
            // uspješna Google prijava, slijedi prijava u Firebase
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // uspješna prijava, prelazak na mainActivity
                        val user = firebaseAuth.currentUser
                        user?.let {

                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        // neuspjela prijava
                        Toast.makeText(this, "Firebase authentication failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onStart() {
        super.onStart()
        // provjera je li korisnik već prijavljen
        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}