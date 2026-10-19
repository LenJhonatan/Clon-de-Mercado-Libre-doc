package com.example.app_clon_mercado_libre.ui.screens

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_clon_mercado_libre.data.ProductRepository
import com.example.app_clon_mercado_libre.data.formatPrice
import com.example.app_clon_mercado_libre.ui.components.ProductImageFromUrl
import com.example.app_clon_mercado_libre.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onPurchaseSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToPurchases: () -> Unit,
    onNavigateToCategories: () -> Unit = {},
    onNavigateToCart: () -> Unit = {}
) {
    val context = LocalContext.current
    var selectedPayment by remember { mutableStateOf("Yape") }
    var isPaying by remember { mutableStateOf(false) }

    val cartItems by ProductRepository.cartItems.collectAsState()

    LaunchedEffect(Unit) {
        ProductRepository.loadCart()
    }

    val total = cartItems.sumOf { it.price * it.quantity }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finaliza Tu Compra", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = MLBlack)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MLYellow)
            )
        },
        bottomBar = {
            Column {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 8.dp,
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total a pagar:", fontSize = 12.sp, color = MLGray)
                            Text(formatPrice(total), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MLBlue)
                        }
                        Button(
                            onClick = {
                                if (cartItems.isEmpty()) return@Button
                                isPaying = true
                                ProductRepository.purchaseCart(selectedPayment) { ok, message ->
                                    isPaying = false
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                    if (ok) onPurchaseSuccess()
                                }
                            },
                            enabled = !isPaying && cartItems.isNotEmpty(),
                            modifier = Modifier.height(48.dp).fillMaxWidth(0.6f),
                            colors = ButtonDefaults.buttonColors(containerColor = MLBlue),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            if (isPaying) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Default.VerifiedUser, null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Pagar y finalizar", color = Color.White, fontWeight = FontWeight.Bold)
                            }
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MLBackground)
                .verticalScroll(rememberScrollState())
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth().padding(bottom = 1.dp),
                color = Color.White
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VerifiedUser, null, tint = Color(0xFF00A650), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Compra 100% protegida", color = Color(0xFF00A650), fontSize = 12.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Text("Paso 3 de 3", fontSize = 12.sp, color = MLGray)
                }
            }

            // Aviso de pago simulado (RF09)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFFFF8E1)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Info, null, tint = Color(0xFFF9A825), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Pago simulado: esta app no realiza cobros reales. Al confirmar, se registrará el pedido y se descontará el stock.",
                        fontSize = 12.sp,
                        color = Color(0xFF6D4C00),
                        lineHeight = 16.sp
                    )
                }
            }

            if (cartItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("Tu carrito está vacío.", color = MLGray)
                }
            } else {
                // Resumen de productos
                CheckoutSection(title = "Productos (${cartItems.size})") {
                    cartItems.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProductImageFromUrl(
                                url = item.imageUrl,
                                contentDescription = item.name,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .border(0.5.dp, Color.LightGray, RoundedCornerShape(4.dp))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.name, fontSize = 13.sp, maxLines = 1)
                                Text("Cantidad: ${item.quantity}", fontSize = 12.sp, color = MLGray)
                            }
                            Text(item.subtotalFormatted, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Medios de pago
                CheckoutSection(title = "Medios de pago", actionIcon = Icons.Default.Lock) {
                    Column {
                        PaymentOption(
                            title = "Yape",
                            subtitle = "Pago simulado. En la app real escanearías el código QR.",
                            tag = "Rápido",
                            selected = selectedPayment == "Yape",
                            onSelect = { selectedPayment = "Yape" }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        PaymentOption(
                            title = "Tarjeta de crédito o débito",
                            subtitle = "Visa, Mastercard, American Express. Pago simulado, no se realiza cobro.",
                            selected = selectedPayment == "Tarjeta",
                            onSelect = { selectedPayment = "Tarjeta" }
                        )
                    }
                }

                // Resumen del pedido
                CheckoutSection(title = "Resumen del pedido") {
                    Column {
                        SummaryRow("Productos", formatPrice(total))
                        SummaryRow("Costo de envío", "GRATIS", isFree = true)

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(formatPrice(total), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MLBlue)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Lock, null, tint = MLGray, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Transacción cifrada de extremo a extremo", color = MLGray, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun CheckoutSection(
    title: String? = null,
    actionIcon: ImageVector? = null,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (title != null) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    if (actionIcon != null) Icon(actionIcon, null, tint = MLGray, modifier = Modifier.size(20.dp))
                }
            }
            content()
        }
    }
}

@Composable
fun PaymentOption(
    title: String,
    subtitle: String,
    tag: String? = null,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onSelect() },
        color = Color(0xFFFFF9C4).copy(alpha = 0.2f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(if (selected) 2.dp else 0.5.dp, if (selected) MLBlue else Color.LightGray)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
            RadioButton(selected = selected, onClick = onSelect, colors = RadioButtonDefaults.colors(selectedColor = MLBlue))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    if (tag != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(color = MLYellow, shape = RoundedCornerShape(2.dp)) {
                            Text(tag, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp))
                        }
                    }
                }
                Text(subtitle, fontSize = 12.sp, color = MLGray, lineHeight = 16.sp)
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String, isDiscount: Boolean = false, isFree: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, color = MLGray)
        Text(
            text = value,
            fontSize = 14.sp,
            color = if (isDiscount) Color.Red else if (isFree) Color(0xFF00A650) else MLBlack,
            fontWeight = if (isFree) FontWeight.Bold else FontWeight.Normal
        )
    }
}
