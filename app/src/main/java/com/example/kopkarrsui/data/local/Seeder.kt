package com.example.kopkarrsui.data.local

import com.example.kopkarrsui.data.local.FirestoreSupport.save
import com.example.kopkarrsui.data.local.FirestoreSupport.setCounter
import com.example.kopkarrsui.data.local.Serializers.toMap
import com.example.kopkarrsui.data.local.entity.Admin
import com.example.kopkarrsui.data.local.entity.Member
import com.example.kopkarrsui.data.local.entity.SavingsAccount
import com.example.kopkarrsui.util.PasswordUtils
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Seeder pengganti seed Room (KopkarDatabase.SeedCallback).
 * Jalan sekali di Application.onCreate — hanya kalau koleksi members masih kosong.
 * ID eksplisit 1..3 karena SessionManager dev-mode bawa member id 1 (KPR-001).
 */
object Seeder {

    suspend fun ensureSeeded() {
        val db = FirebaseFirestore.getInstance()
        if (!db.collection("members").limit(1).get().await().isEmpty) return

        val members = listOf(
            Member(
                id = 1,
                noAnggota = "KPR-001",
                nama = "Budi Santoso",
                nik = "3201234567890001",
                noHp = "081234567890",
                email = "budi@example.com",
                alamat = "Jl. Sudirman No. 1, Jakarta",
                tglGabung = System.currentTimeMillis() - (365L * 24 * 60 * 60 * 1000 * 3),
                status = Member.MemberStatus.AKTIF,
                pinHash = PasswordUtils.hashPin("123456")
            ),
            Member(
                id = 2,
                noAnggota = "KPR-002",
                nama = "Siti Rahayu",
                nik = "3201234567890002",
                noHp = "081234567891",
                email = "siti@example.com",
                alamat = "Jl. Gatot Subroto No. 5, Jakarta",
                tglGabung = System.currentTimeMillis() - (365L * 24 * 60 * 60 * 1000 * 2),
                status = Member.MemberStatus.AKTIF,
                pinHash = PasswordUtils.hashPin("123456")
            ),
            Member(
                id = 3,
                noAnggota = "KPR-003",
                nama = "Ahmad Fauzi",
                nik = "3201234567890003",
                noHp = "081234567892",
                email = "ahmad@example.com",
                alamat = "Jl. Thamrin No. 10, Jakarta",
                tglGabung = System.currentTimeMillis() - (365L * 24 * 60 * 60 * 1000),
                status = Member.MemberStatus.AKTIF,
                pinHash = PasswordUtils.hashPin("123456")
            )
        )
        members.forEach { db.save("members", it.id, it.toMap()) }

        // Pengurus: KPR-001 sebagai Ketua (login -> AdminDashboard)
        db.save("admins", 1, Admin(memberId = 1, role = Admin.AdminRole.KETUA, izinJson = null).toMap())

        val balances = listOf(2_500_000L, 1_800_000L, 3_200_000L)
        members.forEachIndexed { index, member ->
            db.save(
                "savings_accounts",
                (index + 1).toLong(),
                SavingsAccount(
                    memberId = member.id,
                    jenis = SavingsAccount.SavingsType.WAJIB,
                    saldo = balances[index],
                    status = SavingsAccount.SavingsStatus.AKTIF
                ).toMap()
            )
        }

        db.setCounter("members", members.size.toLong())
        db.setCounter("admins", 1L)
        db.setCounter("savings_accounts", members.size.toLong())
    }
}
