package com.example.prueba_tecnica_felipe_garces_pokedex_abril_2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.pokemon_prueba_tecnica.ui.navigation.NavManager
import com.example.pokemon_prueba_tecnica.ui.viewmodels.FavoritePokemonViewModel
import com.example.pokemon_prueba_tecnica.ui.viewmodels.PokemonDetailViewModel
import com.example.pokemon_prueba_tecnica.ui.viewmodels.PokemonListViewModel
import com.example.prueba_tecnica_felipe_garces_pokedex_2025_abril.AuthViewModel
import com.example.prueba_tecnica_felipe_garces_pokedex_abril_2025.ui.theme.Prueba_tecnica_felipe_garces_pokedex_abril_2025Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pokemonListViewModel: PokemonListViewModel by viewModels()
        val pokemonDetailModel: PokemonDetailViewModel by viewModels()
        val favoritePokemonViewModel: FavoritePokemonViewModel by viewModels()
        val authViewModel: AuthViewModel by viewModels()
        enableEdgeToEdge()
        setContent {
            Prueba_tecnica_felipe_garces_pokedex_abril_2025Theme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavManager(navController = navController,pokemonListViewModel,pokemonDetailModel,favoritePokemonViewModel,authViewModel)

                }
            }
        }
    }
}

