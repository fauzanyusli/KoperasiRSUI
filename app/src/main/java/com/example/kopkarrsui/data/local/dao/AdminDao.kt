package com.example.kopkarrsui.data.local.dao

import com.example.kopkarrsui.data.local.FirestoreSupport.docFlow
import com.example.kopkarrsui.data.local.FirestoreSupport.queryFlow
import com.example.kopkarrsui.data.local.FirestoreSupport.save
import com.example.kopkarrsui.data.local.Serializers.adminFrom
import com.example.kopkarrsui.data.local.Serializers.toMap
import com.example.kopkarrsui.data.local.entity.Admin
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Pengganti Room AdminDao (lihat [com.example.kopkarrsui.data.local.FirestoreSupport]). */
class AdminDao @Inject constructor(private val db: FirebaseFirestore) {

    private fun col() = db.collection("admins")

    suspend fun insert(admin: Admin): Long = db.save("admins", admin.id, admin.toMap())

    suspend fun insertAll(admins: List<Admin>): List<Long> = admins.map { insert(it) }

    suspend fun update(admin: Admin): Int {
        if (admin.id == 0L) return 0
        db.save("admins", admin.id, admin.toMap())
        return 1
    }

    fun getById(id: Long): Flow<Admin?> =
        docFlow(col().document(id.toString()), ::adminFrom)

    fun getByMemberId(memberId: Long): Flow<Admin?> =
        queryFlow(col().whereEqualTo("memberId", memberId), ::adminFrom)
            .map { it.firstOrNull() }.distinctUntilChanged()

    fun getByRole(role: Admin.AdminRole): Flow<List<Admin>> =
        queryFlow(col().whereEqualTo("role", role.value).whereEqualTo("status", "aktif"), ::adminFrom)

    fun getAllActive(): Flow<List<Admin>> =
        queryFlow(col().whereEqualTo("status", "aktif"), ::adminFrom)

    fun getAll(): Flow<List<Admin>> =
        queryFlow(col(), ::adminFrom) { it.sortedBy { a -> a.role.value } }

    suspend fun updateStatus(id: Long, status: Admin.AdminStatus, updatedAt: Long): Int {
        val ref = col().document(id.toString())
        if (!ref.get().await().exists()) return 0
        ref.update(mapOf("status" to status.value, "updatedAt" to updatedAt)).await()
        return 1
    }

    fun getActiveByMemberId(memberId: Long): Flow<Admin?> =
        queryFlow(col().whereEqualTo("memberId", memberId).whereEqualTo("status", "aktif"), ::adminFrom)
            .map { it.firstOrNull() }.distinctUntilChanged()
}
