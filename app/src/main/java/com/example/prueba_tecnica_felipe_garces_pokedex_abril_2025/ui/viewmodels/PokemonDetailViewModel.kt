package com.example.pokemon_prueba_tecnica.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.repository.PokemonDetailRepository
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.repository.PokemonFavRepository
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.room.entities.PokemonFavEntity
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.state.PokemonDetailState
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.state.PokemonFavoriteState

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonDetailRepository,
    private val pokemonfavRepository: PokemonFavRepository
) : ViewModel() {



    private val _pokemonState = MutableStateFlow(PokemonDetailState())
    val pokemonState: StateFlow<PokemonDetailState> = _pokemonState

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite



    fun getPokemonDetail(pokemonId: String) {
        viewModelScope.launch {
            _pokemonState.value = PokemonDetailState(isLoading = true)

            val detailResult = repository.getPokemonDetail(pokemonId)
            val speciesResult = repository.getPokemonSpecies(pokemonId)

            if (detailResult.isSuccess && speciesResult.isSuccess) {
                _pokemonState.value = PokemonDetailState(
                    isLoading = false,
                    pokemonDetail = detailResult.getOrNull(),
                    speciesDetail = speciesResult.getOrNull()
                )
                checkIfFavorite(pokemonId)
            } else {
                _pokemonState.value = PokemonDetailState(
                    isLoading = false,
                    errorMessage = "Error al cargar detalles"
                )
            }
        }
    }

    private fun checkIfFavorite(pokemonId: String) {
        viewModelScope.launch {
            pokemonfavRepository.isPokemonFavorite(pokemonId)
                .distinctUntilChanged()
                .collect { isFav: Boolean ->
                    _isFavorite.value = isFav
                }
        }
    }


    fun toggleFavorite(pokemon: PokemonFavoriteState) {
        viewModelScope.launch {
            if (_isFavorite.value) {
                pokemonfavRepository.removeFromFavorites(pokemon.pokemonId)
            } else {
                pokemonfavRepository.addToFavorites(PokemonFavEntity(pokemonId = pokemon.pokemonId, name = pokemon.name))
            }
            _isFavorite.value = !_isFavorite.value
        }
    }
}
