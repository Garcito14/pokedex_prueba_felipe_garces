package com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.models

data class PokemonDetailModel(
    val name: String,
    val sprites: Sprites,
    val pokemonSpeciesModel: PokemonSpeciesModel? = null,
    val types: List<PokemonTypeWrapper>,
)


data class Sprites(
    val front_default: String?,
    val back_default: String?,
    val front_shiny: String?,
    val back_shiny: String?
)

data class PokemonSpeciesModel(
    val flavor_text_entries: List<FlavorTextEntry>
)

data class FlavorTextEntry(
    val flavor_text: String,
    val language: Language
)

data class Language(
    val name: String
)



data class PokemonTypeWrapper(
    val type: PokemonType
)

data class PokemonType(
    val name: String
)
