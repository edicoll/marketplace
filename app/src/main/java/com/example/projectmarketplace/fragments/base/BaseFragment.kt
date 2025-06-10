package com.example.projectmarketplace.fragments.base

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.projectmarketplace.data.User

abstract class BaseFragment<VBinding : ViewBinding> : Fragment() {

    private var _binding: VBinding? = null
    protected val binding get() = _binding!!

    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VBinding

    protected var currentUser: User? = null
    protected val userKey = "USER_KEY"
    protected val favItemKey = "FAVITEM_KEY"
    protected val itemKey = "ITEM_KEY"
    protected val conversationKey = "CONVERSATION_KEY"
    protected val orderKey = "ORDER_KEY"
    protected val reviewKey = "REVIEW_KEY"
    protected val categoryKey = "CATEGORY_KEY"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun setupBackButton(backButton: View) {
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    // Common method to parse user from arguments
    protected fun parseUserFromArguments(): User? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(userKey, User::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(userKey)
        }
    }

}