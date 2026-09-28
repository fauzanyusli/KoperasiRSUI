package com.example.kopkarrsui.data.repository

import com.example.kopkarrsui.data.local.dao.MemberDao
import com.example.kopkarrsui.data.local.entity.Member
import com.example.kopkarrsui.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemberRepositoryImpl @Inject constructor(
    private val memberDao: MemberDao
) : MemberRepository {

    override suspend fun insert(member: Member): Long = memberDao.insert(member)

    override suspend fun update(member: Member): Int = memberDao.update(member)

    override fun getById(id: Long): Flow<Member?> = memberDao.getById(id)

    override fun getByNoAnggota(noAnggota: String): Flow<Member?> = memberDao.getByNoAnggota(noAnggota)

    override fun getByStatus(status: Member.MemberStatus): Flow<List<Member>> = memberDao.getByStatus(status)

    override fun getAll(): Flow<List<Member>> = memberDao.getAll()

    override suspend fun countActive(): Int = memberDao.countActive()

    override suspend fun deleteById(id: Long): Int = memberDao.deleteById(id)

    override fun getByNik(nik: String): Flow<Member?> = memberDao.getByNik(nik)

    override fun getByNoHp(noHp: String): Flow<Member?> = memberDao.getByNoHp(noHp)
}