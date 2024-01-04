package com.example.managerpass.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.managerpass.models.Item
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM Item ORDER BY itemId DESC")
    fun getItemList() : Flow<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Item): Long

    // first way
    @Delete
    suspend fun deleteItem(item: Item) : Int


    //Second way
    @Query("DELETE FROM Item WHERE itemId == :itemId")
    suspend fun deleteItemUsingId(itemId: String) : Int

    @Update
    suspend fun updateItem(item: Item): Int

    @Query("UPDATE Item SET itemTitle= :title, email= :email, password= :password, description= :description WHERE itemId = :itemId")
    suspend fun updateItemParticularField(itemId: String, title:String, email:String, password:String, description:String): Int

}