package com.example.kopkarrsui.data.local

import com.example.kopkarrsui.data.local.entity.Admin
import com.example.kopkarrsui.data.local.entity.AuditLog
import com.example.kopkarrsui.data.local.entity.FinancialStatement
import com.example.kopkarrsui.data.local.entity.Member
import com.example.kopkarrsui.data.local.entity.PointLedger
import com.example.kopkarrsui.data.local.entity.SavingsAccount
import com.example.kopkarrsui.data.local.entity.SHUAllocation
import com.example.kopkarrsui.data.local.entity.Transaction

/**
 * Mapper entity ↔ Map untuk Firestore (Firestore SDK tidak mendukung enum,
 * jadi semua enum disimpan sebagai string value-nya).
 */
object Serializers {

    private fun Map<String, Any?>.long(key: String): Long = (this[key] as? Number)?.toLong() ?: 0L
    private fun Map<String, Any?>.int(key: String): Int = (this[key] as? Number)?.toInt() ?: 0
    private fun Map<String, Any?>.double(key: String): Double = (this[key] as? Number)?.toDouble() ?: 0.0
    private fun Map<String, Any?>.bool(key: String): Boolean = this[key] as? Boolean ?: false
    private fun Map<String, Any?>.str(key: String): String? = this[key] as? String

    private fun Map<String, Any?>.longOrNull(key: String): Long? = (this[key] as? Number)?.toLong()

    // --- Member ---

    fun Member.toMap(): Map<String, Any?> = mapOf(
        "id" to id, "noAnggota" to noAnggota, "nama" to nama, "nik" to nik,
        "noHp" to noHp, "email" to email, "alamat" to alamat, "tglGabung" to tglGabung,
        "status" to status.value, "pinHash" to pinHash, "biometricEnabled" to biometricEnabled,
        "createdAt" to createdAt, "updatedAt" to updatedAt
    )

    fun memberFrom(map: Map<String, Any?>) = Member(
        id = map.long("id"),
        noAnggota = map.str("noAnggota").orEmpty(),
        nama = map.str("nama").orEmpty(),
        nik = map.str("nik"),
        noHp = map.str("noHp"),
        email = map.str("email"),
        alamat = map.str("alamat"),
        tglGabung = map.long("tglGabung"),
        status = Member.MemberStatus.entries.firstOrNull { it.value == map.str("status") } ?: Member.MemberStatus.AKTIF,
        pinHash = map.str("pinHash"),
        biometricEnabled = map.bool("biometricEnabled"),
        createdAt = map.long("createdAt"),
        updatedAt = map.long("updatedAt")
    )

    // --- SavingsAccount ---

    fun SavingsAccount.toMap(): Map<String, Any?> = mapOf(
        "id" to id, "memberId" to memberId, "jenis" to jenis.value, "saldo" to saldo,
        "bungaTahunPersen" to bungaTahunPersen, "tglBuka" to tglBuka,
        "status" to status.value, "createdAt" to createdAt, "updatedAt" to updatedAt
    )

    fun savingsFrom(map: Map<String, Any?>) = SavingsAccount(
        id = map.long("id"),
        memberId = map.long("memberId"),
        jenis = SavingsAccount.SavingsType.entries.firstOrNull { it.value == map.str("jenis") } ?: SavingsAccount.SavingsType.WAJIB,
        saldo = map.long("saldo"),
        bungaTahunPersen = map.double("bungaTahunPersen"),
        tglBuka = map.long("tglBuka"),
        status = SavingsAccount.SavingsStatus.entries.firstOrNull { it.value == map.str("status") } ?: SavingsAccount.SavingsStatus.AKTIF,
        createdAt = map.long("createdAt"),
        updatedAt = map.long("updatedAt")
    )

    // --- Transaction ---

