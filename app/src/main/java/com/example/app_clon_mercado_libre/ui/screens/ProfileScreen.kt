package com.example.app_clon_mercado_libre.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_clon_mercado_libre.R
import com.example.app_clon_mercado_libre.data.ProductRepository
import com.example.app_clon_mercado_libre.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToPurchases: () -> Unit,
    onNavigateToCategories: () -> Unit
) {
    var userName by remember { mutableStateOf("Cargando...") }
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser

    val favoriteProducts = ProductRepository.products.filter { 
        ProductRepository.favorites.contains(it.id) 
    }
    val purchases = ProductRepository.purchases

    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val nombre = document.getString("nombre") ?: "Usuario"
                        val apellido = document.getString("apellido") ?: ""
                        userName = "$nombre $apellido"
                    }
                }
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MLYellow)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        modifier = Modifier.weight(1f).height(40.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = MLGray, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Search in the store...", color = MLGray, fontSize = 14.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = MLBlack)
                }
            }
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
                    icon = { Icon(Icons.Outlined.FavoriteBorder, null) },
                    label = { Text("Favorites", fontSize = 10.sp) },
                    selected = false,
                    onClick = onNavigateToFavorites
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.ShoppingBag, null) },
                    label = { Text("Purchases", fontSize = 10.sp) },
                    selected = false,
                    onClick = onNavigateToPurchases
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Notifications, null) },
                    label = { Text("Notifications", fontSize = 10.sp) },
                    selected = false,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Menu, null, tint = MLBlue) },
                    label = { Text("More", color = MLBlue, fontSize = 10.sp) },
                    selected = true,
                    onClick = { }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MLBackground)
                .verticalScroll(rememberScrollState())
        ) {
            // User Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile Image (Placeholder)
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color.LightGray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(40.dp), tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = userName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Nivel 4 - Mercado Puntos", fontSize = 14.sp, color = MLGray)
                    }
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = MLGray)
                }
            }

            Spacer(modifier = Modifier.height(1.dp))

            // My Purchases Section
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.ShoppingBag, contentDescription = null, tint = MLBlue, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Mis compras", fontSize = 16.sp)
                        }
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = MLGray)
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    if (purchases.isEmpty()) {
                        Text("No hay compras.", color = MLGray, fontSize = 14.sp)
                    } else {
                        // Show last purchase
                        val lastPurchase = purchases.last()
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = lastPurchase.imageRes),
                                contentDescription = null,
                                modifier = Modifier.size(50.dp).clip(RoundedCornerShape(4.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Llega mañana", color = Color(0xFF00A650), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text(lastPurchase.name, fontSize = 14.sp, maxLines = 1)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Favorites Section
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Favorite, contentDescription = null, tint = MLBlue, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Favoritos", fontSize = 16.sp)
                        }
                        Text(
                            "Ver todos", 
                            color = MLBlue, 
                            fontSize = 14.sp, 
                            modifier = Modifier.clickable { onNavigateToFavorites() }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    if (favoriteProducts.isEmpty()) {
                        Text("No hay favoritos seleccionados.", color = MLGray, fontSize = 14.sp)
                    } else {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(favoriteProducts) { product ->
                                Column(modifier = Modifier.width(100.dp)) {
                                    Box {
                                        Image(
                                            painter = painterResource(id = product.imageRes),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(100.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                        Icon(
                                            Icons.Filled.Favorite, 
                                            null, 
                                            tint = MLBlue, 
                                            modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(product.currentPrice, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sell Button
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MLBlue)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Storefront, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Vender un producto", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Menu List
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            ) {
                Column {
                    MenuItem(Icons.Outlined.Person, "Mi cuenta")
                    MenuItem(Icons.Outlined.Shield, "Seguridad")
                    MenuItem(Icons.Outlined.CreditCard, "Tarjetas")
                    MenuItem(Icons.Outlined.GridView, "Categorías", onClick =  onNavigateToCategories)
                    MenuItem(Icons.Outlined.LocationOn, "Direcciones")
                    MenuItem(Icons.Outlined.Checkroom, "Moda")
                    MenuItem(Icons.Outlined.Settings, "Configuración")
                    MenuItem(Icons.Outlined.MenuBook, "Libro de reclamaciones")
                    MenuItem(Icons.Outlined.HelpOutline, "Ayuda")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                "Términos y condiciones", 
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 32.dp),
                color = MLGray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun MenuItem(icon: ImageVector, title: String,onClick: () -> Unit = {}) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {onClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MLBlue, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, modifier = Modifier.weight(1f), fontSize = 16.sp, color = MLBlack)
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
        }
        HorizontalDivider(modifier = Modifier.padding(start = 56.dp), thickness = 0.5.dp, color = Color.LightGray)
    }
}
