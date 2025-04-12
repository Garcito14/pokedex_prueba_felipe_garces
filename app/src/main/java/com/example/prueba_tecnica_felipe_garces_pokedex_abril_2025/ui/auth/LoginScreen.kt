package com.example.prueba_tecnica_felipe_garces_pokedex_2025_abril.ui.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.prueba_tecnica_felipe_garces_pokedex_2025_abril.AuthViewModel
import com.example.prueba_tecnica_felipe_garces_pokedex_abril_2025.R

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
            navController.navigate("pokemon_home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.poke_logi),
            contentDescription = "Fondo Pokedex",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Inicia sesión con Google", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(14.dp))

            Button(onClick = { viewModel.loginWithGoogle() }) {
                Image(
                    painter = painterResource(id = R.drawable.google_foto),
                    contentDescription = "Google",
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp)
                        .padding(5.dp),
                    contentScale = ContentScale.Fit
                )
                Text("Login con Google", fontSize = 25.sp)
            }

            authState.error?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Error: $it", color = Color.Red)
            }
        }
    }
}
