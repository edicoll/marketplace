package com.example.projectmarketplace.views

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import com.example.projectmarketplace.adapters.MessageAdapter
import com.example.projectmarketplace.databinding.FragmentInboxIndividualBinding
import com.example.projectmarketplace.viewModels.InboxIndividualViewModel
import kotlinx.coroutines.launch

class InboxIndividualView(private val binding: FragmentInboxIndividualBinding,
                          private val context: Context,
                          private val activity: FragmentActivity,
                          private  var viewModel: InboxIndividualViewModel,
                          private val conversationId: String,
                          private val participant1Id: String,
                          private val lifecycleOwner: LifecycleOwner){

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessageAdapter
    private lateinit var messageText: String
    private var messageSendingError = "Error sending message"

    fun setupRecyclerView() {

        recyclerView = binding.messagesRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)


        adapter = MessageAdapter(viewModel.messages, participant1Id)
        recyclerView.adapter = adapter
    }

    suspend fun fetchMessages(){
        val messages = viewModel.getMessages(conversationId).sortedBy { it.timestamp }

        adapter = MessageAdapter(messages, participant1Id)
        recyclerView.adapter = adapter
        recyclerView.scrollToPosition(messages.size - 1)
    }

    fun setupMessageSending(){
        binding.sendButton.setOnClickListener {
                messageText = binding.messageInput.text.toString().trim()

                if (messageText.isNotEmpty()){
                    lifecycleOwner.lifecycleScope.launch {
                        val success = viewModel.sendMessage(
                            conversationId,
                            participant1Id,
                            messageText)
                        if (success) {
                            binding.messageInput.text.clear()

                        } else {
                            showToast(messageSendingError)
                        }
                    }
                }
        }
    }

    fun setupRealtimeUpdates() {
        //počinje osuškivati promjene u konverzaciji i kada se dogodi promjena, messages dobije ažuriranu listu
        viewModel.startListening(conversationId) { messages ->
            val sortedMessages = messages.sortedBy { it.timestamp } //sortiranje po datumu
            adapter.updateMessages(sortedMessages)                  //ažuriranje adaptera -> recycleviewa
            recyclerView.scrollToPosition(sortedMessages.size - 1)  //automatski skrola na dno, odnosno zadnju poruku
        }
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}