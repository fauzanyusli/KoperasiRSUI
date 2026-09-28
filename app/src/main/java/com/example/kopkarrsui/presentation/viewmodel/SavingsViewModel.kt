package com.example.kopkarrsui.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kopkarrsui.data.local.SessionManager
import com.example.kopkarrsui.data.local.entity.SavingsAccount
import com.example.kopkarrsui.domain.repository.SavingsRepository
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
class SavingsViewModel @Inject constructor(
    private val savingsRepository: SavingsRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _accounts = MutableStateFlow<List<SavingsAccount>>(emptyList())
    val accounts: StateFlow<List<SavingsAccount>> = _accounts.asStateFlow()

    private val _totalSaldo = MutableStateFlow(0L)
    val totalSaldo: StateFlow<Long> = _totalSaldo.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        val memberId = sessionManager.currentMemberId.takeIf { it > 0 } ?: 1L
        loadAccounts(memberId)
    }

    fun loadAccounts(memberId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _error.value = null
            try {
                val accounts = savingsRepository.getActiveByMember(memberId).firstOrNull() ?: emptyList()
                _accounts.value = accounts
                _totalSaldo.value = accounts.sumOf { it.saldo }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun formatRupiah(amount: Long): String = "Rp ${NumberFormat.getInstance(Locale("id", "ID")).format(amount)}"
}
