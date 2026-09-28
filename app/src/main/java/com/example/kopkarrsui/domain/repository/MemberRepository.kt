package com.example.kopkarrsui.domain.repository

import com.example.kopkarrsui.data.local.entity.Member
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    suspend fun insert(member: Member): Long
    suspend fun update(member: Member): Int
    fun getById(id: Long): Flow<Member?>
    fun getByNoAnggota(noAnggota: String): Flow<Member?>
    fun getByStatus(status: Member.MemberStatus): Flow<List<Member>>
    fun getAll(): Flow<List<Member>>
    suspend fun countActive(): Int
    suspend fun deleteById(id: Long): Int
    fun getByNik(nik: String): Flow<Member?>
    fun getByNoHp(noHp: String): Flow<Member?>
}