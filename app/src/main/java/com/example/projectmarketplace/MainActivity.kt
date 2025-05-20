package com.example.projectmarketplace

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.projectmarketplace.data.User
import com.example.projectmarketplace.data.Message
import com.example.projectmarketplace.fragments.AddFragment
import com.example.projectmarketplace.fragments.HomeFragment
import com.example.projectmarketplace.fragments.InboxFragment
import com.example.projectmarketplace.fragments.ProfileFragment
import com.example.projectmarketplace.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView



class MainActivity : AppCompatActivity() {

     val user1 = User(
        id = 1,
        name = "Edi",
        email = "edicolliva@gmail.com",
        rating = 3.55F,
        password = "edi"
     )
    val messages = listOf(
        Message(
            id = 1,
            senderId = 2,
            senderName = "Josip",
            recieverId = 1,
            text = "Hej, kako ide s aplikacijom?",
            timestamp = System.currentTimeMillis() - 3600000
        ),
        Message(
            id = 1,
            senderId = 2,
            senderName = "Branko",
            recieverId = 1,
            text = "Jesi li vidio moju poruku?",
            timestamp = System.currentTimeMillis() - 86400000,
            isRead = true
        ),Message(
            id = 1,
            senderId = 2,
            senderName = "Karlo",
            recieverId = 1,
            text = "Radi li ova aplikacija?",
            timestamp = System.currentTimeMillis() - 86400000
        )
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)


        val homeFragment = HomeFragment()
        val searchFragment = SearchFragment()
        val addFragment = AddFragment()
        val inboxFragment = InboxFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList("MESSAGES_KEY", ArrayList(messages))
            }
        }
        val profileFragment = ProfileFragment().apply {
            arguments = Bundle().apply {
                putParcelable("USER_KEY", user1)
            }
        }

        setCurrentFragment(homeFragment)

        bottomNavigationView.setOnItemSelectedListener  {item->
            when (item.itemId) {
                R.id.home -> setCurrentFragment(homeFragment)
                R.id.search -> setCurrentFragment(searchFragment)
                R.id.add -> setCurrentFragment(addFragment)
                R.id.inbox -> setCurrentFragment(inboxFragment)
                R.id.profile -> setCurrentFragment(profileFragment)
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }

}