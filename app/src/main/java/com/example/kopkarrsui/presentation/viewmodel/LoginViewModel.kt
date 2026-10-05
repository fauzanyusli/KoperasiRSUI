package com.example.kopkarrsui.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.app.Application
import com.example.kopkarrsui.data.local.SessionManager
import com.example.kopkarrsui.data.local.dao.AdminDao
import com.example.kopkarrsui.domain.repository.MemberRepository
import com.example.kopkarrsui.util.MemberAuth
import com.example.kopkarrsui.util.NotificationHelper
import com.example.kopkarrsui.util.PasswordUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val adminDao: AdminDao,
    private val sessionManager: SessionManager,
    private val firebaseAuth: FirebaseAuth,
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

                // Firebase Auth: email = mapping no. anggota, password = PIN
                firebaseSignIn(member.noAnggota, pin)

                sessionManager.saveSession(member.id)
                NotificationHelper.showWelcomeNotification(application, member.nama)

                // Role routing: member with active admin record (pengurus/admin) -> AdminDashboard
                val isAdmin = adminDao.getActiveByMemberId(member.id).firstOrNull() != null
                _loginState.value = LoginState.Success(member.id, isAdmin)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Gagal login")
            }
        }
    }

    /**
     * Jembatan ke Firebase Auth (best effort):
     * - akun belum ada → createUser (PIN = password, anggota self-provision saat login pertama)
     * - password Auth lama beda dengan PIN (hash lokal cocok) → fallback anonymous
     * - offline → lanjut, Firestore tetap jalan dari cache
     */
    private suspend fun firebaseSignIn(noAnggota: String, pin: String) {
        val email = MemberAuth.emailFor(noAnggota)
        try {
            try {
                firebaseAuth.signInWithEmailAndPassword(email, pin).await()
            } catch (_: FirebaseAuthInvalidUserException) {
                firebaseAuth.createUserWithEmailAndPassword(email, pin).await()
            }
        } catch (_: FirebaseAuthInvalidCredentialsException) {
            // wrong-password / weak-password (PIN < 6 digit) — hash lokal sudah valid, tetap masuk
            runCatching { firebaseAuth.signInAnonymously().await() }
        } catch (_: Exception) {
            // jaringan bermasalah — session lokal tetap jalan
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }

    sealed class LoginState {
        data object Idle : LoginState()
        data object Loading : LoginState()
        data class Success(val memberId: Long, val isAdmin: Boolean = false) : LoginState()
        data class Error(val message: String) : LoginState()
    }
}
