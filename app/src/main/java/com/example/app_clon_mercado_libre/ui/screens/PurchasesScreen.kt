package com.example.app_clon_mercado_libre.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_clon_mercado_libre.data.ProductRepository
import com.example.app_clon_mercado_libre.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchasesScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToPurchases: () -> Unit,
    onNavigateToCategories: () -> Unit = {}
) {
    val purchases = ProductRepository.purchases

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis compras", fontWeight = FontWeight.Bold, color = Color.Black)  },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MLYellow)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Home", fontSize = 10.sp) },
                    selected = false,
                    onClick = onNavigateToHome
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.GridView, null) },
                    label = { Text("Categorías", fontSize = 10.sp) },
                    selected = false,
                    onClick = onNavigateToCategories
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.ShoppingCart, null, tint = MLBlue) },
                    label = { Text("Carrito", color = MLBlue, fontSize = 10.sp) },
                    selected = true,
                    onClick = onNavigateToPurchases
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.PlayCircle, null) },
                    label = { Text("Videos", fontSize = 10.sp) },
                    selected = false,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Menu, null) },
                    label = { Text("More", fontSize = 10.sp) },
                    selected = false,
                    onClick = onNavigateToProfile
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MLBackground)
        ) {
            if (purchases.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No has realizado compras aún.", color = MLGray)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
                    items(purchases) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                                .clickable { onNavigateToDetail(product.id) },
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = product.imageRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(60.dp).clip(RoundedCornerShape(4.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text("Entregado", color = Color(0xFF00A650), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text(product.name, fontSize = 14.sp, maxLines = 1)
                                    Text(product.currentPrice, fontSize = 12.sp, color = MLGray)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