    fun Transaction.toMap(): Map<String, Any?> = mapOf(
        "id" to id, "memberId" to memberId, "tipe" to tipe.value, "jumlah" to jumlah,
        "keterangan" to keterangan, "refId" to refId, "refUnitUsaha" to refUnitUsaha,
        "tgl" to tgl, "status" to status.value, "poinDihasilkan" to poinDihasilkan,
        "createdAt" to createdAt
    )

    fun transactionFrom(map: Map<String, Any?>) = Transaction(
        id = map.long("id"),
        memberId = map.long("memberId"),
        tipe = Transaction.TransactionType.entries.firstOrNull { it.value == map.str("tipe") } ?: Transaction.TransactionType.LAINNYA,
        jumlah = map.long("jumlah"),
        keterangan = map.str("keterangan"),
        refId = map.str("refId"),
        refUnitUsaha = map.str("refUnitUsaha"),
        tgl = map.long("tgl"),
        status = Transaction.TransactionStatus.entries.firstOrNull { it.value == map.str("status") } ?: Transaction.TransactionStatus.SUKSES,
        poinDihasilkan = map.int("poinDihasilkan"),
        createdAt = map.long("createdAt")
    )

    // --- PointLedger ---

    fun PointLedger.toMap(): Map<String, Any?> = mapOf(
        "id" to id, "memberId" to memberId, "tipe" to tipe.value, "jumlah" to jumlah,
        "refTransaksiId" to refTransaksiId, "keterangan" to keterangan, "tgl" to tgl,
        "expiredAt" to expiredAt, "createdAt" to createdAt
    )

    fun pointLedgerFrom(map: Map<String, Any?>) = PointLedger(
        id = map.long("id"),
        memberId = map.long("memberId"),
        tipe = PointLedger.PointType.entries.firstOrNull { it.value == map.str("tipe") } ?: PointLedger.PointType.EARN,
        jumlah = map.int("jumlah"),
        refTransaksiId = map.longOrNull("refTransaksiId"),
        keterangan = map.str("keterangan"),
        tgl = map.long("tgl"),
        expiredAt = map.longOrNull("expiredAt"),
        createdAt = map.long("createdAt")
    )

    // --- SHUAllocation ---

    fun SHUAllocation.toMap(): Map<String, Any?> = mapOf(
        "id" to id, "memberId" to memberId, "tahun" to tahun, "jumlah" to jumlah,
        "status" to status.value, "detailJson" to detailJson, "tglHitung" to tglHitung,
        "tglBagi" to tglBagi, "tglCair" to tglCair, "createdAt" to createdAt, "updatedAt" to updatedAt
    )

    fun shuFrom(map: Map<String, Any?>) = SHUAllocation(
        id = map.long("id"),
        memberId = map.long("memberId"),
        tahun = map.int("tahun"),
        jumlah = map.long("jumlah"),
        status = SHUAllocation.SHUStatus.entries.firstOrNull { it.value == map.str("status") } ?: SHUAllocation.SHUStatus.DIHITUNG,
        detailJson = map.str("detailJson"),
        tglHitung = map.long("tglHitung"),
        tglBagi = map.longOrNull("tglBagi"),
        tglCair = map.longOrNull("tglCair"),
        createdAt = map.long("createdAt"),
        updatedAt = map.long("updatedAt")
    )

    // --- FinancialStatement ---

    fun FinancialStatement.toMap(): Map<String, Any?> = mapOf(
        "id" to id, "tahun" to tahun, "neracaJson" to neracaJson, "labaRugiJson" to labaRugiJson,
        "shuTotal" to shuTotal, "cadanganWajib" to cadanganWajib, "pembangunan" to pembangunan,
        "bagiHasilTotal" to bagiHasilTotal, "status" to status.value, "tglRAT" to tglRAT,
        "createdAt" to createdAt, "updatedAt" to updatedAt
    )

