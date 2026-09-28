package com.example.kopkarrsui.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.kopkarrsui.data.local.entity.SHUAllocation
import kotlinx.coroutines.flow.Flow

@Dao
interface SHUAllocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(allocation: SHUAllocation): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(allocations: List<SHUAllocation>): List<Long>

    @Update
    suspend fun update(allocation: SHUAllocation): Int

    @Query("SELECT * FROM shu_allocations WHERE id = :id")
    fun getById(id: Long): Flow<SHUAllocation?>

    @Query("SELECT * FROM shu_allocations WHERE member_id = :memberId AND tahun = :tahun")
    fun getByMemberAndYear(memberId: Long, tahun: Int): Flow<SHUAllocation?>

    @Query("SELECT * FROM shu_allocations WHERE member_id = :memberId ORDER BY tahun DESC")
    fun getByMember(memberId: Long): Flow<List<SHUAllocation>>

    @Query("SELECT * FROM shu_allocations WHERE tahun = :tahun ORDER BY jumlah DESC")
    fun getByYear(tahun: Int): Flow<List<SHUAllocation>>

    @Query("SELECT * FROM shu_allocations WHERE tahun = :tahun AND status = :status")
    fun getByYearAndStatus(tahun: Int, status: SHUAllocation.SHUStatus): Flow<List<SHUAllocation>>

    @Query("SELECT SUM(jumlah) FROM shu_allocations WHERE tahun = :tahun AND status IN ('dibagikan', 'dicairkan')")
    suspend fun getTotalDistributedByYear(tahun: Int): Long

    @Query("UPDATE shu_allocations SET status = :status, tgl_bagi = :tglBagi, updated_at = :updatedAt WHERE tahun = :tahun AND status = 'dihitung'")
    suspend fun updateStatusToDistributed(tahun: Int, status: SHUAllocation.SHUStatus, tglBagi: Long, updatedAt: Long): Int

    @Query("UPDATE shu_allocations SET status = 'dicairkan', tgl_cair = :tglCair, updated_at = :updatedAt WHERE id = :id")
    suspend fun updateToCair(id: Long, tglCair: Long, updatedAt: Long): Int

    @Query("SELECT COUNT(*) FROM shu_allocations WHERE tahun = :tahun")
    suspend fun countByYear(tahun: Int): Int
}