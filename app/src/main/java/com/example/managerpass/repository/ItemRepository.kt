package com.example.managerpass.repository

import android.app.Application
import com.example.managerpass.database.ItemDatabase
import com.example.managerpass.models.Item
import com.example.managerpass.utils.Resource.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import androidx.lifecycle.MutableLiveData
import com.example.managerpass.utils.Resource


class ItemRepository(application: Application) {

    private val itemDao = ItemDatabase.getInstance(application).itemDao


    private val db = ItemDatabase.getInstance(application.applicationContext)

    fun openDatabase(){
        if (!db.isOpen) {
            db.openHelper.writableDatabase
        }
    }

    fun getItemList() = flow {
        emit(Loading())
        try {
            val result = itemDao.getItemList()
            emit(Success(result))
        }catch (e:Exception){
            emit(Error(e.message.toString()))
        }
    }

    fun insertItem(item: Item) = MutableLiveData<Resource<Long>>().apply {
        postValue(Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val result = itemDao.insertItem(item)
                postValue(Success(result))
            }
        }catch (e: Exception){
            postValue(Error(e.message.toString()))
        }
    }


    fun deleteItem(item: Item) = MutableLiveData<Resource<Int>>().apply {
        postValue(Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val result = itemDao.deleteItem(item)
                postValue(Success(result))
            }
        }catch (e: Exception){
            postValue(Error(e.message.toString()))
        }
    }

    fun deleteItemUsingId(itemId: String) = MutableLiveData<Resource<Int>>().apply {
        postValue(Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val result = itemDao.deleteItemUsingId(itemId)
                postValue(Success(result))
            }
        }catch (e: Exception){
            postValue(Error(e.message.toString()))
        }
    }


    fun updateItem(item: Item) = MutableLiveData<Resource<Int>>().apply {
        postValue(Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val result = itemDao.updateItem(item)
                postValue(Success(result))
            }
        }catch (e: Exception){
            postValue(Error(e.message.toString()))
        }
    }

    fun updateItemParticularField(itemId: String, title: String, email: String, password: String, description: String) = MutableLiveData<Resource<Int>>().apply {
        postValue(Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val result = itemDao.updateItemParticularField(itemId, title, email, password, description)
                postValue(Success(result))
            }
        }catch (e: Exception){
            postValue(Error(e.message.toString()))
        }
    }

}