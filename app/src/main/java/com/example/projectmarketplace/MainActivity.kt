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
        rating = 3.55F
     )
    val user2 = User(
        id = "",
        name = "Edi",
        email = "branko@gmail.com",
        rating = 3.55F
    )
    val conversations = listOf(
        Conversation(
            id = 1,
            participant1Id = 1,
            participant2Id = 2,
            participant1Name = "Edi",
            participant2Name = "Josip",
            lastMessage = "Radi li ova aplikacija?",
            timestamp = System.currentTimeMillis() - 86400000
        ),
        Conversation(
            id = 2,
            participant1Id = 1,
            participant2Id = 3,
            participant1Name = "Edi",
            participant2Name = "Branko",
            lastMessage = "Jesi li vidio moju poruku?",
            timestamp = System.currentTimeMillis() - 86400000
        )
    )
    val items = listOf(
        Item(
            title = "Auto",
            description = "Brand new car fiat panda.",
            price = 5000.00,
            brand = "Fiat",
            condition = "new",
            sellerId = "2",
            color = "white",
            createdAt = Date(System.currentTimeMillis() - 486400000),
            category = "Vehicles"
        ),
        Item(
            title = "Monitor",
            description = "Used wide monitor for gaming.",
            price = 40.00,
            brand = "Dell",
            condition = "used",
            sellerId = "2",
            color = "black",
            createdAt = Date(System.currentTimeMillis() - 96400000),
            category = "Electronics"
        ),
        Item(
            title = "Laptop",
            description = "Used office laptop.",
            price = 300.00,
            brand = "Acer",
            condition = "used",
            sellerId = "2",
            color = "silver",
            createdAt = Date(System.currentTimeMillis() - 66400000),
            category = "Electronics"
        ),
        Item(
            title = "Sunglasses",
            description = "New ray ban stylish sunglasses",
            price = 100.00,
            brand = "Ray Ban",
            condition = "new",
            sellerId = "2",
            color = "black",
            createdAt = Date(System.currentTimeMillis() - 6400000),
            category = "Accessories"
        ),
        Item(
            title = "Auto",
            description = "Brand new car audi a3.",
            price = 10000.00,
            brand = "Audi",
            condition = "new",
            sellerId = "2",
            color = "black",
            createdAt = Date(System.currentTimeMillis() - 486400000),
            category = "Vehicles"
        ),
        Item(
            title = "Mouse",
            description = "Used mouse for gaming.",
            price = 20.00,
            brand = "Razer",
            condition = "used",
            sellerId = "2",
            color = "black",
            createdAt = Date(System.currentTimeMillis() - 96400000),
            category = "Electronics"
        ),
        Item(
            title = "Laptop Asus",
            description = "Used office laptop.",
            price = 300.00,
            brand = "Asus",
            condition = "used",
            sellerId = "2",
            color = "silver",
            createdAt = Date(System.currentTimeMillis() - 66400000),
            category = "Electronics"
        ),
        Item(
            title = "Sunglasses",
            description = "New Police stylish sunglasses",
            price = 100.00,
            brand = "Police",
            condition = "new",
            sellerId = "2",
            color = "black",
            createdAt = Date(System.currentTimeMillis() - 6400000),
            category = "Accessories"
        )
    )
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