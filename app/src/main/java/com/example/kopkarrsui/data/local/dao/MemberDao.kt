package com.example.kopkarrsui.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.kopkarrsui.data.local.entity.Member
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(member: Member): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(members: List<Member>): List<Long>

    @Update
    suspend fun update(member: Member): Int

    @Query("SELECT * FROM members WHERE id = :id")
    fun getById(id: Long): Flow<Member?>

    @Query("SELECT * FROM members WHERE no_anggota = :noAnggota")
    fun getByNoAnggota(noAnggota: String): Flow<Member?>

    @Query("SELECT * FROM members WHERE status = :status ORDER BY nama ASC")
    fun getByStatus(status: Member.MemberStatus): Flow<List<Member>>

    @Query("SELECT * FROM members ORDER BY nama ASC")
    fun getAll(): Flow<List<Member>>

    @Query("SELECT COUNT(*) FROM members WHERE status = 'aktif'")
    suspend fun countActive(): Int

    @Query("DELETE FROM members WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Query("SELECT * FROM members WHERE nik = :nik")
    fun getByNik(nik: String): Flow<Member?>

    @Query("SELECT * FROM members WHERE no_hp = :noHp")
    fun getByNoHp(noHp: String): Flow<Member?>
}