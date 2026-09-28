package com.example.kopkarrsui.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kopkarrsui.data.local.entity.PointLedger
import kotlinx.coroutines.flow.Flow

@Dao
interface PointLedgerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ledger: PointLedger): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ledgers: List<PointLedger>): List<Long>

    @Query("SELECT * FROM point_ledgers WHERE id = :id")
    fun getById(id: Long): Flow<PointLedger?>

    @Query("SELECT * FROM point_ledgers WHERE member_id = :memberId ORDER BY tgl DESC LIMIT :limit OFFSET :offset")
    fun getByMemberPaged(memberId: Long, limit: Int, offset: Int): Flow<List<PointLedger>>

    @Query("SELECT * FROM point_ledgers WHERE member_id = :memberId AND tipe = :tipe ORDER BY tgl DESC")
    fun getByMemberAndType(memberId: Long, tipe: PointLedger.PointType): Flow<List<PointLedger>>

    @Query("SELECT SUM(jumlah) FROM point_ledgers WHERE member_id = :memberId")
    fun getTotalPoinByMember(memberId: Long): Flow<Int?>

    @Query("SELECT SUM(jumlah) FROM point_ledgers WHERE member_id = :memberId AND tipe = 'earn'")
    fun getTotalEarnedByMember(memberId: Long): Flow<Int?>

    @Query("SELECT SUM(jumlah) FROM point_ledgers WHERE member_id = :memberId AND tipe = 'redeem'")
    fun getTotalRedeemedByMember(memberId: Long): Flow<Int?>

    @Query("SELECT SUM(jumlah) FROM point_ledgers WHERE member_id = :memberId AND tipe = 'expire'")
    fun getTotalExpiredByMember(memberId: Long): Flow<Int?>

    @Query("SELECT * FROM point_ledgers WHERE ref_transaksi_id = :refTransaksiId")
    fun getByRefTransaksi(refTransaksiId: Long): Flow<List<PointLedger>>

    @Query("SELECT * FROM point_ledgers WHERE expired_at IS NOT NULL AND expired_at <= :now AND tipe = 'earn'")
    fun getExpiredPoints(now: Long): Flow<List<PointLedger>>
}