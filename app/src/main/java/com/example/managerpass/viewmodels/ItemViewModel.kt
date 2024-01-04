package com.example.managerpass.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Query
import com.example.managerpass.models.Item
import com.example.managerpass.repository.ItemRepository
import com.example.managerpass.utils.Resource
import kotlinx.coroutines.flow.Flow

class ItemViewModel(application: Application) : AndroidViewModel(application) {

    private val itemRepository = ItemRepository(application)

    fun openDatabase() {
        itemRepository.openDatabase()
    }

    fun getItemList() = itemRepository.getItemList()

    fun insertItem(item: Item): MutableLiveData<Resource<Long>> {
        return itemRepository.insertItem(item)
    }

    fun deleteItem(item: Item): MutableLiveData<Resource<Int>> {
        return itemRepository.deleteItem(item)
    }

    fun deleteItemUsingId(itemId: String): MutableLiveData<Resource<Int>> {
        return itemRepository.deleteItemUsingId(itemId)
    }

    fun updateItem(item: Item): MutableLiveData<Resource<Int>> {
        return itemRepository.updateItem(item)
    }

    fun updateItemParticularField(itemId: String,title:String,email: String,password: String,description:String): MutableLiveData<Resource<Int>> {
        return itemRepository.updateItemParticularField(itemId, title, email, password, description)
    }


}