package com.example.perpusucp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.perpusucp.data.entity.Kategori
import kotlinx.coroutines.flow.Flow

@Dao
interface KategoriDao {
    @Query("SELECT * FROM kategori WHERE isDeleted = 0 ORDER BY nama ASC")
    fun getAllKategori(): Flow<List<Kategori>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertKategori(kategori: Kategori)

    @Update
    suspend fun updateKategori(kategori: Kategori)

    @Query("SELECT * FROM kategori WHERE id = :id")
    suspend fun getKategoriById(id: Int): Kategori?

    @Query("SELECT COUNT(*) FROM kategori WHERE parentId = :parentId AND isDeleted = 0")
    suspend fun countSubKategori(parentId: Int): Int
}