package com.example.app_clon_mercado_libre.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_clon_mercado_libre.R
import com.example.app_clon_mercado_libre.ui.theme.*

import com.example.app_clon_mercado_libre.data.ProductRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String?,
    onNavigateBack: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToAddress: (String) -> Unit,
    onNavigateToPurchases: () -> Unit,
    onNavigateToCategories: () -> Unit = {}
) {
    var selectedColor by remember { mutableStateOf("Negro") }
    var quantity by remember { mutableStateOf(1) }
    var showQuantityMenu by remember { mutableStateOf(false) }
    var couponApplied by remember { mutableStateOf(false) }

    // Obtener datos reales del repositorio
    val product = ProductRepository.getProductById(productId)
    val isFavorite = product?.let { ProductRepository.favorites.contains(it.id) } ?: false
    
    val productName = product?.name ?: "Producto no encontrado"
    val price = product?.currentPrice ?: "S/ 0"
    val oldPrice = product?.oldPrice ?: "S/ 0"
    val productImage = product?.imageRes ?: R.drawable.mdl // Placeholder
    val delivery = product?.delivery ?: "Llega hoy"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = MLBlack)
                    }
                },
                actions = {
                    IconButton(onClick = { }) { Icon(Icons.Default.Search, contentDescription = "Buscar", tint = MLBlack) }
                    IconButton(onClick = { }) { Icon(Icons.Outlined.Notifications, contentDescription = "Notificaciones", tint = MLBlack) }
                    IconButton(onClick = { }) { Icon(Icons.Outlined.Share, contentDescription = "Compartir", tint = MLBlack) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MLYellow)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, null) }, 
                    label = { Text("Inicio", fontSize = 10.sp) }, 
                    selected = false, 
                    onClick = onNavigateBack
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.GridView, null) }, 
                    label = { Text("Categorías", fontSize = 10.sp) }, 
                    selected = false, 
                    onClick = onNavigateToCategories
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.ShoppingCart, null) }, 
                    label = { Text("Carrito", fontSize = 10.sp) }, 
                    selected = false, 
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
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            // Image and Counter
            Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                Image(
                    painter = painterResource(id = productImage),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
                Surface(
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                ) {
                    Text("1 / 4", color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text("Ver más productos marca Roadtrip", color = MLBlue, fontSize = 14.sp)
                
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                    Text(
                        text = productName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(1f),
                        lineHeight = 22.sp,
                        color = Color.Black
                    )
                    IconButton(onClick = { 
                        product?.let { 
                            if (isFavorite) {
                                ProductRepository.favorites.remove(it.id)
                            } else {
                                ProductRepository.favorites.add(it.id)
                            }
                        }
                    }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (isFavorite) MLBlue else Color.LightGray
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) {
                        Icon(Icons.Default.Star, null, tint = MLBlue, modifier = Modifier.size(16.dp))
                    }
                    Text(" 5.0 | +5 vendidos", fontSize = 14.sp, color = Color.Black)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = oldPrice,
                    fontSize = 16.sp,
                    color = Color.Red,
                    textDecoration = TextDecoration.LineThrough
                )
                Text(text = price, fontSize = 32.sp, fontWeight = FontWeight.Bold,color = Color.Black)   //,color = Color.Black
                Text("6 cuotas de S/ 24.83 sin interés", color = Color(0xFF00A650), fontSize = 16.sp)  //(0xFF00A650)

                Spacer(modifier = Modifier.height(16.dp))

                // Medios de pago
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Medios de pago", color = MLBlue, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Row {
                        repeat(4) {
                            Box(modifier = Modifier.size(24.dp).padding(2.dp).background(Color(0xFFE0E0E0), RoundedCornerShape(2.dp)))
                        }
                        Text("+1", color = MLBlue, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Cupon
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ConfirmationNumber, null, tint = MLBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cupón", color = MLBlue, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        Checkbox(checked = couponApplied, onCheckedChange = { couponApplied = it })
                        Text("Aplicar S/ 25 OFF.", fontSize = 14.sp)
                    }
                }
                Text("Ver cupones disponibles", color = MLBlue, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))

                Spacer(modifier = Modifier.height(24.dp))

                // Colores
                Text("Colores: $selectedColor", fontWeight = FontWeight.Bold, color = Color.Black )   //, color = Color.Black
                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    ColorOption(productImage, isSelected = selectedColor == "Negro") { selectedColor = "Negro" }
                    Spacer(modifier = Modifier.width(8.dp))
                    ColorOption(productImage, isSelected = selectedColor == "Gris") { selectedColor = "Gris" }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Envio
                Text("Envío gratis a todo el país", color = Color(0xFF00A650), fontWeight = FontWeight.Bold)
                Text(text = "Conoce los tiempos y las formas de envío.", color = MLGray, fontSize = 14.sp)
                Text("Calcular cuándo llega", color = MLBlue, fontSize = 14.sp, modifier = Modifier.clickable { })

                Spacer(modifier = Modifier.height(24.dp))

                // Cantidad
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showQuantityMenu = true },
                    color = Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Cantidad: $quantity", fontWeight = FontWeight.Bold, color = Color.Black )   //, color = Color.Black
                        Text("(+5 disponibles)", color = MLGray, fontSize = 12.sp)   //
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = MLBlue)
                    }
                    
                    DropdownMenu(
                        expanded = showQuantityMenu,
                        onDismissRequest = { showQuantityMenu = false }
                    ) {
                        (1..6).forEach { num ->
                            DropdownMenuItem(
                                text = { Text("$num unidad${if (num > 1) "es" else ""}") },
                                onClick = {
                                    quantity = num
                                    showQuantityMenu = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botones
                Button(
                    onClick = { 
                        product?.let { onNavigateToAddress(it.id) }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MLBlue)
                ) {
                    Text("Comprar Ahora", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MLBlue),
                    border = BorderStroke(1.dp, MLBlue.copy(alpha = 0.3f))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AddShoppingCart, null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Agregar al carrito", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}


@Composable
fun ColorOption(imageRes: Int, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MLBlue else Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Image(painter = painterResource(id = imageRes), contentDescription = null, modifier = Modifier.fillMaxSize())
    }
}
