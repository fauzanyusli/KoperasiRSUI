package com.example.kopkarrsui.data.local.dao

import com.example.kopkarrsui.data.local.FirestoreSupport.docFlow
import com.example.kopkarrsui.data.local.FirestoreSupport.fetch
import com.example.kopkarrsui.data.local.FirestoreSupport.queryFlow
import com.example.kopkarrsui.data.local.FirestoreSupport.save
import com.example.kopkarrsui.data.local.Serializers.memberFrom
import com.example.kopkarrsui.data.local.Serializers.toMap
import com.example.kopkarrsui.data.local.entity.Member
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Pengganti Room MemberDao — dibaca [com.example.kopkarrsui.data.local.FirestoreSupport].
 * Signature disamakan dengan DAO Room supaya repository/viewmodel tidak berubah.
 */
class MemberDao @Inject constructor(private val db: FirebaseFirestore) {

    private fun col() = db.collection("members")

    suspend fun insert(member: Member): Long = db.save("members", member.id, member.toMap())

    suspend fun insertAll(members: List<Member>): List<Long> = members.map { insert(it) }

    suspend fun update(member: Member): Int {
        if (member.id == 0L) return 0
        db.save("members", member.id, member.toMap())
        return 1
    }

    fun getById(id: Long): Flow<Member?> =
        docFlow(db.collection("members").document(id.toString()), ::memberFrom)

    fun getByNoAnggota(noAnggota: String): Flow<Member?> =
        queryFlow(col().whereEqualTo("noAnggota", noAnggota), ::memberFrom)
            .map { it.firstOrNull() }.distinctUntilChanged()

    fun getByStatus(status: Member.MemberStatus): Flow<List<Member>> =
        queryFlow(col().whereEqualTo("status", status.value), ::memberFrom) {
            it.sortedBy { m -> m.nama }
        }

    fun getAll(): Flow<List<Member>> =
        queryFlow(col(), ::memberFrom) { it.sortedBy { m -> m.nama } }

    suspend fun countActive(): Int =
        fetch(col().whereEqualTo("status", "aktif"), ::memberFrom).size

    suspend fun deleteById(id: Long): Int {
        val ref = db.collection("members").document(id.toString())
        if (!ref.get().await().exists()) return 0
        ref.delete().await()
        return 1
    }

    fun getByNik(nik: String): Flow<Member?> =
        queryFlow(col().whereEqualTo("nik", nik), ::memberFrom)
            .map { it.firstOrNull() }.distinctUntilChanged()

    fun getByNoHp(noHp: String): Flow<Member?> =
        queryFlow(col().whereEqualTo("noHp", noHp), ::memberFrom)
            .map { it.firstOrNull() }.distinctUntilChanged()
}
