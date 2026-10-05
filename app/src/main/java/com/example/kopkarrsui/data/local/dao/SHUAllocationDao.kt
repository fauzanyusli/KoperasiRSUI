package com.example.kopkarrsui.data.local.dao

import com.example.kopkarrsui.data.local.FirestoreSupport.docFlow
import com.example.kopkarrsui.data.local.FirestoreSupport.fetch
import com.example.kopkarrsui.data.local.FirestoreSupport.queryFlow
import com.example.kopkarrsui.data.local.FirestoreSupport.save
import com.example.kopkarrsui.data.local.Serializers.shuFrom
import com.example.kopkarrsui.data.local.Serializers.toMap
import com.example.kopkarrsui.data.local.entity.SHUAllocation
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Pengganti Room SHUAllocationDao (lihat [com.example.kopkarrsui.data.local.FirestoreSupport]). */
class SHUAllocationDao @Inject constructor(private val db: FirebaseFirestore) {

    private fun col() = db.collection("shu_allocations")

    suspend fun insert(allocation: SHUAllocation): Long =
        db.save("shu_allocations", allocation.id, allocation.toMap())

    suspend fun insertAll(allocations: List<SHUAllocation>): List<Long> = allocations.map { insert(it) }

    suspend fun update(allocation: SHUAllocation): Int {
        if (allocation.id == 0L) return 0
        db.save("shu_allocations", allocation.id, allocation.toMap())
        return 1
    }

    fun getById(id: Long): Flow<SHUAllocation?> =
        docFlow(col().document(id.toString()), ::shuFrom)

    fun getByMemberAndYear(memberId: Long, tahun: Int): Flow<SHUAllocation?> =
        queryFlow(col().whereEqualTo("memberId", memberId).whereEqualTo("tahun", tahun), ::shuFrom)
            .map { it.firstOrNull() }.distinctUntilChanged()

    fun getByMember(memberId: Long): Flow<List<SHUAllocation>> =
        queryFlow(col().whereEqualTo("memberId", memberId), ::shuFrom) {
            it.sortedByDescending { a -> a.tahun }
        }

    fun getByYear(tahun: Int): Flow<List<SHUAllocation>> =
        queryFlow(col().whereEqualTo("tahun", tahun), ::shuFrom) {
            it.sortedByDescending { a -> a.jumlah }
        }

    fun getByYearAndStatus(tahun: Int, status: SHUAllocation.SHUStatus): Flow<List<SHUAllocation>> =
        queryFlow(col().whereEqualTo("tahun", tahun).whereEqualTo("status", status.value), ::shuFrom) {
            it.sortedByDescending { a -> a.jumlah }
        }

    suspend fun getTotalDistributedByYear(tahun: Int): Long =
        fetch(col().whereEqualTo("tahun", tahun), ::shuFrom)
            .filter { it.status.value == "dibagikan" || it.status.value == "dicairkan" }
            .sumOf { it.jumlah }

    suspend fun updateStatusToDistributed(tahun: Int, status: SHUAllocation.SHUStatus, tglBagi: Long, updatedAt: Long): Int {
        val targets = fetch(
            col().whereEqualTo("tahun", tahun).whereEqualTo("status", "dihitung"),
            ::shuFrom
        )
        if (targets.isEmpty()) return 0
        val batch = db.batch()
        targets.forEach { allocation ->
            batch.update(
                col().document(allocation.id.toString()),
                mapOf("status" to status.value, "tglBagi" to tglBagi, "updatedAt" to updatedAt)
            )
        }
        batch.commit().await()
        return targets.size
    }

    suspend fun updateToCair(id: Long, tglCair: Long, updatedAt: Long): Int {
        val ref = col().document(id.toString())
        if (!ref.get().await().exists()) return 0
        ref.update(mapOf("status" to "dicairkan", "tglCair" to tglCair, "updatedAt" to updatedAt)).await()
        return 1
    }

    suspend fun countByYear(tahun: Int): Int =
        fetch(col().whereEqualTo("tahun", tahun), ::shuFrom).size
}
