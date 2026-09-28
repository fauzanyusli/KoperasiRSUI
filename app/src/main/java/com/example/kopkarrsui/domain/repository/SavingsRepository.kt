package com.example.kopkarrsui.domain.repository

import com.example.kopkarrsui.data.local.entity.SavingsAccount
import kotlinx.coroutines.flow.Flow

interface SavingsRepository {
    suspend fun insert(account: SavingsAccount): Long
    suspend fun update(account: SavingsAccount): Int
    fun getById(id: Long): Flow<SavingsAccount?>
    fun getByMemberAndType(memberId: Long, jenis: SavingsAccount.SavingsType): Flow<SavingsAccount?>
    fun getByMember(memberId: Long): Flow<List<SavingsAccount>>
    fun getActiveByMember(memberId: Long): Flow<List<SavingsAccount>>
    fun getByType(jenis: SavingsAccount.SavingsType): Flow<List<SavingsAccount>>
    fun getTotalSaldoByMember(memberId: Long): Flow<Long?>
    suspend fun adjustSaldo(id: Long, amount: Long, updatedAt: Long): Int
    suspend fun updateStatus(id: Long, status: SavingsAccount.SavingsStatus, updatedAt: Long): Int
}