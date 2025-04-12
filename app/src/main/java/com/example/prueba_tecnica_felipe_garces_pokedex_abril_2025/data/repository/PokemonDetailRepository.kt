package com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.repository


import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.API.ApiService
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.models.PokemonDetailModel
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.models.PokemonSpeciesModel

import javax.inject.Inject

class PokemonDetailRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getPokemonDetail(pokemonId: String): Result<PokemonDetailModel> {
        return try {
            val response = apiService.getPokemonDetail(pokemonId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("NO hay respuesta"))
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getPokemonSpecies(pokemonId: String): Result<PokemonSpeciesModel> {
        return try {
            val response = apiService.getPokemonSpecies(pokemonId)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("No hay nada"))
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}