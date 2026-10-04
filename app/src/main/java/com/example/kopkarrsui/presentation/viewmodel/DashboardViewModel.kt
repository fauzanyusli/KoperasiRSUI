package com.example.kopkarrsui.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.app.Application
import com.example.kopkarrsui.data.local.SessionManager
import com.example.kopkarrsui.data.local.entity.Member
import com.example.kopkarrsui.data.local.entity.SHUAllocation
import com.example.kopkarrsui.data.local.entity.Transaction
import com.example.kopkarrsui.domain.repository.MemberRepository
import com.example.kopkarrsui.domain.repository.SavingsRepository
import com.example.kopkarrsui.domain.repository.TransactionRepository
import com.example.kopkarrsui.domain.repository.PointRepository
import com.example.kopkarrsui.domain.repository.SHURepository
import com.example.kopkarrsui.util.NotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val savingsRepository: SavingsRepository,
    private val transactionRepository: TransactionRepository,
    private val pointRepository: PointRepository,
    private val shuRepository: SHURepository,
    private val sessionManager: SessionManager,
    private val application: Application
) : ViewModel() {

    private val _member = MutableStateFlow<Member?>(null)
    val member: StateFlow<Member?> = _member.asStateFlow()

    private val _totalSaldo = MutableStateFlow(0L)
    val totalSaldo: StateFlow<Long> = _totalSaldo.asStateFlow()

    private val _totalPoin = MutableStateFlow(0)
    val totalPoin: StateFlow<Int> = _totalPoin.asStateFlow()

    private val _latestSHU = MutableStateFlow<SHUAllocation?>(null)
    val latestSHU: StateFlow<SHUAllocation?> = _latestSHU.asStateFlow()

    private val _recentTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val recentTransactions: StateFlow<List<Transaction>> = _recentTransactions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        val memberId = sessionManager.currentMemberId
        if (memberId > 0) {
            loadDashboardData(memberId)
        } else {
            // Fallback ke 1 kalau belum login (dev mode)
            loadDashboardData(1)
        }
    }

    fun loadDashboardData(memberId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _error.value = null
            try {
                _member.value = memberRepository.getById(memberId).firstOrNull()
                val savings = savingsRepository.getActiveByMember(memberId).firstOrNull() ?: emptyList()
                _totalSaldo.value = savings.sumOf { it.saldo }
                _totalPoin.value = pointRepository.getTotalPoinByMember(memberId).firstOrNull() ?: 0
                _recentTransactions.value = transactionRepository.getByMemberPaged(memberId, 5, 0).firstOrNull() ?: emptyList()
                _latestSHU.value = shuRepository.getByMemberAndYear(memberId, java.time.Year.now().value).firstOrNull()

                // Kirim pengingat setoran kalau belum ada transaksi bulan ini
                val calendar = java.util.Calendar.getInstance()
                calendar.set(java.util.Calendar.DAY_OF_MONTH, 1)
                calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
                calendar.set(java.util.Calendar.MINUTE, 0)
                calendar.set(java.util.Calendar.SECOND, 0)
                val startOfMonth = calendar.timeInMillis
                val hasSetoranBulanIni = _recentTransactions.value.any {
                    it.tipe == Transaction.TransactionType.SETORAN_TABUNGAN && it.tgl >= startOfMonth
                }
                if (!hasSetoranBulanIni && _recentTransactions.value.isNotEmpty()) {
                    NotificationHelper.showSetoranReminder(application)
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Gagal memuat data"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout(sessionManager: SessionManager) {
        sessionManager.clearSession()
    }

    fun formatRupiah(amount: Long): String = com.example.kopkarrsui.util.formatRupiah(amount)
    fun formatPoin(poin: Int): String = NumberFormat.getInstance(Locale("id", "ID")).format(poin)
}
