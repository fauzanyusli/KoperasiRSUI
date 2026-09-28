package com.example.kopkarrsui.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kopkarrsui.data.local.entity.AuditLog
import kotlinx.coroutines.flow.Flow

@Dao
interface AuditLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: AuditLog): Long

    @Query("SELECT * FROM audit_logs ORDER BY created_at DESC LIMIT :limit")
    fun getRecent(limit: Int = 100): Flow<List<AuditLog>>

    @Query("SELECT * FROM audit_logs WHERE member_id = :memberId ORDER BY created_at DESC")
    fun getByMember(memberId: Long): Flow<List<AuditLog>>

    @Query("SELECT * FROM audit_logs WHERE table_name = :tableName ORDER BY created_at DESC")
    fun getByTable(tableName: String): Flow<List<AuditLog>>

    @Query("SELECT * FROM audit_logs WHERE created_at BETWEEN :startDate AND :endDate ORDER BY created_at DESC")
    fun getByDateRange(startDate: Long, endDate: Long): Flow<List<AuditLog>>

    @Query("SELECT COUNT(*) FROM audit_logs")
    fun getTotalCount(): Flow<Int>

    @Query("DELETE FROM audit_logs WHERE created_at < :beforeDate")
    suspend fun deleteOlderThan(beforeDate: Long)
}
