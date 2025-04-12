package com.example.prueba_tecnica_felipe_garces_pokedex_2025_abril

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex_pokeapi_prueba_tecnica_felipe_garces_2025.data.state.AuthState
import com.example.prueba_tecnica_felipe_garces_pokedex_2025_abril.ui.auth.AuthResponse
import com.example.prueba_tecnica_felipe_garces_pokedex_2025_abril.ui.auth.AuthenticationManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authManager: AuthenticationManager
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState

    fun loginWithGoogle() {
        viewModelScope.launch {
            authManager.signInWithGoogle().collect { response ->
                when (response) {
                    is AuthResponse.Success -> {
                        val currentUser = Firebase.auth.currentUser
                        _authState.value = AuthState(
                            isAuthenticated = true,
                            userEmail = currentUser?.email,
                            displayName = currentUser?.displayName,
                            error = null
                        )
                    }

                    is AuthResponse.Error -> {
                        _authState.value = AuthState(
                            isAuthenticated = false,
                            error = response.message
                        )
                    }
                }
            }
        }
    }
}
