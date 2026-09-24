package com.example.app_clon_mercado_libre.ui.screens

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_clon_mercado_libre.R
import com.example.app_clon_mercado_libre.data.ProductRepository
import com.example.app_clon_mercado_libre.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    productId: String?,
    onPurchaseSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToPurchases: () -> Unit,
    onNavigateToCategories: () -> Unit = {}
) {
    var selectedPayment by remember { mutableStateOf("Yape") }
    val product = ProductRepository.getProductById(productId)
    
    val productName = product?.name ?: "Producto"
    val productImage = product?.imageRes ?: R.drawable.mdl
    val productPrice = product?.currentPrice ?: "S/ 0"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finaliza Tu Compra", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = MLBlack)
                    }
                },
                actions = {
                    IconButton(onClick = { }) { Icon(Icons.Default.AccountCircle, null, tint = MLGray) }
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
                            Text(productPrice, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MLBlue)
                        }
                        Button(
                            onClick = {
                                product?.let { ProductRepository.purchases.add(it) }
                                onPurchaseSuccess()
                            },
                            modifier = Modifier.height(48.dp).fillMaxWidth(0.7f),
                            colors = ButtonDefaults.buttonColors(containerColor = MLBlue),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.VerifiedUser, null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Pagar y finalizar", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
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

            // Forma de Entrega
            CheckoutSection(title = "Forma de entrega") {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.LocationOn, null, tint = MLGray, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Dirección registrada", fontSize = 12.sp, color = MLGray)
                        Text("Av. Javier Prado Este 2450, Dpto 12D, San Borja, Lima", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Juan Pérez • 987 654 321", fontSize = 12.sp, color = MLGray)
                    }
                    Text("Modificar", color = MLBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Delivery Time
            CheckoutSection {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = Color(0xFFE8F5E9), shape = CircleShape, modifier = Modifier.size(40.dp)) {
                        Icon(Icons.Default.Bolt, null, tint = Color(0xFF00A650), modifier = Modifier.padding(8.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(color = Color(0xFFE0F2F1), shape = RoundedCornerShape(2.dp)) {
                                Text("FULL", color = Color(0xFF00A650), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Llega entre el viernes 24 y sábado 25", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Text("Envío prioritario a tu domicilio", fontSize = 12.sp, color = MLGray)
                        Text("Envío Gratis (Mercado Envíos FULL)", color = Color(0xFF00A650), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Medios de pago
            CheckoutSection(title = "Medios de pago", actionIcon = Icons.Default.Lock) {
                Column {
                    PaymentOption(
                        title = "Yape",
                        subtitle = "Paga al instante escaneando código QR o ingresando tu código de aprobación Yape desde tu celular.",
                        tag = "Rápido",
                        selected = selectedPayment == "Yape",
                        onSelect = { selectedPayment = "Yape" }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    PaymentOption(
                        title = "Tarjeta de crédito o débito",
                        subtitle = "Visa, Mastercard, American Express. Hasta 12 cuotas sin intereses con bancos seleccionados.",
                        selected = selectedPayment == "Tarjeta",
                        onSelect = { selectedPayment = "Tarjeta" }
                    )
                }
            }

            // Resumen del pedido
            CheckoutSection(title = "Resumen del pedido") {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = productImage),
                            contentDescription = null,
                            modifier = Modifier.size(60.dp).clip(RoundedCornerShape(4.dp)).background(Color.White).border(0.5.dp, Color.LightGray, RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(productName, fontSize = 14.sp, maxLines = 1)
                            Text("1 unidad • Titánio Gris 256GB", fontSize = 12.sp, color = MLGray)
                            Text(productPrice, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    SummaryRow("Producto", productPrice)
                    SummaryRow("Cupón Mercado Pago", "- S/ 150", isDiscount = true)
                    SummaryRow("Costo de envío", "GRATIS", isFree = true)
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(productPrice, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MLBlue)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
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
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
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
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 14.sp, color = MLGray)
        Text(
            text = value, 
            fontSize = 14.sp, 
            color = if (isDiscount) Color.Red else if (isFree) Color(0xFF00A650) else MLBlack,
            fontWeight = if (isFree) FontWeight.Bold else FontWeight.Normal
        )
    }
}
