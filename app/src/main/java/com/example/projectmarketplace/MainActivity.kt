package com.example.projectmarketplace

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.fragments.AddFragment
import com.example.projectmarketplace.fragments.HomeFragment
import com.example.projectmarketplace.fragments.InboxFragment
import com.example.projectmarketplace.fragments.ProfileFragment
import com.example.projectmarketplace.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.Manifest
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import android.content.Intent
import androidx.fragment.app.commit
import com.example.projectmarketplace.data.Review
import com.example.projectmarketplace.fragments.ReviewFragment

class MainActivity : AppCompatActivity() {

    val items = emptyList<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        requestNotificationPermission()
        saveFcmTokenToFirestore()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        val homeFragment = HomeFragment()
        val searchFragment = SearchFragment()
        val addFragment = AddFragment()
        val inboxFragment = InboxFragment()
        val profileFragment = ProfileFragment()

        // Prvo postavi home fragment kao default
        setCurrentFragment(homeFragment)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> setCurrentFragment(homeFragment)
                R.id.search -> setCurrentFragment(searchFragment)
                R.id.add -> setCurrentFragment(addFragment)
                R.id.inbox -> setCurrentFragment(inboxFragment)
                R.id.profile -> setCurrentFragment(profileFragment)
            }
            true
        }

        // intent ako je aktivnost startana iz notifikacije
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        //  intent ako je aktivnost već bila pokrenuta
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val navigationDestination = intent.getStringExtra("NAVIGATION_DESTINATION")
        Log.d("MainActivity", "Navigation destination: $navigationDestination")

        when (navigationDestination) {
            "inbox" -> {
                val conversationId = intent.getStringExtra("CONVERSATION_ID")
                val messageId = intent.getStringExtra("MESSAGE_ID")
                navigateToInboxFragment(conversationId, messageId)
            }
            "reviews" -> {
                val reviewId = intent.getStringExtra("REVIEW_ID")
                navigateToReviewsFragment(reviewId)
            }
        }
    }

    private fun navigateToInboxFragment(conversationId: String?, messageId: String?) {
        Log.d("MainActivity", "Navigating to InboxFragment with conversation: $conversationId")

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.inbox

        val inboxFragment = InboxFragment().apply {
            arguments = Bundle().apply {
                putString("conversationId", conversationId)
                putString("messageId", messageId)
            }
        }

        setCurrentFragment(inboxFragment)
    }

    private fun navigateToReviewsFragment(reviewId: String?) {
        Log.d("MainActivity", "Navigating to ReviewFragment with ID: $reviewId")

        try {

            val reviewFragment = ReviewFragment().apply {
                arguments = Bundle().apply {

                    putParcelableArrayList("reviews", ArrayList(emptyList<Review>()))
                    reviewId?.let { putString("highlightReviewId", it) }
                }
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, reviewFragment)
                .addToBackStack("reviews_from_notification")
                .commit()

        } catch (e: Exception) {
            Log.e("MainActivity", "Error opening ReviewFragment: ${e.message}")
            e.printStackTrace()


            val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
            bottomNavigationView.selectedItemId = R.id.profile
            setCurrentFragment(ProfileFragment())
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            addToBackStack(null)
            commit()
        }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }
    }

    private fun saveFcmTokenToFirestore() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM", "Dobiven token: $token")

                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    FirebaseFirestore.getInstance().collection("users")
                        .document(userId)
                        .update("fcmToken", token)
                        .addOnSuccessListener {
                            Log.d("FCM", "Token uspješno spremljen u Firestore")
                        }
                        .addOnFailureListener {
                            Log.e("FCM", "Greška pri spremanju tokena", it)
                        }
                } else {
                    Log.e("FCM", "Korisnik nije prijavljen")
                }
            } else {
                Log.e("FCM", "Greška pri dohvaćanju tokena", task.exception)
            }
        }
    }
}