package com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.room.entities.PokemonFavEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonRoomDao {

    @Query("SELECT * FROM pokemon_favs")
    fun getAllPokemonFav(): Flow<List<PokemonFavEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM pokemon_favs WHERE pokemonId = :pokemonId)")
    fun isFavorite(pokemonId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFav(pokemon: PokemonFavEntity)

    @Query("DELETE FROM pokemon_favs WHERE pokemonId = :pokemonId")
    suspend fun removeFav(pokemonId: String)
}
