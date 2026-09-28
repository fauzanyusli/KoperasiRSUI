package com.example.kopkarrsui.data.repository

import com.example.kopkarrsui.data.local.dao.SavingsAccountDao
import com.example.kopkarrsui.data.local.entity.SavingsAccount
import com.example.kopkarrsui.domain.repository.SavingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavingsRepositoryImpl @Inject constructor(
    private val dao: SavingsAccountDao
) : SavingsRepository {

    override suspend fun insert(account: SavingsAccount): Long = dao.insert(account)

    override suspend fun update(account: SavingsAccount): Int = dao.update(account)

    override fun getById(id: Long): Flow<SavingsAccount?> = dao.getById(id)

    override fun getByMemberAndType(memberId: Long, jenis: SavingsAccount.SavingsType): Flow<SavingsAccount?> =
        dao.getByMemberAndType(memberId, jenis)

    override fun getByMember(memberId: Long): Flow<List<SavingsAccount>> = dao.getByMember(memberId)

    override fun getActiveByMember(memberId: Long): Flow<List<SavingsAccount>> = dao.getActiveByMember(memberId)

    override fun getByType(jenis: SavingsAccount.SavingsType): Flow<List<SavingsAccount>> = dao.getByType(jenis)

    override fun getTotalSaldoByMember(memberId: Long): Flow<Long?> = dao.getTotalSaldoByMember(memberId)

    override suspend fun adjustSaldo(id: Long, amount: Long, updatedAt: Long): Int = dao.adjustSaldo(id, amount, updatedAt)

    override suspend fun updateStatus(id: Long, status: SavingsAccount.SavingsStatus, updatedAt: Long): Int =
        dao.updateStatus(id, status, updatedAt)
}