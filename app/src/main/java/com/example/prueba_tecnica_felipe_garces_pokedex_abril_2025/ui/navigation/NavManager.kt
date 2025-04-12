package com.example.pokemon_prueba_tecnica.ui.navigation



import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


import com.example.pokemon_prueba_tecnica.ui.viewmodels.FavoritePokemonViewModel
import com.example.pokemon_prueba_tecnica.ui.viewmodels.PokemonDetailViewModel
import com.example.pokemon_prueba_tecnica.ui.viewmodels.PokemonListViewModel
import com.example.pokemon_prueba_tecnica.ui.views.PokemonDetailView
import com.example.pokemon_prueba_tecnica.ui.views.PokemonFavoritesScreen
import com.example.pokemon_prueba_tecnica.ui.views.PokemonListScreen
import com.example.pokemon_prueba_tecnica.ui.views.SavedPokemonView
import com.example.prueba_tecnica_felipe_garces_pokedex_2025_abril.AuthViewModel
import com.example.prueba_tecnica_felipe_garces_pokedex_2025_abril.ui.auth.LoginScreen

@Composable
fun NavManager(
    navController: NavController,
    pokemonViewModel: PokemonListViewModel,
    pokemonDetailViewModel: PokemonDetailViewModel,
    favoritePokemonViewModel:FavoritePokemonViewModel,
    authViewModel: AuthViewModel

    ) {

    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = "login") {


        composable("pokemon_home") {
            PokemonListScreen(pokemonViewModel, navController)
        }


        composable(
            route = "pokemon_detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            id?.let {
                PokemonDetailView(
                    navController = navController,
                    pokemonDetailViewModel = pokemonDetailViewModel,
                    id = it
                )
            }
        }

        composable("pokemon_favorites") {
            PokemonFavoritesScreen(pokemonViewModel,favoritePokemonViewModel, navController)
        }

        composable("saved_pokemon") {
            SavedPokemonView(pokemonViewModel,navController)
        }

        composable("login") {
            LoginScreen(navController,authViewModel)
        }


    }
}