    fun financialFrom(map: Map<String, Any?>) = FinancialStatement(
        id = map.long("id"),
        tahun = map.int("tahun"),
        neracaJson = map.str("neracaJson").orEmpty(),
        labaRugiJson = map.str("labaRugiJson").orEmpty(),
        shuTotal = map.long("shuTotal"),
        cadanganWajib = map.long("cadanganWajib"),
        pembangunan = map.long("pembangunan"),
        bagiHasilTotal = map.long("bagiHasilTotal"),
        status = FinancialStatement.FinancialStatus.entries.firstOrNull { it.value == map.str("status") } ?: FinancialStatement.FinancialStatus.DRAFT,
        tglRAT = map.longOrNull("tglRAT"),
        createdAt = map.long("createdAt"),
        updatedAt = map.long("updatedAt")
    )

    // --- Admin ---

    fun Admin.toMap(): Map<String, Any?> = mapOf(
        "id" to id, "memberId" to memberId, "role" to role.value, "izinJson" to izinJson,
        "aktifSejak" to aktifSejak, "status" to status.value,
        "createdAt" to createdAt, "updatedAt" to updatedAt
    )

    fun adminFrom(map: Map<String, Any?>) = Admin(
        id = map.long("id"),
        memberId = map.long("memberId"),
        role = Admin.AdminRole.entries.firstOrNull { it.value == map.str("role") } ?: Admin.AdminRole.KETUA,
        izinJson = map.str("izinJson"),
        aktifSejak = map.long("aktifSejak"),
        status = Admin.AdminStatus.entries.firstOrNull { it.value == map.str("status") } ?: Admin.AdminStatus.AKTIF,
        createdAt = map.long("createdAt"),
        updatedAt = map.long("updatedAt")
    )

    // --- AuditLog ---

    fun AuditLog.toMap(): Map<String, Any?> = mapOf(
        "id" to id, "memberId" to memberId, "tableName" to tableName, "recordId" to recordId,
        "action" to action.value, "oldValue" to oldValue, "newValue" to newValue,
        "description" to description, "ipAddress" to ipAddress, "createdAt" to createdAt
    )

    fun auditLogFrom(map: Map<String, Any?>) = AuditLog(
        id = map.long("id"),
        memberId = map.longOrNull("memberId"),
        tableName = map.str("tableName").orEmpty(),
        recordId = map.longOrNull("recordId"),
        action = AuditLog.AuditAction.entries.firstOrNull { it.value == map.str("action") } ?: AuditLog.AuditAction.CREATE,
        oldValue = map.str("oldValue"),
        newValue = map.str("newValue"),
        description = map.str("description"),
        ipAddress = map.str("ipAddress"),
        createdAt = map.long("createdAt")
    )

    /** Registry untuk backup/restore: nama koleksi ↔ (toMap, fromMap, id). */
    val all: List<BackupCollection<*>> = listOf(
        BackupCollection("members", { m: Member -> m.toMap() }, ::memberFrom, { it.id }),
        BackupCollection("savings_accounts", { m: SavingsAccount -> m.toMap() }, ::savingsFrom, { it.id }),
        BackupCollection("transactions", { m: Transaction -> m.toMap() }, ::transactionFrom, { it.id }),
        BackupCollection("point_ledgers", { m: PointLedger -> m.toMap() }, ::pointLedgerFrom, { it.id }),
        BackupCollection("shu_allocations", { m: SHUAllocation -> m.toMap() }, ::shuFrom, { it.id }),
        BackupCollection("financial_statements", { m: FinancialStatement -> m.toMap() }, ::financialFrom, { it.id }),
        BackupCollection("admins", { m: Admin -> m.toMap() }, ::adminFrom, { it.id }),
        BackupCollection("audit_logs", { m: AuditLog -> m.toMap() }, ::auditLogFrom, { it.id })
    )

    data class BackupCollection<T>(
        val name: String,
        val toMap: (T) -> Map<String, Any?>,
        val fromMap: (Map<String, Any?>) -> T,
        val id: (T) -> Long
    )
}
