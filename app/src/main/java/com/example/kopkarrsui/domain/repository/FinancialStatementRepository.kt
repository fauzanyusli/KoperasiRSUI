package com.example.kopkarrsui.domain.repository

import com.example.kopkarrsui.data.local.entity.FinancialStatement
import kotlinx.coroutines.flow.Flow

interface FinancialStatementRepository {
    suspend fun insert(statement: FinancialStatement): Long
    suspend fun insertAll(statements: List<FinancialStatement>): List<Long>
    suspend fun update(statement: FinancialStatement): Int
    fun getById(id: Long): Flow<FinancialStatement?>
    fun getByYear(tahun: Int): Flow<FinancialStatement?>
    fun getAll(): Flow<List<FinancialStatement>>
    fun getByStatus(status: FinancialStatement.FinancialStatus): Flow<List<FinancialStatement>>
    fun getLatest(): Flow<FinancialStatement?>
    suspend fun updateStatus(tahun: Int, status: FinancialStatement.FinancialStatus, tglRAT: Long?, updatedAt: Long): Int
}