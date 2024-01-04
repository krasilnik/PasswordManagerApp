package com.example.managerpass.adapter

import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.managerpass.databinding.ViewVaultLayoutBinding
import com.example.managerpass.models.Item
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class ItemRVVBListAdapter(
    private val deleteUpdateCallback : (type:String, position: Int, item: Item) -> Unit
) :
    ListAdapter<Item, ItemRVVBListAdapter.ViewHolder>(DiffCallback()) {



    class ViewHolder(val viewVaultLayoutBinding: ViewVaultLayoutBinding)
        : RecyclerView.ViewHolder(viewVaultLayoutBinding.root)

    var key : String="mysecretkey12345"
    var secretKeySpec = SecretKeySpec(key.toByteArray(),"AES")

    private fun decrypt(string : String) : String{
        var cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec)
        var decryptedBytes = cipher.doFinal(Base64.decode(string, Base64.DEFAULT)) // decoding the entered string
        return String(decryptedBytes,Charsets.UTF_8) // returning the decrypted string
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ViewVaultLayoutBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val password = item.password
        holder.viewVaultLayoutBinding.titleTxt.text = item.title
        holder.viewVaultLayoutBinding.emailTxt.text = item.email
        holder.viewVaultLayoutBinding.passwordTxt.text = password
        holder.viewVaultLayoutBinding.descrTxt.text = item.description
        holder.viewVaultLayoutBinding.deleteImg.setOnClickListener{
            if(holder.adapterPosition != -1) {
                deleteUpdateCallback("delete",holder.adapterPosition, item)
            }
        }
        holder.viewVaultLayoutBinding.editImg.setOnClickListener{
            if(holder.adapterPosition != -1) {
                deleteUpdateCallback("update",holder.adapterPosition, item)
            }
        }
    }




    class DiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

    }

}