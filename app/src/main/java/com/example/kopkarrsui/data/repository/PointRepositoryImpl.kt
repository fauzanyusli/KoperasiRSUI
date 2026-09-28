package com.example.kopkarrsui.data.repository

import com.example.kopkarrsui.data.local.dao.PointLedgerDao
import com.example.kopkarrsui.data.local.entity.PointLedger
import com.example.kopkarrsui.domain.repository.PointRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PointRepositoryImpl @Inject constructor(
    private val dao: PointLedgerDao
) : PointRepository {

    override suspend fun insert(ledger: PointLedger): Long = dao.insert(ledger)

    override suspend fun insertAll(ledgers: List<PointLedger>): List<Long> = dao.insertAll(ledgers)

    override fun getById(id: Long): Flow<PointLedger?> = dao.getById(id)

    override fun getByMemberPaged(memberId: Long, limit: Int, offset: Int): Flow<List<PointLedger>> =
        dao.getByMemberPaged(memberId, limit, offset)

    override fun getByMemberAndType(memberId: Long, tipe: PointLedger.PointType): Flow<List<PointLedger>> =
        dao.getByMemberAndType(memberId, tipe)

    override fun getTotalPoinByMember(memberId: Long): Flow<Int?> = dao.getTotalPoinByMember(memberId)

    override fun getTotalEarnedByMember(memberId: Long): Flow<Int?> = dao.getTotalEarnedByMember(memberId)

    override fun getTotalRedeemedByMember(memberId: Long): Flow<Int?> = dao.getTotalRedeemedByMember(memberId)

    override fun getTotalExpiredByMember(memberId: Long): Flow<Int?> = dao.getTotalExpiredByMember(memberId)

    override fun getByRefTransaksi(refTransaksiId: Long): Flow<List<PointLedger>> = dao.getByRefTransaksi(refTransaksiId)

    override fun getExpiredPoints(now: Long): Flow<List<PointLedger>> = dao.getExpiredPoints(now)
}