package com.example.projectmarketplace.repositories

import com.example.projectmarketplace.data.Conversation
import com.example.projectmarketplace.data.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class MessageRepository {

    private val database: FirebaseFirestore = Firebase.firestore
    private val messageCollection = database.collection("messages")
    private val conversationCollection = database.collection("conversations")
    private val userCollection = database.collection("users")
    private val auth = FirebaseAuth.getInstance()

    suspend fun getMessages(conversationId: String): List<Message>{

        return try {

            val querySnapshot = messageCollection
                .whereEqualTo("conversationId", conversationId)
                .get()
                .await()

            clearUnreadCount(conversationId)

            querySnapshot.documents.mapNotNull { doc ->
                doc.toObject(Message::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }


    suspend fun sendMessage(
        conversationId: String,
        senderId: String,
        text: String
    ): Boolean {
        return try {

            val messageId = messageCollection.document().id

            val message = Message(
                id = messageId,
                conversationId = conversationId,
                senderId = senderId,
                senderName = getUserName(senderId).toString(),
                text = text,
                timestamp = System.currentTimeMillis(),
            )

            setLastMessage(message)
            setUnreadCount(message)

            messageCollection.document(messageId)
                .set(message)
                .await()

            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUserName(userId: String): String? {
        return try {
            val document = userCollection.document(userId).get().await()
            document.getString("name")
        } catch (e: Exception) {
            null
        }
    }

    fun listenForMessages(
        conversationId: String,
        onMessagesUpdated: (List<Message>) -> Unit  //kada se nešto promijeni poziva se ova funkcija s novim podatcima
    ) {
        messageCollection
            .whereEqualTo("conversationId", conversationId)
            .addSnapshotListener { snapshot, error ->      //addSnapshotListener automatski reagira na promjene u bazi
                if (error != null) {
                    return@addSnapshotListener
                }

                val messages = snapshot?.documents?.mapNotNull {
                    it.toObject(Message::class.java)
                } ?: emptyList()

                onMessagesUpdated(messages)
            }
    }

    fun setLastMessage(lastMessage: Message){

        val updates = mapOf(
            "lastMessage" to lastMessage.text,
            "timestamp" to lastMessage.timestamp
        )

        conversationCollection.document(lastMessage.conversationId)
            .update(updates)


    }

    fun setUnreadCount(lastMessage: Message){

        val conversation = conversationCollection.document(lastMessage.conversationId)

        conversation.get()
            .addOnSuccessListener { document ->
                val conversationObject = document.toObject(Conversation::class.java)

                conversationObject?.let {
                    val updates = hashMapOf<String, Any>()

                    if (lastMessage.senderId == it.participant1Id) {

                        updates["participant2unreadCount"] = FieldValue.increment(1)
                    } else if (lastMessage.senderId == it.participant2Id) {

                        updates["participant1unreadCount"] = FieldValue.increment(1)
                    }
                    conversation
                        .update(updates)

                }
            }

    }

    fun clearUnreadCount(conversationId: String) {

        val conversation = conversationCollection.document(conversationId)
        val currentUserId = auth.currentUser?.uid

        conversation.get()
            .addOnSuccessListener { document ->
                val conversationObject = document.toObject(Conversation::class.java)

                conversationObject?.let {
                    val updates = hashMapOf<String, Any>()

                    if (currentUserId == it.participant1Id) {

                        updates["participant1unreadCount"] = 0

                    } else if (currentUserId == it.participant2Id) {

                        updates["participant2unreadCount"] = 0
                    }
                    conversation
                        .update(updates)

                }
            }
    }

}
