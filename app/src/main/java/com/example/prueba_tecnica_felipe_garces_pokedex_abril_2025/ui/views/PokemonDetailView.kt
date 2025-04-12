package com.example.pokemon_prueba_tecnica.ui.views

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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



import com.example.pokemon_prueba_tecnica.ui.viewmodels.PokemonDetailViewModel
import com.example.pokemon_prueba_tecnica_felipe_garces_abril.data.state.PokemonFavoriteState
import com.example.prueba_tecnica_felipe_garces_pokedex_abril_2025.R

@Composable

fun PokemonDetailView(pokemonDetailViewModel: PokemonDetailViewModel, navController: NavController, id: String) {
    val state by pokemonDetailViewModel.pokemonState.collectAsState()
    val isFavorite by pokemonDetailViewModel.isFavorite.collectAsState()
    val flavorText = state.speciesDetail?.flavor_text_entries
        ?.firstOrNull { it.language.name == "es" }?.flavor_text ?: "Sin descripción"




    val context = LocalContext.current
    LaunchedEffect(id) {
        pokemonDetailViewModel.getPokemonDetail(id)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.poke_fondo_2),
            contentDescription = "Fondo de Pokemon",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)) {
            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                state.errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.errorMessage ?: "Error desconocido", fontSize = 18.sp)
                    }
                }

                state.pokemonDetail != null -> {
                    val detail = state.pokemonDetail!!
                    val spriteUrls = listOfNotNull(
                        detail.sprites.front_default,
                        detail.sprites.back_default,
                        detail.sprites.front_shiny,
                        detail.sprites.back_shiny
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {

                        Row(
                            modifier = Modifier.padding(top = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            detail.types.forEach { tipo ->
                                val color = getPokemonTypeColor(tipo.type.name)

                                Box(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .background(color, shape = RoundedCornerShape(8.dp))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = tipo.type.name.capitalize(),
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        Text(
                            text = detail.name.capitalize(),
                            style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        )

                        if (spriteUrls.isNotEmpty()) {
                            SpriteCarrusel(sprites = spriteUrls)
                        } else {
                            Text(text = "No hay sprites disponibles", fontSize = 16.sp)
                        }

                        Spacer(Modifier.height(10.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = flavorText,
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center
                                    , color = Color.White
                                ),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }


                    }


                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {

                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.align(Alignment.TopStart)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.White,
                                modifier = Modifier.size(45.dp)
                            )
                        }
                        Row {

                            IconButton(
                                onClick = {
                                    pokemonDetailViewModel.toggleFavorite(
                                        PokemonFavoriteState(
                                            pokemonId = id,
                                            name = detail.name,
                                            spriteUrl = detail.sprites.front_default!!.toString()

                                        )
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorito",
                                    tint = if (isFavorite) Color.Red else Color.White,
                                    modifier = Modifier.size(80.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))


                            IconButton(
                                onClick = {
                                    sharePokemon(context, detail.sprites.front_default!!)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Compartir",
                                    tint = Color.White,
                                    modifier = Modifier.size(45.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }


}


private fun sharePokemon(context: Context, pokemonName: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Mira este Pokémon: $pokemonName")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Compartir Pokemon")
    context.startActivity(shareIntent)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpriteCarrusel(sprites: List<String>) {
    val pagerState = rememberPagerState(pageCount = { 1000 })

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(

            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            val index = page % sprites.size
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberImagePainter(sprites[index]),
                    contentDescription = "Sprite ${index + 1}",
                    modifier = Modifier.size(150.dp)

                )
            }
        }


        Text(
            text = "Sprite ${pagerState.currentPage  % sprites.size +1 } de ${sprites.size}",
            fontSize = 20.sp,

            color = Color.White,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}


fun getPokemonTypeColor(type: String): Color {
    return when (type.lowercase()) {
        "normal" -> Color(0xFFA8A77A)
        "fighting" -> Color(0xFFC22E28)
        "flying" -> Color(0xFFA98FF3)
        "poison" -> Color(0xFFA33EA1)
        "ground" -> Color(0xFFE2BF65)
        "rock" -> Color(0xFFB6A136)
        "bug" -> Color(0xFFA6B91A)
        "ghost" -> Color(0xFF735797)
        "steel" -> Color(0xFFB7B7CE)
        "fire" -> Color(0xFFEE8130)
        "water" -> Color(0xFF6390F0)
        "grass" -> Color(0xFF7AC74C)
        "electric" -> Color(0xFFF7D02C)
        "psychic" -> Color(0xFFF95587)
        "ice" -> Color(0xFF96D9D6)
        "dragon" -> Color(0xFF6F35FC)
        "dark" -> Color(0xFF705746)
        "fairy" -> Color(0xFFD685AD)
        else -> Color.Gray
    }
}
