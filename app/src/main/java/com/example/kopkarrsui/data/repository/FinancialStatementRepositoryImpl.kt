package com.example.kopkarrsui.data.repository

import com.example.kopkarrsui.data.local.dao.FinancialStatementDao
import com.example.kopkarrsui.data.local.entity.FinancialStatement
import com.example.kopkarrsui.domain.repository.FinancialStatementRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FinancialStatementRepositoryImpl @Inject constructor(
    private val dao: FinancialStatementDao
) : FinancialStatementRepository {

    override suspend fun insert(statement: FinancialStatement): Long = dao.insert(statement)

    override suspend fun insertAll(statements: List<FinancialStatement>): List<Long> = dao.insertAll(statements)

    override suspend fun update(statement: FinancialStatement): Int = dao.update(statement)

    override fun getById(id: Long): Flow<FinancialStatement?> = dao.getById(id)

    override fun getByYear(tahun: Int): Flow<FinancialStatement?> = dao.getByYear(tahun)

    override fun getAll(): Flow<List<FinancialStatement>> = dao.getAll()

    override fun getByStatus(status: FinancialStatement.FinancialStatus): Flow<List<FinancialStatement>> = dao.getByStatus(status)

    override fun getLatest(): Flow<FinancialStatement?> = dao.getLatest()

    override suspend fun updateStatus(tahun: Int, status: FinancialStatement.FinancialStatus, tglRAT: Long?, updatedAt: Long): Int =
        dao.updateStatus(tahun, status, tglRAT, updatedAt)
}