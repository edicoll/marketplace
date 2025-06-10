package com.example.projectmarketplace.repositories

import com.example.projectmarketplace.data.Item
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ItemRepository {
    private val database: FirebaseFirestore = Firebase.firestore
    private val itemsCollection = database.collection("items")

    suspend fun addItem(item: Item): String {  //suspend služi da ta funkcija može biti pauzirana i nastaviti se kasnije, bez ometanja  glavnog threada
        val ref = itemsCollection.add(item).await() //await kao pretvara firebase task u kotlin rezultat
        return ref.id
    }

    suspend fun getItems(): List<Pair<String, Item>> {
        val querySnapshot = itemsCollection.get().await()
        return querySnapshot.documents.mapNotNull { doc ->
            val item = doc.toObject(Item::class.java)
            if (item != null) Pair(doc.id, item) else null
        }
    }
    /*  za sad ami ne koristi
    suspend fun getItemsByUser(userId: String): List<Item> {
        val querySnapshot = itemsCollection
            .whereEqualTo("userId", userId)
            .get()
            .await()
        return querySnapshot.documents.map { doc ->
            doc.toObject(Item::class.java)?.copy(id = doc.id) ?: Item()
        }
    }*/
    /* za sada ništa
    suspend fun updateItem(item: Item) {
        itemsCollection.document(item.id).set(item).await()
    }*/

    suspend fun deleteItem(itemId: String) {
        itemsCollection.document(itemId).delete().await()
    }
}