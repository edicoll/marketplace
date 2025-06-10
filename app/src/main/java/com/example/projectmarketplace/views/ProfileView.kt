package com.example.projectmarketplace.views

import android.content.Context
import android.content.Intent

import com.example.projectmarketplace.LoginActivity
import com.example.projectmarketplace.data.User
import com.example.projectmarketplace.databinding.FragmentProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth


class ProfileView(private val binding: FragmentProfileBinding,
                  private val context: Context, private val currentUser: User?
) {
    private lateinit var googleSignInClient: GoogleSignInClient
    private var firebaseAuth = FirebaseAuth.getInstance()


    fun setupUserInfo(){

        binding.name.text = firebaseAuth.currentUser?.displayName
        binding.email.text = firebaseAuth.currentUser?.email
        //binding.ratingBar.rating = currentUser?.rating!! //zamijeniti
    }


    fun setupLogout() {
        // konfiguracija prijavljenog korisnika
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)

        // gumb za odjavu
        binding.logOut.setOnClickListener {
            // odjavi Google
            googleSignInClient.signOut().addOnCompleteListener {
                // odjavi Firebase
                FirebaseAuth.getInstance().signOut()

                // povratak na loginActivity
                val intent = Intent(context, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
            }
        }
    }
}