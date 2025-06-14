package com.example.projectmarketplace

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.projectmarketplace.data.Conversation
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.data.Review
import com.example.projectmarketplace.data.User
import com.example.projectmarketplace.fragments.AddFragment
import com.example.projectmarketplace.fragments.HomeFragment
import com.example.projectmarketplace.fragments.InboxFragment
import com.example.projectmarketplace.fragments.ProfileFragment
import com.example.projectmarketplace.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Date


class MainActivity : AppCompatActivity() {



    private val userKey = "USER_KEY"
    private val reviewKey = "REVIEW_KEY"
    private val itemKey = "ITEM_KEY"
    private val conversationKey = "CONVERSATION_KEY"

     val user1 = User(
        id = "u",
        name = "Edi",
        email = "edicolliva@gmail.com",
        rating = 3.55f,
         ratingCount = 0
     )

    val conversations = emptyList<Conversation>()

    val items = emptyList<Item>()
    val reviews = emptyList<Review>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)


        val homeFragment = HomeFragment().apply {
            arguments = Bundle().apply{
                putParcelableArrayList(itemKey, ArrayList(items))
            }
        }
        val searchFragment = SearchFragment().apply {
            arguments = Bundle().apply{
                putParcelableArrayList(itemKey, ArrayList(items))
            }
        }
        val addFragment = AddFragment()
        val inboxFragment = InboxFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(conversationKey, ArrayList(conversations))
            }
        }
        val profileFragment = ProfileFragment().apply {
            arguments = Bundle().apply {
                putParcelable(userKey, user1)
                putParcelableArrayList(reviewKey, ArrayList(reviews))
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