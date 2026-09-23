

package com.example.app_clon_mercado_libre.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.app_clon_mercado_libre.ui.theme.MLBackground
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categorías ") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MLBackground),
            contentAlignment = Alignment.Center
        ) {
            Text("Categorías ", fontSize = 20.sp, color = Color.Gray)
            // 2. El botón con su evento de clic y su texto interno
         /*   Button(
               onClick = { /* Tu acción aquí */

                },

               modifier = Modifier.padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)), //Color.Blue
            ) {
                Text("Agregar Categorías")
           }

          */
        }


    }
}

