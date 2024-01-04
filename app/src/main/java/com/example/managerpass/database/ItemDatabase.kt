package com.example.managerpass.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.managerpass.dao.ItemDao
import com.example.managerpass.models.Item

@Database(
    entities = [Item::class],
    version = 1,
    exportSchema = false
)
abstract class ItemDatabase : RoomDatabase() {

    abstract val itemDao : ItemDao

    companion object {
        @Volatile
        private var INSTANCE : ItemDatabase? = null
        fun getInstance(context: Context) : ItemDatabase{
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ItemDatabase::class.java,
                    "item_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}
