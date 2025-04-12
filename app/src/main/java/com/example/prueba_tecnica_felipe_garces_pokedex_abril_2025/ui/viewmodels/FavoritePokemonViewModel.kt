package com.example.pokemon_prueba_tecnica.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.repository.PokemonDetailRepository
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.repository.PokemonFavRepository
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.room.entities.PokemonFavEntity

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritePokemonViewModel @Inject constructor(
    private val repository: PokemonDetailRepository,
    private val pokemonfavRepository: PokemonFavRepository
) : ViewModel() {

    private val _favoritePokemonList = MutableStateFlow<List<PokemonFavEntity>>(emptyList())
    val favoritePokemonList: StateFlow<List<PokemonFavEntity>> = _favoritePokemonList

    init {
        loadFavoritePokemon()
    }

    fun loadFavoritePokemon() {
        viewModelScope.launch {
            pokemonfavRepository.getAllFavorites().collect { favoriteList ->
                _favoritePokemonList.value = favoriteList
            }
        }
    }




}
