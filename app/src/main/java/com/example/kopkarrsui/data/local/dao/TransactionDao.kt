package com.example.kopkarrsui.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.kopkarrsui.data.local.entity.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transaction): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<Transaction>): List<Long>

    @Update
    suspend fun update(transaction: Transaction): Int

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getById(id: Long): Flow<Transaction?>

    @Query("SELECT * FROM transactions WHERE member_id = :memberId ORDER BY tgl DESC LIMIT :limit OFFSET :offset")
    fun getByMemberPaged(memberId: Long, limit: Int, offset: Int): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE member_id = :memberId AND tipe = :tipe ORDER BY tgl DESC")
    fun getByMemberAndType(memberId: Long, tipe: Transaction.TransactionType): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE member_id = :memberId AND tgl BETWEEN :startDate AND :endDate ORDER BY tgl DESC")
    fun getByMemberAndDateRange(memberId: Long, startDate: Long, endDate: Long): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE member_id = :memberId AND status = :status ORDER BY tgl DESC")
    fun getByMemberAndStatus(memberId: Long, status: Transaction.TransactionStatus): Flow<List<Transaction>>

    @Query("SELECT SUM(jumlah) FROM transactions WHERE member_id = :memberId AND tipe = 'belanja' AND status = 'sukses'")
    fun getTotalBelanjaByMember(memberId: Long): Flow<Long?>

    @Query("SELECT SUM(poin_dihasilkan) FROM transactions WHERE member_id = :memberId AND status = 'sukses'")
    fun getTotalPoinByMember(memberId: Long): Flow<Int?>

    @Query("SELECT * FROM transactions WHERE ref_unit_usaha = :unitUsaha AND tgl BETWEEN :startDate AND :endDate ORDER BY tgl DESC")
    fun getByUnitUsahaAndDateRange(unitUsaha: String, startDate: Long, endDate: Long): Flow<List<Transaction>>

    @Query("SELECT COUNT(*) FROM transactions WHERE member_id = :memberId AND status = 'sukses'")
    suspend fun countSuccessByMember(memberId: Long): Int
}