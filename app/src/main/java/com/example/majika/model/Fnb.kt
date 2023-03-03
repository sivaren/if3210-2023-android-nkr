package com.example.majika.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fnb")
data class Fnb (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val fnbName: String,
    @ColumnInfo(name = "price")
    val fnbPrice: Int,
    @ColumnInfo(name = "quantity")
    val fnbQuantity: Int
)
