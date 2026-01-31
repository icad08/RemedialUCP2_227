package com.example.perpusucp.data.repository

import com.example.perpusucp.data.entity.Buku
import com.example.perpusucp.data.entity.Kategori
import kotlinx.coroutines.flow.Flow

interface PerpusRepository {
    fun getAllKategori(): Flow<List<Kategori>>
    suspend fun insertKategori(kategori: Kategori)
    suspend fun updateKategori(kategori: Kategori)

    suspend fun deleteKategoriSafe(kategori: Kategori, moveBukuToUncategorized: Boolean)

    suspend fun insertBuku(buku: Buku)
    suspend fun getBukuByKategori(kategoriId: Int): List<Buku>
}