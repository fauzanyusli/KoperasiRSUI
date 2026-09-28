package com.example.kopkarrsui.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.kopkarrsui.data.local.entity.SavingsAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingsAccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: SavingsAccount): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(accounts: List<SavingsAccount>): List<Long>

    @Update
    suspend fun update(account: SavingsAccount): Int

    @Query("SELECT * FROM savings_accounts WHERE id = :id")
    fun getById(id: Long): Flow<SavingsAccount?>

    @Query("SELECT * FROM savings_accounts WHERE member_id = :memberId AND jenis = :jenis")
    fun getByMemberAndType(memberId: Long, jenis: SavingsAccount.SavingsType): Flow<SavingsAccount?>

    @Query("SELECT * FROM savings_accounts WHERE member_id = :memberId ORDER BY jenis ASC")
    fun getByMember(memberId: Long): Flow<List<SavingsAccount>>

    @Query("SELECT * FROM savings_accounts WHERE member_id = :memberId AND status = 'aktif'")
    fun getActiveByMember(memberId: Long): Flow<List<SavingsAccount>>

    @Query("SELECT SUM(saldo) FROM savings_accounts WHERE member_id = :memberId AND status = 'aktif'")
    fun getTotalSaldoByMember(memberId: Long): Flow<Long?>

    @Query("SELECT * FROM savings_accounts WHERE jenis = :jenis AND status = 'aktif'")
    fun getByType(jenis: SavingsAccount.SavingsType): Flow<List<SavingsAccount>>

    @Query("UPDATE savings_accounts SET saldo = saldo + :amount, updated_at = :updatedAt WHERE id = :id")
    suspend fun adjustSaldo(id: Long, amount: Long, updatedAt: Long): Int

    @Query("UPDATE savings_accounts SET status = :status, updated_at = :updatedAt WHERE id = :id")
    suspend fun updateStatus(id: Long, status: SavingsAccount.SavingsStatus, updatedAt: Long): Int
}