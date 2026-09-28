package com.example.kopkarrsui.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kopkarrsui.data.local.SessionManager
import com.example.kopkarrsui.data.local.entity.FinancialStatement
import com.example.kopkarrsui.data.local.entity.SHUAllocation
import com.example.kopkarrsui.domain.repository.FinancialStatementRepository
import com.example.kopkarrsui.domain.repository.SHURepository
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
class SHUViewModel @Inject constructor(
    private val shuRepository: SHURepository,
    private val financialStatementRepository: FinancialStatementRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _mySHU = MutableStateFlow<SHUAllocation?>(null)
    val mySHU: StateFlow<SHUAllocation?> = _mySHU.asStateFlow()

    private val _financialStatement = MutableStateFlow<FinancialStatement?>(null)
    val financialStatement: StateFlow<FinancialStatement?> = _financialStatement.asStateFlow()

    private val _shuHistory = MutableStateFlow<List<SHUAllocation>>(emptyList())
    val shuHistory: StateFlow<List<SHUAllocation>> = _shuHistory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        val memberId = sessionManager.currentMemberId.takeIf { it > 0 } ?: 1L
        loadSHU(memberId)
    }

    fun loadSHU(memberId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _error.value = null
            try {
                val currentYear = java.time.Year.now().value
                _mySHU.value = shuRepository.getByMemberAndYear(memberId, currentYear).firstOrNull()
                _shuHistory.value = shuRepository.getByMember(memberId).firstOrNull() ?: emptyList()
                _financialStatement.value = financialStatementRepository.getByYear(currentYear).firstOrNull()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun formatRupiah(amount: Long): String = "Rp ${NumberFormat.getInstance(Locale("id", "ID")).format(amount)}"
    fun getSHUStatusLabel(status: SHUAllocation.SHUStatus): String = status.label
    fun getFinancialStatusLabel(status: FinancialStatement.FinancialStatus): String = status.label
}
