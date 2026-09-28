package com.example.kopkarrsui.domain.usecase

import com.example.kopkarrsui.data.local.entity.Member
import com.example.kopkarrsui.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemberUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {

    suspend fun registerMember(member: Member): Result<Long> {
        return try {
            val id = memberRepository.insert(member)
            Result.Success(id)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun updateMember(member: Member): Result<Int> {
        return try {
            val count = memberRepository.update(member)
            Result.Success(count)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    fun getMemberById(id: Long): Flow<Member?> = memberRepository.getById(id)

    fun getMemberByNoAnggota(noAnggota: String): Flow<Member?> = memberRepository.getByNoAnggota(noAnggota)

    fun getActiveMembers(): Flow<List<Member>> = memberRepository.getByStatus(Member.MemberStatus.AKTIF)

    fun getAllMembers(): Flow<List<Member>> = memberRepository.getAll()

    suspend fun getActiveMemberCount(): Int = memberRepository.countActive()

    sealed interface Result<out T> {
        data class Success<T>(val value: T) : Result<T>
        data class Failure(val exception: Exception) : Result<Nothing>
    }
}