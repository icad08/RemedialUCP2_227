package com.example.perpusucp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.perpusucp.data.entity.Buku
import com.example.perpusucp.data.entity.BukuPenulisCrossRef
import com.example.perpusucp.data.entity.EksemplarBuku

@Dao
interface BukuDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBuku(buku: Buku): Long

    @Update
    suspend fun updateBuku(buku: Buku)

    @Query("SELECT * FROM buku WHERE kategoriId = :kategoriId AND isDeleted = 0")
    suspend fun getBukuByKategori(kategoriId: Int): List<Buku>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEksemplar(eksemplar: EksemplarBuku)

    @Query("""
        SELECT COUNT(*) FROM eksemplar_buku 
        INNER JOIN buku ON eksemplar_buku.bukuId = buku.id 
        WHERE buku.kategoriId = :kategoriId 
        AND eksemplar_buku.status = 'Dipinjam' 
        AND buku.isDeleted = 0
    """)
    suspend fun countBukuDipinjamDiKategori(kategoriId: Int): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBukuPenulisCrossRef(ref: BukuPenulisCrossRef)
}