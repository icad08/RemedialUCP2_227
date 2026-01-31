package com.example.perpusucp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "penulis")
data class Penulis(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String
)