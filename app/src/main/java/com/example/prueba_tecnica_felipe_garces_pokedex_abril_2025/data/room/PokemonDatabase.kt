package com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.room.dao.PokemonRoomDao
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.room.dao.SavedPokemonDao
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.room.entities.PokemonFavEntity
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.room.entities.SavedPokemonEntity

@Database(
    entities = [PokemonFavEntity::class, SavedPokemonEntity::class],
    version = 1,
    exportSchema = false
)

abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonRoomDao
    abstract fun savedPokemonDao(): SavedPokemonDao

}
