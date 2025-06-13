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
        rating = 3.55f
     )
    val user2 = User(
        id = "",
        name = "Edi",
        email = "branko@gmail.com",
        rating = 3.55f
    )
    val conversations = emptyList<Conversation>()

    val items = emptyList<Item>()
    val reviews = listOf(
        Review(
            id = 1,
            userIdTo = 1,
            userIdFrom = 201,
            rating = 5,
            comment = "Excellent service! The product was exactly as described and arrived quickly."
        ),
        Review(
            id = 2,
            userIdTo = 1,
            userIdFrom = 202,
            rating = 4,
            comment = "Good quality product, but shipping took longer than expected."
        ),
        Review(
            id = 3,
            userIdTo = 1,
            userIdFrom = 203,
            rating = 3,
            comment = "Average experience. The item works but shows signs of wear."
        ),
        Review(
            id = 4,
            userIdTo = 1,
            userIdFrom = 204,
            rating = 1,
            comment = "Very disappointed. Product didn't match the description at all."
        )
    )



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