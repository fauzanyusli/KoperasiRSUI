package com.example.kopkarrsui.data.local.dao

import com.example.kopkarrsui.data.local.FirestoreSupport.docFlow
import com.example.kopkarrsui.data.local.FirestoreSupport.queryFlow
import com.example.kopkarrsui.data.local.FirestoreSupport.save
import com.example.kopkarrsui.data.local.Serializers.pointLedgerFrom
import com.example.kopkarrsui.data.local.Serializers.toMap
import com.example.kopkarrsui.data.local.entity.PointLedger
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Pengganti Room PointLedgerDao (lihat [com.example.kopkarrsui.data.local.FirestoreSupport]). */
class PointLedgerDao @Inject constructor(private val db: FirebaseFirestore) {

    private fun col() = db.collection("point_ledgers")

    suspend fun insert(ledger: PointLedger): Long =
        db.save("point_ledgers", ledger.id, ledger.toMap())

    suspend fun insertAll(ledgers: List<PointLedger>): List<Long> = ledgers.map { insert(it) }

    fun getById(id: Long): Flow<PointLedger?> =
        docFlow(col().document(id.toString()), ::pointLedgerFrom)

    fun getByMemberPaged(memberId: Long, limit: Int, offset: Int): Flow<List<PointLedger>> =
        queryFlow(col().whereEqualTo("memberId", memberId), ::pointLedgerFrom) {
            it.sortedByDescending { l -> l.tgl }.drop(offset).take(limit)
        }

    fun getByMemberAndType(memberId: Long, tipe: PointLedger.PointType): Flow<List<PointLedger>> =
        queryFlow(col().whereEqualTo("memberId", memberId).whereEqualTo("tipe", tipe.value), ::pointLedgerFrom) {
            it.sortedByDescending { l -> l.tgl }
        }

    fun getTotalPoinByMember(memberId: Long): Flow<Int?> =
        queryFlow(col().whereEqualTo("memberId", memberId), ::pointLedgerFrom)
            .map { list -> list.sumOf { it.jumlah }.takeIf { list.isNotEmpty() } }

    fun getTotalEarnedByMember(memberId: Long): Flow<Int?> =
        queryFlow(col().whereEqualTo("memberId", memberId).whereEqualTo("tipe", "earn"), ::pointLedgerFrom)
            .map { list -> list.sumOf { it.jumlah }.takeIf { list.isNotEmpty() } }

    fun getTotalRedeemedByMember(memberId: Long): Flow<Int?> =
        queryFlow(col().whereEqualTo("memberId", memberId).whereEqualTo("tipe", "redeem"), ::pointLedgerFrom)
            .map { list -> list.sumOf { it.jumlah }.takeIf { list.isNotEmpty() } }

    fun getTotalExpiredByMember(memberId: Long): Flow<Int?> =
        queryFlow(col().whereEqualTo("memberId", memberId).whereEqualTo("tipe", "expire"), ::pointLedgerFrom)
            .map { list -> list.sumOf { it.jumlah }.takeIf { list.isNotEmpty() } }

    fun getByRefTransaksi(refTransaksiId: Long): Flow<List<PointLedger>> =
        queryFlow(col().whereEqualTo("refTransaksiId", refTransaksiId), ::pointLedgerFrom) {
            it.sortedByDescending { l -> l.tgl }
        }

    fun getExpiredPoints(now: Long): Flow<List<PointLedger>> =
        queryFlow(col().whereEqualTo("tipe", "earn"), ::pointLedgerFrom) { list ->
            list.filter { it.expiredAt != null && it.expiredAt <= now }.sortedBy { l -> l.expiredAt }
        }
}
