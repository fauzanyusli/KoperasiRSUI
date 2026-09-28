package com.example.kopkarrsui.data.repository

import com.example.kopkarrsui.data.local.dao.TransactionDao
import com.example.kopkarrsui.data.local.entity.Transaction
import com.example.kopkarrsui.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionRepository {

    override suspend fun insert(transaction: Transaction): Long = dao.insert(transaction)

    override suspend fun insertAll(transactions: List<Transaction>): List<Long> = dao.insertAll(transactions)

    override suspend fun update(transaction: Transaction): Int = dao.update(transaction)

    override fun getById(id: Long): Flow<Transaction?> = dao.getById(id)

    override fun getByMemberPaged(memberId: Long, limit: Int, offset: Int): Flow<List<Transaction>> =
        dao.getByMemberPaged(memberId, limit, offset)

    override fun getByMemberAndType(memberId: Long, tipe: Transaction.TransactionType): Flow<List<Transaction>> =
        dao.getByMemberAndType(memberId, tipe)

    override fun getByMemberAndDateRange(memberId: Long, startDate: Long, endDate: Long): Flow<List<Transaction>> =
        dao.getByMemberAndDateRange(memberId, startDate, endDate)

    override fun getByMemberAndStatus(memberId: Long, status: Transaction.TransactionStatus): Flow<List<Transaction>> =
        dao.getByMemberAndStatus(memberId, status)

    override fun getTotalBelanjaByMember(memberId: Long): Flow<Long?> = dao.getTotalBelanjaByMember(memberId)

    override fun getTotalPoinByMember(memberId: Long): Flow<Int?> = dao.getTotalPoinByMember(memberId)

    override fun getByUnitUsahaAndDateRange(unitUsaha: String, startDate: Long, endDate: Long): Flow<List<Transaction>> =
        dao.getByUnitUsahaAndDateRange(unitUsaha, startDate, endDate)

    override suspend fun countSuccessByMember(memberId: Long): Int = dao.countSuccessByMember(memberId)
}