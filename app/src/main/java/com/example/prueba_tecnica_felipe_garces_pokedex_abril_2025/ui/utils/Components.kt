package com.example.pokemon_prueba_tecnica.ui.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CButton(text: String, onClick: () -> Unit, enabled: Boolean) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.DarkGray,
            contentColor = Color.White
        ),
        border = BorderStroke(2.dp, Color(0xFFf6ff3f)),
        shape = RoundedCornerShape(15.dp)
    ) {
        Text(text)
    }
}