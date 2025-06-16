package com.example.projectmarketplace.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope

import com.example.projectmarketplace.LoginActivity
import com.example.projectmarketplace.databinding.FragmentProfileBinding
import com.example.projectmarketplace.viewModels.ProfileViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import androidx.core.content.edit


class ProfileView(private val binding: FragmentProfileBinding,
                  private val context: Context,
                  private var viewModel: ProfileViewModel,
                  private val lifecycleOwner: LifecycleOwner,
                  private val activity: Activity
) {
    private lateinit var googleSignInClient: GoogleSignInClient
    private var firebaseAuth = FirebaseAuth.getInstance()


    fun setupUserInfo(){

        binding.name.text = firebaseAuth.currentUser?.displayName
        binding.email.text = firebaseAuth.currentUser?.email
        lifecycleOwner.lifecycleScope.launch {
            binding.ratingBar.rating = viewModel.getUserRating()!!
        }

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

    fun deleteAccount(){
        binding.deleteAccount.setOnClickListener {
            lifecycleOwner.lifecycleScope.launch {

                if(viewModel.deleteAccount()){
                    clearLocalData()
                    val intent = Intent(context, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    private fun clearLocalData() {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        prefs.edit() { clear() }

    }
}