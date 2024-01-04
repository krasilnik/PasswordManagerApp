package com.example.managerpass.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "itemId")
    val id : String,
    @ColumnInfo(name = "itemTitle")
    val title: String,
    val email: String,
    val password: String,
    val description: String
)
