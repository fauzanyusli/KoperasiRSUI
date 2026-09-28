package com.example.kopkarrsui.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.app.Application
import com.example.kopkarrsui.data.local.SessionManager
import com.example.kopkarrsui.domain.repository.MemberRepository
import com.example.kopkarrsui.util.NotificationHelper
import com.example.kopkarrsui.util.PasswordUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val sessionManager: SessionManager,
    private val application: Application
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(noAnggota: String, pin: String) {
        if (noAnggota.isBlank() || pin.isBlank()) {
            _loginState.value = LoginState.Error("Nomor anggota dan PIN harus diisi")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val member = memberRepository.getByNoAnggota(noAnggota).firstOrNull()
                if (member == null) {
                    _loginState.value = LoginState.Error("Nomor anggota tidak ditemukan")
                    return@launch
                }

                if (member.status.value != "aktif") {
                    _loginState.value = LoginState.Error("Akun tidak aktif")
                    return@launch
                }

                // PIN validation — SHA-256 hash compare
                if (!PasswordUtils.verifyPin(pin, member.pinHash.orEmpty())) {
                    _loginState.value = LoginState.Error("PIN salah")
                    return@launch
                }

                sessionManager.saveSession(member.id)
                NotificationHelper.showWelcomeNotification(application, member.nama)
                _loginState.value = LoginState.Success(member.id)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Gagal login")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }

    sealed class LoginState {
        data object Idle : LoginState()
        data object Loading : LoginState()
        data class Success(val memberId: Long) : LoginState()
        data class Error(val message: String) : LoginState()
    }
}
