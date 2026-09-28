package com.example.kopkarrsui.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.kopkarrsui.data.local.entity.Admin
import kotlinx.coroutines.flow.Flow

@Dao
interface AdminDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(admin: Admin): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(admins: List<Admin>): List<Long>

    @Update
    suspend fun update(admin: Admin): Int

    @Query("SELECT * FROM admins WHERE id = :id")
    fun getById(id: Long): Flow<Admin?>

    @Query("SELECT * FROM admins WHERE member_id = :memberId")
    fun getByMemberId(memberId: Long): Flow<Admin?>

    @Query("SELECT * FROM admins WHERE role = :role AND status = 'aktif'")
    fun getByRole(role: Admin.AdminRole): Flow<List<Admin>>

    @Query("SELECT * FROM admins WHERE status = 'aktif'")
    fun getAllActive(): Flow<List<Admin>>

    @Query("SELECT * FROM admins ORDER BY role ASC")
    fun getAll(): Flow<List<Admin>>

    @Query("UPDATE admins SET status = :status, updated_at = :updatedAt WHERE id = :id")
    suspend fun updateStatus(id: Long, status: Admin.AdminStatus, updatedAt: Long): Int

    @Query("SELECT * FROM admins WHERE member_id = :memberId AND status = 'aktif'")
    fun getActiveByMemberId(memberId: Long): Flow<Admin?>
}