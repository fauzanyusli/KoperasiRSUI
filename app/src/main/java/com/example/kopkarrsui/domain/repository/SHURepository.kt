package com.example.kopkarrsui.domain.repository

import com.example.kopkarrsui.data.local.entity.SHUAllocation
import kotlinx.coroutines.flow.Flow

interface SHURepository {
    suspend fun insert(allocation: SHUAllocation): Long
    suspend fun insertAll(allocations: List<SHUAllocation>): List<Long>
    suspend fun update(allocation: SHUAllocation): Int
    fun getById(id: Long): Flow<SHUAllocation?>
    fun getByMemberAndYear(memberId: Long, tahun: Int): Flow<SHUAllocation?>
    fun getByMember(memberId: Long): Flow<List<SHUAllocation>>
    fun getByYear(tahun: Int): Flow<List<SHUAllocation>>
    fun getByYearAndStatus(tahun: Int, status: SHUAllocation.SHUStatus): Flow<List<SHUAllocation>>
    suspend fun getTotalDistributedByYear(tahun: Int): Long
    suspend fun updateStatusToDistributed(tahun: Int, status: SHUAllocation.SHUStatus, tglBagi: Long, updatedAt: Long): Int
    suspend fun updateToCair(id: Long, tglCair: Long, updatedAt: Long): Int
    suspend fun countByYear(tahun: Int): Int
}