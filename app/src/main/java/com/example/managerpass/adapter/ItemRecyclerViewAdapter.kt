package com.example.managerpass.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.managerpass.R
import com.example.managerpass.models.Item

class ItemRecyclerViewAdapter(
    private val deleteUpdateCallback : (type:String, position: Int, itemL: Item) -> Unit
) :
RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>(){

    private val itemList = arrayListOf<Item>()


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val titleTxt : TextView = itemView.findViewById(R.id.titleTxt)
        val emailTxt : TextView = itemView.findViewById(R.id.emailTxt)
        val passwordTxt : TextView = itemView.findViewById(R.id.passwordTxt)
        val descrTxt : TextView = itemView.findViewById(R.id.descrTxt)

        val deleteImg : ImageView = itemView.findViewById(R.id.deleteImg)
        val editImg : ImageView = itemView.findViewById(R.id.editImg)
    }

    fun addAllItem(newItemList: List<Item>){
        itemList.clear()
        itemList.addAll(newItemList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_vault_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.titleTxt.text = item.title
        holder.emailTxt.text = item.email
        holder.passwordTxt.text = item.password
        holder.descrTxt.text = item.description

        holder.deleteImg.setOnClickListener{
            if(holder.adapterPosition != -1) {
                deleteUpdateCallback("delete",holder.adapterPosition, item)
            }
        }
        holder.editImg.setOnClickListener{
            if(holder.adapterPosition != -1) {
                deleteUpdateCallback("update",holder.adapterPosition, item)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}