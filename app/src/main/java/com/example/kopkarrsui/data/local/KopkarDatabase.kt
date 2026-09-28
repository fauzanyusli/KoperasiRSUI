package com.example.kopkarrsui.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
