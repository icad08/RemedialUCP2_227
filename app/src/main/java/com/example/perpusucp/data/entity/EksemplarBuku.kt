package com.example.perpusucp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "eksemplar_buku",
    foreignKeys = [
        ForeignKey(
            entity = Buku::class,
            parentColumns = ["id"],
            childColumns = ["bukuId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["bukuId"])]
)
data class EksemplarBuku(
    @PrimaryKey
    val kodeEksemplar: String,
    val bukuId: Int,
    val status: String,
    val kondisi: String = "Baik"
)