package com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.state


import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.models.PokemonDetailModel
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.models.PokemonSpeciesModel

data class PokemonDetailState(
    val isLoading: Boolean = true,
    val pokemonDetail: PokemonDetailModel? = null,
    val speciesDetail: PokemonSpeciesModel? = null,
    val errorMessage: String? = null
)
