package com.example.projectmarketplace

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.projectmarketplace.data.Conversation
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.data.User
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
            id = 1,
            sellerId = 2,
            sellerName = "Josip",
            sellerRating = 2.5F,
            title = "Auto",
            description = "Brand new car fiat panda.",
            brand = "Fiat",
            condition = "new",
            color = "white",
            price = 5000.00f,
            timestamp = System.currentTimeMillis() - 486400000
        ),Item(
            id = 2,
            sellerId = 2,
            sellerName = "Josip",
            sellerRating = 2.5F,
            title = "Monitor",
            description = "Used wide monitor for gaming.",
            brand = "Dell",
            condition = "used",
            color = "black",
            price = 40.00f,
            timestamp = System.currentTimeMillis() - 96400000
        ),Item(
            id = 3,
            sellerId = 2,
            sellerName = "Josip",
            sellerRating = 2.5F,
            title = "Laptop",
            description = "Used office laptop.",
            brand = "Acer",
            condition = "used",
            color = "silver",
            price = 300.00f,
            timestamp = System.currentTimeMillis() - 66400000
        ),Item(
            id = 4,
            sellerId = 2,
            sellerName = "Josip",
            sellerRating = 2.5F,
            title = "Sunglasses",
            description = "New ray ban stylish sunglasses",
            brand = "Ray Ban",
            condition = "new",
            color = "black",
            price = 100.00f,
            timestamp = System.currentTimeMillis() - 6400000
        ),Item(
            id = 5,
            sellerId = 2,
            sellerName = "Josip",
            sellerRating = 2.5F,
            title = "Auto",
            description = "Brand new car fiat panda.",
            brand = "Fiat",
            condition = "new",
            color = "white",
            price = 5000.00f,
            timestamp = System.currentTimeMillis() - 486400000
        ),Item(
            id = 6,
            sellerId = 2,
            sellerName = "Josip",
            sellerRating = 2.5F,
            title = "Monitor",
            description = "Used wide monitor for gaming.",
            brand = "Dell",
            condition = "used",
            color = "black",
            price = 40.00f,
            timestamp = System.currentTimeMillis() - 96400000
        ),Item(
            id = 7,
            sellerId = 2,
            sellerName = "Josip",
            sellerRating = 2.5F,
            title = "Laptop",
            description = "Used office laptop.",
            brand = "Acer",
            condition = "used",
            color = "silver",
            price = 300.00f,
            timestamp = System.currentTimeMillis() - 66400000
        ),Item(
            id = 8,
            sellerId = 2,
            sellerName = "Josip",
            sellerRating = 2.5F,
            title = "Sunglasses",
            description = "New ray ban stylish sunglasses",
            brand = "Ray Ban",
            condition = "new",
            color = "black",
            price = 100.00f,
            timestamp = System.currentTimeMillis() - 6400000
        )
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)


        val homeFragment = HomeFragment().apply {
            arguments = Bundle().apply{
                putParcelableArrayList("ITEM_KEY", ArrayList(items))
            }
        }
        val searchFragment = SearchFragment().apply {
            arguments = Bundle().apply{
                putParcelableArrayList("ITEM_KEY", ArrayList(items))
            }
        }
        val addFragment = AddFragment()
        val inboxFragment = InboxFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList("CONVERSATION_KEY", ArrayList(conversations))
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