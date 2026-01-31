package com.example.perpusucp.data.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "buku_penulis_cross_ref",
    primaryKeys = ["bukuId", "penulisId"],
    indices = [Index(value = ["penulisId"]), Index(value = ["bukuId"])]
)
data class BukuPenulisCrossRef(
    val bukuId: Int,
    val penulisId: Int
)