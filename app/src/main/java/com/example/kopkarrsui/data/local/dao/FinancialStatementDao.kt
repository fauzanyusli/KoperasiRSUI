package com.example.kopkarrsui.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.kopkarrsui.data.local.entity.FinancialStatement
import kotlinx.coroutines.flow.Flow

@Dao
interface FinancialStatementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(statement: FinancialStatement): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(statements: List<FinancialStatement>): List<Long>

    @Update
    suspend fun update(statement: FinancialStatement): Int

    @Query("SELECT * FROM financial_statements WHERE id = :id")
    fun getById(id: Long): Flow<FinancialStatement?>

    @Query("SELECT * FROM financial_statements WHERE tahun = :tahun")
    fun getByYear(tahun: Int): Flow<FinancialStatement?>

    @Query("SELECT * FROM financial_statements ORDER BY tahun DESC")
    fun getAll(): Flow<List<FinancialStatement>>

    @Query("SELECT * FROM financial_statements WHERE status = :status ORDER BY tahun DESC")
    fun getByStatus(status: FinancialStatement.FinancialStatus): Flow<List<FinancialStatement>>

    @Query("SELECT * FROM financial_statements WHERE tahun = (SELECT MAX(tahun) FROM financial_statements)")
    fun getLatest(): Flow<FinancialStatement?>

    @Query("UPDATE financial_statements SET status = :status, tgl_rat = :tglRAT, updated_at = :updatedAt WHERE tahun = :tahun")
    suspend fun updateStatus(tahun: Int, status: FinancialStatement.FinancialStatus, tglRAT: Long?, updatedAt: Long): Int
}