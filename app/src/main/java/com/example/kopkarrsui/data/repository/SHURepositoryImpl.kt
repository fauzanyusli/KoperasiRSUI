package com.example.kopkarrsui.data.repository

import com.example.kopkarrsui.data.local.dao.SHUAllocationDao
import com.example.kopkarrsui.data.local.entity.SHUAllocation
import com.example.kopkarrsui.domain.repository.SHURepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SHURepositoryImpl @Inject constructor(
    private val dao: SHUAllocationDao
) : SHURepository {

    override suspend fun insert(allocation: SHUAllocation): Long = dao.insert(allocation)

    override suspend fun insertAll(allocations: List<SHUAllocation>): List<Long> = dao.insertAll(allocations)

    override suspend fun update(allocation: SHUAllocation): Int = dao.update(allocation)

    override fun getById(id: Long): Flow<SHUAllocation?> = dao.getById(id)

    override fun getByMemberAndYear(memberId: Long, tahun: Int): Flow<SHUAllocation?> = dao.getByMemberAndYear(memberId, tahun)

    override fun getByMember(memberId: Long): Flow<List<SHUAllocation>> = dao.getByMember(memberId)

    override fun getByYear(tahun: Int): Flow<List<SHUAllocation>> = dao.getByYear(tahun)

    override fun getByYearAndStatus(tahun: Int, status: SHUAllocation.SHUStatus): Flow<List<SHUAllocation>> = dao.getByYearAndStatus(tahun, status)

    override suspend fun getTotalDistributedByYear(tahun: Int): Long = dao.getTotalDistributedByYear(tahun)

    override suspend fun updateStatusToDistributed(tahun: Int, status: SHUAllocation.SHUStatus, tglBagi: Long, updatedAt: Long): Int =
        dao.updateStatusToDistributed(tahun, status, tglBagi, updatedAt)

    override suspend fun updateToCair(id: Long, tglCair: Long, updatedAt: Long): Int = dao.updateToCair(id, tglCair, updatedAt)

    override suspend fun countByYear(tahun: Int): Int = dao.countByYear(tahun)
}