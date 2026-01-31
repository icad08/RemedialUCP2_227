package com.example.perpusucp.data.repository

import androidx.room.withTransaction
import com.example.perpusucp.data.PerpusDatabase
import com.example.perpusucp.data.entity.AuditLog
import com.example.perpusucp.data.entity.Buku
import com.example.perpusucp.data.entity.Kategori
import kotlinx.coroutines.flow.Flow

class OfflinePerpusRepository(private val db: PerpusDatabase) : PerpusRepository {

    override fun getAllKategori(): Flow<List<Kategori>> = db.kategoriDao().getAllKategori()

    override suspend fun insertKategori(kategori: Kategori) {
        db.withTransaction {
            db.kategoriDao().insertKategori(kategori)
            db.auditDao().insertLog(
                AuditLog(
                    tableName = "Kategori",
                    action = "INSERT",
                    catatan = "Menambahkan kategori: ${kategori.nama}"
                )
            )
        }
    }

    override suspend fun updateKategori(kategori: Kategori) {
        db.withTransaction {
            val oldData = db.kategoriDao().getKategoriById(kategori.id)
            if (oldData?.parentId != null && kategori.id == oldData.parentId) {
                throw Exception("Cyclic Reference Detected")
            }

            db.kategoriDao().updateKategori(kategori)
            db.auditDao().insertLog(
                AuditLog(
                    tableName = "Kategori",
                    action = "UPDATE",
                    catatan = "Update kategori ID: ${kategori.id}"
                )
            )
        }
    }

    override suspend fun deleteKategoriSafe(kategori: Kategori, moveBukuToUncategorized: Boolean) {
        db.withTransaction {
            val bukuDipinjamCount = db.bukuDao().countBukuDipinjamDiKategori(kategori.id)

            if (bukuDipinjamCount > 0) {
                throw Exception("Gagal menghapus: Ada $bukuDipinjamCount buku sedang dipinjam.")
            }

            val bukuList = db.bukuDao().getBukuByKategori(kategori.id)

            if (moveBukuToUncategorized) {
                bukuList.forEach { buku ->
                    db.bukuDao().updateBuku(buku.copy(kategoriId = null))
                }
            } else {
                bukuList.forEach { buku ->
                    db.bukuDao().updateBuku(buku.copy(isDeleted = true))
                }
            }

            db.kategoriDao().updateKategori(kategori.copy(isDeleted = true))

            db.auditDao().insertLog(
                AuditLog(
                    tableName = "Kategori",
                    action = "SOFT_DELETE",
                    catatan = "Kategori ${kategori.nama} dihapus. Mode Pindah: $moveBukuToUncategorized"
                )
            )
        }
    }

    override suspend fun insertBuku(buku: Buku) {
        db.bukuDao().insertBuku(buku)
    }

    override suspend fun getBukuByKategori(kategoriId: Int): List<Buku> {
        return db.bukuDao().getBukuByKategori(kategoriId)
    }
}