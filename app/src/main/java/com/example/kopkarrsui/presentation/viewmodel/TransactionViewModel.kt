package com.example.kopkarrsui.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kopkarrsui.data.local.entity.Transaction
import com.example.kopkarrsui.domain.repository.PointRepository
import com.example.kopkarrsui.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val pointRepository: PointRepository
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _totalPoin = MutableStateFlow(0)
    val totalPoin: StateFlow<Int> = _totalPoin.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var currentPage = 0
    private var hasMore = true

    init {
        loadTransactions(1) // TODO: current member ID
    }

    fun loadTransactions(memberId: Long, refresh: Boolean = false) {
        if (refresh) { currentPage = 0; hasMore = true; _transactions.value = emptyList() }
        if (!hasMore && !refresh) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val newTx = transactionRepository.getByMemberPaged(memberId, 20, currentPage * 20).firstOrNull() ?: emptyList()
                if (newTx.size < 20) hasMore = false
                _transactions.value = if (refresh) newTx else _transactions.value + newTx
                currentPage++
                _totalPoin.value = pointRepository.getTotalPoinByMember(memberId).firstOrNull() ?: 0
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMore(memberId: Long) {
        if (!hasMore || _isLoading.value) return
        loadTransactions(memberId, refresh = false)
    }

    fun formatRupiah(amount: Long): String = "Rp ${NumberFormat.getInstance(Locale("id", "ID")).format(amount)}"
    fun formatPoin(poin: Int): String = NumberFormat.getInstance(Locale("id", "ID")).format(poin)
}
