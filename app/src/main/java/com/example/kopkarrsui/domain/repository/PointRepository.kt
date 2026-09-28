package com.example.kopkarrsui.domain.repository

import com.example.kopkarrsui.data.local.entity.PointLedger
import kotlinx.coroutines.flow.Flow

interface PointRepository {
    suspend fun insert(ledger: PointLedger): Long
    suspend fun insertAll(ledgers: List<PointLedger>): List<Long>
    fun getById(id: Long): Flow<PointLedger?>
    fun getByMemberPaged(memberId: Long, limit: Int, offset: Int): Flow<List<PointLedger>>
    fun getByMemberAndType(memberId: Long, tipe: PointLedger.PointType): Flow<List<PointLedger>>
    fun getTotalPoinByMember(memberId: Long): Flow<Int?>
    fun getTotalEarnedByMember(memberId: Long): Flow<Int?>
    fun getTotalRedeemedByMember(memberId: Long): Flow<Int?>
    fun getTotalExpiredByMember(memberId: Long): Flow<Int?>
    fun getByRefTransaksi(refTransaksiId: Long): Flow<List<PointLedger>>
    fun getExpiredPoints(now: Long): Flow<List<PointLedger>>
}