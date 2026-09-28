package com.example.kopkarrsui.data.local

import androidx.room.TypeConverter
import com.example.kopkarrsui.data.local.entity.Admin
import com.example.kopkarrsui.data.local.entity.FinancialStatement
import com.example.kopkarrsui.data.local.entity.Member
import com.example.kopkarrsui.data.local.entity.PointLedger
import com.example.kopkarrsui.data.local.entity.SHUAllocation
import com.example.kopkarrsui.data.local.entity.SavingsAccount
import com.example.kopkarrsui.data.local.entity.Transaction

class Converters {
    @TypeConverter
    fun fromMemberStatus(value: String?): Member.MemberStatus? =
        value?.let { Member.MemberStatus.valueOf(it.uppercase()) }

    @TypeConverter
    fun toMemberStatus(status: Member.MemberStatus?): String? = status?.value

    @TypeConverter
    fun fromSavingsType(value: String?): SavingsAccount.SavingsType? =
        value?.let { SavingsAccount.SavingsType.valueOf(it.uppercase()) }

    @TypeConverter
    fun toSavingsType(type: SavingsAccount.SavingsType?): String? = type?.value

    @TypeConverter
    fun fromSavingsStatus(value: String?): SavingsAccount.SavingsStatus? =
        value?.let { SavingsAccount.SavingsStatus.valueOf(it.uppercase()) }

    @TypeConverter
    fun toSavingsStatus(status: SavingsAccount.SavingsStatus?): String? = status?.value

    @TypeConverter
    fun fromTransactionType(value: String?): Transaction.TransactionType? =
        value?.let { Transaction.TransactionType.valueOf(it.uppercase()) }

    @TypeConverter
    fun toTransactionType(type: Transaction.TransactionType?): String? = type?.value

    @TypeConverter
    fun fromTransactionStatus(value: String?): Transaction.TransactionStatus? =
        value?.let { Transaction.TransactionStatus.valueOf(it.uppercase()) }

    @TypeConverter
    fun toTransactionStatus(status: Transaction.TransactionStatus?): String? = status?.value

    @TypeConverter
    fun fromPointType(value: String?): PointLedger.PointType? =
        value?.let { PointLedger.PointType.valueOf(it.uppercase()) }

    @TypeConverter
    fun toPointType(type: PointLedger.PointType?): String? = type?.value

    @TypeConverter
    fun fromSHUStatus(value: String?): SHUAllocation.SHUStatus? =
        value?.let { SHUAllocation.SHUStatus.valueOf(it.uppercase()) }

    @TypeConverter
    fun toSHUStatus(status: SHUAllocation.SHUStatus?): String? = status?.value

    @TypeConverter
    fun fromFinancialStatus(value: String?): FinancialStatement.FinancialStatus? =
        value?.let { FinancialStatement.FinancialStatus.valueOf(it.uppercase()) }

    @TypeConverter
    fun toFinancialStatus(status: FinancialStatement.FinancialStatus?): String? = status?.value

    @TypeConverter
    fun fromAdminRole(value: String?): Admin.AdminRole? =
        value?.let { Admin.AdminRole.valueOf(it.uppercase()) }

    @TypeConverter
    fun toAdminRole(role: Admin.AdminRole?): String? = role?.value

    @TypeConverter
    fun fromAdminStatus(value: String?): Admin.AdminStatus? =
        value?.let { Admin.AdminStatus.valueOf(it.uppercase()) }

    @TypeConverter
    fun toAdminStatus(status: Admin.AdminStatus?): String? = status?.value
}
