package com.example.projectmarketplace.views


import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.projectmarketplace.repositories.ItemRepository
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.databinding.FragmentHomeIndividualBinding
import com.example.projectmarketplace.fragments.InboxIndividualFragment
import com.google.firebase.auth.FirebaseAuth
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.example.projectmarketplace.data.Order
import com.example.projectmarketplace.databinding.FragmentEditItemBinding
import com.example.projectmarketplace.fragments.OrderFragment
import com.example.projectmarketplace.viewModels.EditItemViewModel
import com.example.projectmarketplace.viewModels.ItemViewModel


class EditItemView(private val binding: FragmentEditItemBinding,
               private val context: Context, private val item: Item,
               private val itemRepository: ItemRepository,
               private val lifecycleOwner: LifecycleOwner,
               private val activity: FragmentActivity,
               private  var viewModel: EditItemViewModel
) {
    private val auth = FirebaseAuth.getInstance()
    private var boughtItems: List<Order> = emptyList()

    fun bind(){

        with(binding) {
            title.text = item.title
            priceValue.text = context.getString(R.string.price_format, item.price)
            brandInput.text = item.brand
            descriptionInput.text = item.description
            conditionInput.text = item.condition
            colorInput.text = item.color
            image.setOnClickListener {
                showFullScreenImage()
            }
        }

        item.imageUrl.let { imageUrl ->
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .centerCrop()
                .into(binding.image)
        }
    }

    private fun showFullScreenImage(){
        val dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.dialog_fullscreen_image)

        val imageView = dialog.findViewById<ImageView>(R.id.fullscreen_image)
        val closeButton = dialog.findViewById<ImageButton>(R.id.close_button)

        // Učitaj sliku u fullscreen prikaz
        item.imageUrl?.let { imageUrl ->
            Glide.with(context)
                .load(imageUrl)
                .into(imageView)
        }

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    suspend fun deleteItem() {
        if (viewModel.deleteItem(item)) {
            showToast("Item successfully deleted")
            activity.supportFragmentManager.popBackStack()
        } else {
            showToast("Error deleting item")
        }
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


}

