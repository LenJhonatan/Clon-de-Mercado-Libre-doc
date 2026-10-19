package com.example.app_clon_mercado_libre.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_clon_mercado_libre.data.Order
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
    onNavigateToCategories: () -> Unit = {},
    onNavigateToCart: () -> Unit = {}
) {
    val orders by ProductRepository.orders.collectAsState()
    val ordersLoading by ProductRepository.ordersLoading.collectAsState()

    LaunchedEffect(Unit) {
        ProductRepository.loadMyOrders()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis compras", fontWeight = FontWeight.Bold, color = Color.Black) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MLYellow)
            )
        },
        bottomBar = {
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
                    selected = false,
                    onClick = onNavigateToCart
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MLBackground)
        ) {
            when {
                ordersLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MLBlue)
                    }
                }
                orders.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Outlined.ReceiptLong,
                                contentDescription = null,
                                tint = MLGray,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("No has realizado compras aún.", color = MLGray)
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(orders, key = { it.id }) { order ->
                            OrderCard(order = order, onNavigateToDetail = onNavigateToDetail)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderCard(order: Order, onNavigateToDetail: (String) -> Unit) {
    val statusColor = when (order.status) {
        "pendiente" -> Color(0xFFF9A825)
        "pagado" -> MLBlue
        "enviado" -> Color(0xFF7B1FA2)
        "entregado" -> Color(0xFF00A650)
        else -> MLGray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(color = statusColor.copy(alpha = 0.15f), shape = RoundedCornerShape(4.dp)) {
                    Text(
                        order.status.replaceFirstChar { it.uppercase() },
                        color = statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Text(order.totalFormatted, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MLBlue)
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(order.itemsSummary, fontSize = 14.sp, maxLines = 2)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Pedido ${order.id.take(8).uppercase()} • Pago: ${order.paymentMethod} (simulado)",
                fontSize = 11.sp,
                color = MLGray
            )

            val firstProductId = order.items.firstOrNull()?.productId
            if (firstProductId != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Ver producto",
                    color = MLBlue,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onNavigateToDetail(firstProductId) }
                )
            }
        }
    }
}
