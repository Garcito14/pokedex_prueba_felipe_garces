package com.example.pokedex_pokeapi_prueba_tecnica_felipe_garces_2025.data.state

data class AuthState(
    val isAuthenticated: Boolean = false,
    val userEmail: String? = null,
    val displayName: String? = null,
    val error: String? = null
)
