package com.example.app_clon_mercado_libre.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.RemoveShoppingCart
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_clon_mercado_libre.data.CartItem
import com.example.app_clon_mercado_libre.data.ProductRepository
import com.example.app_clon_mercado_libre.data.formatPrice
import com.example.app_clon_mercado_libre.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToCheckout: () -> Unit
) {
    val context = LocalContext.current
    val cartItems by ProductRepository.cartItems.collectAsState()
    val cartLoading by ProductRepository.cartLoading.collectAsState()

    LaunchedEffect(Unit) {
        ProductRepository.loadCart()
    }

    val total = cartItems.sumOf { it.price * it.quantity }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi carrito", fontWeight = FontWeight.Bold, color = Color.Black) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MLYellow)
            )
        },
        bottomBar = {
            Column {
                Surface(shadowElevation = 8.dp, color = Color.White) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total (${cartItems.size} productos):", fontSize = 12.sp, color = MLGray)
                            Text(formatPrice(total), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MLBlue)
                        }
                        Button(
                            onClick = onNavigateToCheckout,
                            enabled = cartItems.isNotEmpty(),
                            modifier = Modifier.height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MLBlue)
                        ) {
                            Text("Continuar compra", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, null) },
                        label = { Text("Inicio", fontSize = 10.sp) },
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
                        onClick = { }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Outlined.PlayCircle, null) },
                        label = { Text("Videos", fontSize = 10.sp) },
                        selected = false,
                        onClick = { }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Menu, null) },
                        label = { Text("Más", fontSize = 10.sp) },
                        selected = false,
                        onClick = onNavigateToProfile
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MLBackground)
        ) {
            when {
                cartLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MLBlue)
                    }
                }
                cartItems.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Outlined.RemoveShoppingCart,
                                contentDescription = null,
                                tint = MLGray,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Tu carrito está vacío.", color = MLGray, fontSize = 16.sp)
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(cartItems, key = { it.productId }) { item ->
                            CartItemRow(
                                item = item,
                                onDetail = { onNavigateToDetail(item.productId) },
                                onIncrease = {
                                    if (item.quantity >= item.stock) {
                                        Toast.makeText(
                                            context,
                                            "No hay más stock (máx. ${item.stock})",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        ProductRepository.updateCartQuantity(item.productId, item.quantity + 1)
                                    }
                                },
                                onDecrease = {
                                    ProductRepository.updateCartQuantity(item.productId, item.quantity - 1)
                                },
                                onRemove = {
                                    ProductRepository.removeCartItem(item.productId)
                                    Toast.makeText(context, "Producto quitado del carrito", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartItem,
    onDetail: () -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            com.example.app_clon_mercado_libre.ui.components.ProductImageFromUrl(
                url = item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onDetail() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontSize = 14.sp, fontWeight = FontWeight.Medium, maxLines = 2)
                Text(formatPrice(item.price), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MLBlack)
                Text("Máx. ${item.stock} disponibles", fontSize = 11.sp, color = MLGray)
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, Color.LightGray),
                        modifier = Modifier.size(28.dp)
                    ) {
                        IconButton(onClick = onDecrease, modifier = Modifier.size(28.dp)) {
                            Icon(
                                Icons.Default.Remove,
                                contentDescription = "Menos",
                                modifier = Modifier.size(16.dp),
                                tint = MLBlue
                            )
                        }
                    }
                    Text(
                        item.quantity.toString(),
                        modifier = Modifier.padding(horizontal = 14.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, Color.LightGray),
                        modifier = Modifier.size(28.dp)
                    ) {
                        IconButton(onClick = onIncrease, enabled = item.quantity < item.stock, modifier = Modifier.size(28.dp)) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Más",
                                modifier = Modifier.size(16.dp),
                                tint = if (item.quantity < item.stock) MLBlue else Color.Gray
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(item.subtotalFormatted, fontWeight = FontWeight.Bold, color = MLBlue, fontSize = 14.sp)
                    IconButton(onClick = onRemove) {
                        Icon(Icons.Default.Delete, contentDescription = "Quitar", tint = MLGray)
                    }
                }
            }
        }
    }
}
