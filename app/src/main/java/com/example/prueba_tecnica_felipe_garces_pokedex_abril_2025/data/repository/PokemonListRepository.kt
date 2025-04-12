package com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.repository

import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.API.ApiService
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.models.PokemonModel
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.models.PokemonResults
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.room.dao.SavedPokemonDao
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.room.entities.SavedPokemonEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokemonListRepository @Inject constructor(
    private val apiService: ApiService,
    private val savedPokemonDao: SavedPokemonDao
) {
    suspend fun getPokemonList(limit: Int, offset: Int): Result<PokemonModel> {
        return try {
            val response = apiService.getPokemonList(limit, offset)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("No hay datos"))
            } else {
                Result.failure(Exception("Error : ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchPokemon(query: String): Result<PokemonModel> {
        return try {
            val response = apiService.getPokemonList(1000, 0)

            if (response.isSuccessful) {
                response.body()?.let { pokemonModel ->
                    val filteredResults = pokemonModel.results.filter {
                        it.name.contains(query, ignoreCase = true)
                    }
                    Result.success(PokemonModel(pokemonModel.count, "", "", filteredResults))
                } ?: Result.failure(Exception("Respuesta vacía"))
            } else {
                Result.failure(Exception("Error en la API: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun savePokemon(pokemon: PokemonResults) {
        savedPokemonDao.insertSavedPokemon(SavedPokemonEntity(pokemon.name,pokemon.name))
    }

    fun getSavedPokemon(): Flow<List<SavedPokemonEntity>> {
        return savedPokemonDao.getSavedPokemon()
    }
}