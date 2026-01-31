package com.example.perpusucp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.perpusucp.data.entity.AuditLog
import kotlinx.coroutines.flow.Flow

@Dao
interface AuditDao {
    @Insert
    suspend fun insertLog(log: AuditLog)

    @Query("SELECT * FROM audit_log ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<AuditLog>>
}