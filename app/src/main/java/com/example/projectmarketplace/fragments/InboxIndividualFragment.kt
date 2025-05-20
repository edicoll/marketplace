package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.Message
import com.example.projectmarketplace.data.User


class InboxIndividualFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inbox_individual, container, false)

    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageButton>(R.id.back).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val message = arguments?.getParcelable<Message>("MESSAGE", Message::class.java)
        message?.let {
            view.findViewById<TextView>(R.id.name).text = it.senderName
            view.findViewById<TextView>(R.id.text).text = "838383"
            view.findViewById<TextView>(R.id.date).text = it.text
        }
    }

    companion object {
        fun newInstance(message: Message): InboxIndividualFragment {
            return InboxIndividualFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("MESSAGE", message)
                }
            }
        }
    }
}