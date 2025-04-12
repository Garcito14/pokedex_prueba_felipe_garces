package com.example.pokemon_prueba_tecnica.ui.views

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter


import com.example.pokemon_prueba_tecnica.ui.utils.CButton
import com.example.pokemon_prueba_tecnica.ui.utils.isNetworkAvailable

import com.example.pokemon_prueba_tecnica.ui.viewmodels.PokemonListViewModel
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.models.PokemonResults
import com.example.prueba_tecnica_felipe_garces_pokedex_abril_2025.R
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(pokemonViewModel: PokemonListViewModel, navController: NavController) {
    val pokemonList by pokemonViewModel.pokemonList.collectAsState()
    val searchResults by pokemonViewModel.searchResults.collectAsState()
    val nextPage by pokemonViewModel.nextPage.collectAsState()
    val previousPage by pokemonViewModel.previousPage.collectAsState()
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        if (!isNetworkAvailable(context)) {
            Toast.makeText(context, "No hay conexión a Internet, puede ver sus busquedas guardadas", Toast.LENGTH_SHORT).show()
        }
        pokemonViewModel.getPokemonList()
    }



    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.pokedex_fondo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(vertical = 50.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {


                IconButton(onClick = { isSearchVisible = !isSearchVisible }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color(0xFFf6ff3f)
                        , modifier = Modifier.size(40.dp)
                    )
                }
                IconButton(onClick = { pokemonViewModel.getPokemonList() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refrescar lista",
                        tint = Color(0xFFf6ff3f),
                        modifier = Modifier.size(40.dp)
                    )
                }

                if (isSearchVisible) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            pokemonViewModel.searchPokemon(it)
                        },
                        label = {
                            Text(
                                "Buscar Pokémon",
                                style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.DarkGray),
                        singleLine = true,
                        textStyle = TextStyle(color = Color.White),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Yellow,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White
                        ),

                        trailingIcon = {
                            IconButton(onClick = { isSearchVisible = false }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color.White
                                )
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            val displayList = if (searchQuery.isEmpty()) pokemonList else searchResults ?: emptyList()

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f)
            ) {
                items(displayList) { pokemon ->
                    PokemonItem(pokemon, navController,pokemonViewModel)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (searchQuery.isEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CButton(
                        text = "Page -",
                        onClick = { pokemonViewModel.previousPage() },
                        enabled = previousPage != null
                    )

                    CButton(
                        text = "Page +",
                        onClick = { pokemonViewModel.nextPage() },
                        enabled = nextPage != null
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Spacer(Modifier.width(10.dp))
                    CButton(
                        text = "Pokemon Guardados",
                        onClick = { navController.navigate("saved_pokemon") },
                        enabled = true
                    )
                    Spacer(Modifier.width(10.dp))
                    CButton(
                        text = "Pokemon Favoritos",
                        onClick = { navController.navigate("pokemon_favorites") },
                        enabled = true
                    )

                }

            }
        }
    }
}

@Composable
fun PokemonItem(pokemon: PokemonResults, navController: NavController, pokemonViewModel: PokemonListViewModel) {
    val pokemonId = pokemon.url.split("/").dropLast(1).last()
    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {

                navController.navigate("pokemon_detail/$pokemonId")
                pokemonViewModel.savePokemon(pokemon)
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .padding(4.dp)
                    .size(100.dp)
            )
            Text(
                text = pokemon.name.capitalize(),
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }


}


@Composable
fun SavedPokemonView(viewModel: PokemonListViewModel, navController: NavController) {
    val savedPokemon by viewModel.savedPokemon.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getSavedPokemon()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.pokebola_fondo),
            contentDescription = "Fondo de Pokebola",
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {},
            contentScale = ContentScale.Crop
        )


        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(top = 20.dp, start = 16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver",
                tint = Color.White,
                modifier = Modifier.size(45.dp)
            )
        }


        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(top = 80.dp)
        ) {
            items(savedPokemon) { pokemon ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                    ,
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = pokemon.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}
