package com.example.kopkarrsui.domain.repository

import com.example.kopkarrsui.data.local.entity.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun insert(transaction: Transaction): Long
    suspend fun insertAll(transactions: List<Transaction>): List<Long>
    suspend fun update(transaction: Transaction): Int
    fun getById(id: Long): Flow<Transaction?>
    fun getByMemberPaged(memberId: Long, limit: Int, offset: Int): Flow<List<Transaction>>
    fun getByMemberAndType(memberId: Long, tipe: Transaction.TransactionType): Flow<List<Transaction>>
    fun getByMemberAndDateRange(memberId: Long, startDate: Long, endDate: Long): Flow<List<Transaction>>
    fun getByMemberAndStatus(memberId: Long, status: Transaction.TransactionStatus): Flow<List<Transaction>>
    fun getTotalBelanjaByMember(memberId: Long): Flow<Long?>
    fun getTotalPoinByMember(memberId: Long): Flow<Int?>
    fun getByUnitUsahaAndDateRange(unitUsaha: String, startDate: Long, endDate: Long): Flow<List<Transaction>>
    suspend fun countSuccessByMember(memberId: Long): Int
}