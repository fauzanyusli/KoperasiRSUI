package com.example.kopkarrsui.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.kopkarrsui.data.local.dao.AdminDao
import com.example.kopkarrsui.data.local.dao.AuditLogDao
import com.example.kopkarrsui.data.local.dao.FinancialStatementDao
import com.example.kopkarrsui.data.local.dao.MemberDao
import com.example.kopkarrsui.data.local.dao.PointLedgerDao
import com.example.kopkarrsui.data.local.dao.SHUAllocationDao
import com.example.kopkarrsui.data.local.dao.SavingsAccountDao
import com.example.kopkarrsui.data.local.dao.TransactionDao
import com.example.kopkarrsui.data.local.entity.Admin
import com.example.kopkarrsui.data.local.entity.AuditLog
import com.example.kopkarrsui.data.local.entity.FinancialStatement
import com.example.kopkarrsui.data.local.entity.Member
import com.example.kopkarrsui.data.local.entity.PointLedger
import com.example.kopkarrsui.data.local.entity.SHUAllocation
import com.example.kopkarrsui.data.local.entity.SavingsAccount
import com.example.kopkarrsui.data.local.entity.Transaction
import com.example.kopkarrsui.util.PasswordUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Member::class,
        SavingsAccount::class,
        Transaction::class,
        PointLedger::class,
        SHUAllocation::class,
        FinancialStatement::class,
        Admin::class,
        AuditLog::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class KopkarDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun savingsAccountDao(): SavingsAccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun pointLedgerDao(): PointLedgerDao
    abstract fun shuAllocationDao(): SHUAllocationDao
    abstract fun financialStatementDao(): FinancialStatementDao
    abstract fun adminDao(): AdminDao
    abstract fun auditLogDao(): AuditLogDao

    companion object {
        @Volatile
        private var INSTANCE: KopkarDatabase? = null

        fun getDatabase(context: Context): KopkarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KopkarDatabase::class.java,
                    "kopkar_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(SeedCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class SeedCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    seedData(database)
                }
            }
        }

        private suspend fun seedData(database: KopkarDatabase) {
            val memberDao = database.memberDao()
            val savingsDao = database.savingsAccountDao()

            // Seed 3 anggota untuk testing
            val members = listOf(
                Member(
                    noAnggota = "KPR-001",
                    nama = "Budi Santoso",
                    nik = "3201234567890001",
                    noHp = "081234567890",
                    email = "budi@example.com",
                    alamat = "Jl. Sudirman No. 1, Jakarta",
                    tglGabung = System.currentTimeMillis() - (365L * 24 * 60 * 60 * 1000 * 3), // 3 tahun lalu
                    status = Member.MemberStatus.AKTIF,
                    pinHash = PasswordUtils.hashPin("123456")
                ),
                Member(
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

            val memberIds = memberDao.insertAll(members)

            // Seed tabungan untuk masing-masing anggota
            memberIds.forEachIndexed { index, memberId ->
                val saldo = when (index) {
                    0 -> 2_500_000L
                    1 -> 1_800_000L
                    else -> 3_200_000L
                }
                savingsDao.insert(
                    SavingsAccount(
                        memberId = memberId,
                        jenis = SavingsAccount.SavingsType.WAJIB,
                        saldo = saldo,
                        status = SavingsAccount.SavingsStatus.AKTIF
                    )
                )
            }
        }
    }
}
