package com.example.perpusucp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.perpusucp.data.dao.AuditDao
import com.example.perpusucp.data.dao.BukuDao
import com.example.perpusucp.data.dao.KategoriDao
import com.example.perpusucp.data.entity.AuditLog
import com.example.perpusucp.data.entity.Buku
import com.example.perpusucp.data.entity.BukuPenulisCrossRef
import com.example.perpusucp.data.entity.EksemplarBuku
import com.example.perpusucp.data.entity.Kategori
import com.example.perpusucp.data.entity.Penulis

@Database(
    entities = [
        Kategori::class,
        Buku::class,
        EksemplarBuku::class,
        Penulis::class,
        BukuPenulisCrossRef::class,
        AuditLog::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PerpusDatabase : RoomDatabase() {
    abstract fun kategoriDao(): KategoriDao
    abstract fun bukuDao(): BukuDao
    abstract fun auditDao(): AuditDao

    companion object {
        @Volatile
        private var Instance: PerpusDatabase? = null

        fun getDatabase(context: Context): PerpusDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    PerpusDatabase::class.java,
                    "perpus_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}