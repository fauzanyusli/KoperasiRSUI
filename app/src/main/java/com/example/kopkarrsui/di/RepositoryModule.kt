package com.example.kopkarrsui.di

import com.example.kopkarrsui.data.repository.FinancialStatementRepositoryImpl
import com.example.kopkarrsui.data.repository.MemberRepositoryImpl
import com.example.kopkarrsui.data.repository.PointRepositoryImpl
import com.example.kopkarrsui.data.repository.SavingsRepositoryImpl
import com.example.kopkarrsui.data.repository.SHURepositoryImpl
import com.example.kopkarrsui.data.repository.TransactionRepositoryImpl
import com.example.kopkarrsui.domain.repository.FinancialStatementRepository
import com.example.kopkarrsui.domain.repository.MemberRepository
import com.example.kopkarrsui.domain.repository.PointRepository
import com.example.kopkarrsui.domain.repository.SavingsRepository
import com.example.kopkarrsui.domain.repository.SHURepository
import com.example.kopkarrsui.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMemberRepository(impl: MemberRepositoryImpl): MemberRepository

    @Binds
    @Singleton
    abstract fun bindSavingsRepository(impl: SavingsRepositoryImpl): SavingsRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindPointRepository(impl: PointRepositoryImpl): PointRepository

    @Binds
    @Singleton
    abstract fun bindSHURepository(impl: SHURepositoryImpl): SHURepository

    @Binds
    @Singleton
    abstract fun bindFinancialStatementRepository(impl: FinancialStatementRepositoryImpl): FinancialStatementRepository
}