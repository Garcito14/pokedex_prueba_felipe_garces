package com.example.pokemon_prueba_tecnica.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.models.PokemonResults
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.repository.PokemonListRepository
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.room.entities.SavedPokemonEntity

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonListRepository
) : ViewModel() {

    private val _pokemonList = MutableStateFlow<List<PokemonResults>>(emptyList())
    val pokemonList: StateFlow<List<PokemonResults>> get() = _pokemonList

    private val _nextPage = MutableStateFlow<String?>(null)
    val nextPage: StateFlow<String?> get() = _nextPage

    private val _previousPage = MutableStateFlow<String?>(null)
    val previousPage: StateFlow<String?> get() = _previousPage


    private val _searchResults = MutableStateFlow<List<PokemonResults>?>(null)
    val searchResults: StateFlow<List<PokemonResults>?> = _searchResults

    private val _savedPokemon = MutableStateFlow<List<SavedPokemonEntity>>(emptyList())
    val savedPokemon: StateFlow<List<SavedPokemonEntity>> = _savedPokemon

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var offset = 0

    init {
        getPokemonList()
        getSavedPokemon()
    }
    fun savePokemon(pokemon: PokemonResults) {
        viewModelScope.launch {
            repository.savePokemon(pokemon)
        }
    }

    fun getSavedPokemon() {
        viewModelScope.launch {
            repository.getSavedPokemon().collect { savedList ->
                _savedPokemon.value = savedList
            }
        }
    }



    fun getPokemonList() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getPokemonList(20, offset).onSuccess {
                _pokemonList.value = it.results
                _nextPage.value = it.next
                _previousPage.value = it.previous
            }
            _isLoading.value = false
        }
    }
    fun searchPokemon(query: String) {
        if (query.isEmpty()) {
            _searchResults.value = null
            getPokemonList()
        } else {
            viewModelScope.launch {
                repository.searchPokemon(query).onSuccess { response ->
                    _searchResults.value = response.results
                }
            }
        }
    }
    fun nextPage() {
        if (_nextPage.value != null) {
            offset += 20
            getPokemonList()
        }
    }

    fun previousPage() {
        if (_previousPage.value != null && offset >= 20) {
            offset -= 20
            getPokemonList()
        }
    }
}