package com.example.kopkarrsui.di

import android.content.Context
import com.example.kopkarrsui.data.local.KopkarDatabase
import com.example.kopkarrsui.data.local.dao.AdminDao
import com.example.kopkarrsui.data.local.dao.AuditLogDao
import com.example.kopkarrsui.data.local.dao.FinancialStatementDao
import com.example.kopkarrsui.data.local.dao.MemberDao
import com.example.kopkarrsui.data.local.dao.PointLedgerDao
import com.example.kopkarrsui.data.local.dao.SHUAllocationDao
import com.example.kopkarrsui.data.local.dao.SavingsAccountDao
import com.example.kopkarrsui.data.local.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): KopkarDatabase =
        KopkarDatabase.getDatabase(context)

    @Provides fun provideMemberDao(db: KopkarDatabase): MemberDao = db.memberDao()
    @Provides fun provideSavingsAccountDao(db: KopkarDatabase): SavingsAccountDao = db.savingsAccountDao()
    @Provides fun provideTransactionDao(db: KopkarDatabase): TransactionDao = db.transactionDao()
    @Provides fun providePointLedgerDao(db: KopkarDatabase): PointLedgerDao = db.pointLedgerDao()
    @Provides fun provideSHUAllocationDao(db: KopkarDatabase): SHUAllocationDao = db.shuAllocationDao()
    @Provides fun provideFinancialStatementDao(db: KopkarDatabase): FinancialStatementDao = db.financialStatementDao()
    @Provides fun provideAdminDao(db: KopkarDatabase): AdminDao = db.adminDao()
    @Provides fun provideAuditLogDao(db: KopkarDatabase): AuditLogDao = db.auditLogDao()
}
