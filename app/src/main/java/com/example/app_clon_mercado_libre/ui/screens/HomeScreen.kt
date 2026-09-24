package com.example.app_clon_mercado_libre.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_clon_mercado_libre.R
import com.example.app_clon_mercado_libre.data.Product
import com.example.app_clon_mercado_libre.data.ProductRepository
import com.example.app_clon_mercado_libre.ui.theme.*

@Composable
fun HomeScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToPurchases: () -> Unit,
    onNavigateToCategories: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val filteredProducts = if (searchQuery.isEmpty()) {
        ProductRepository.products
    } else {
        ProductRepository.products.filter { 
            it.name.contains(searchQuery, ignoreCase = true) || 
            it.category.contains(searchQuery, ignoreCase = true)
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
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = MLGray, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Search in the store...", color = MLGray, fontSize = 14.sp) },
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                ),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(onSearch = {
                                    keyboardController?.hide()
                                }),
                                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(Icons.Outlined.Notifications, contentDescription = "Notificaciones", tint = MLBlack)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = MLBlack, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ingresa tu código postal...", fontSize = 12.sp, color = MLBlack, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = MLBlack, modifier = Modifier.size(16.dp))
                }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null, tint = MLBlue) },
                    label = { Text("Inicio", color = Color.Black, fontSize = 10.sp) },

                    selected = true,
                    onClick = { }

                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.GridView, contentDescription = null) },
                    label = { Text("Categorias", color = Color.Black, fontSize = 10.sp) },
                    selected = false,
                    onClick = onNavigateToCategories
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.ShoppingCart, contentDescription = null) },
                    label = { Text("Carrito",color = Color.Black, fontSize = 10.sp) },
                    selected = false,
                    onClick = onNavigateToPurchases
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.PlayCircle, contentDescription = null) },
                    label = { Text("Videos", color = Color.Black, fontSize = 10.sp) },
                    selected = false,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Menu, contentDescription = null) },
                    label = { Text("Más",color = Color.Black, fontSize = 10.sp) },
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
                .verticalScroll(rememberScrollState())
        ) {
            // Main Banner
            Image(
                painter = painterResource(id = R.drawable.especialdetecnologia),
                contentDescription = "Especial Tecnologia",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.FillBounds
            )

            // Categories Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                CategoryItem("Supermercado", R.drawable.supermercado)
                CategoryItem("Moda", R.drawable.modas)
                CategoryItem("Construcción", R.drawable.contruccion)
                CategoryItem("Envíos Rápidos", R.drawable.enviosrapidos)
                CategoryItem("Ver más", R.drawable.mas)
            }

            // Offers Section Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Ofertas del día", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MLBlack)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.FlashOn, contentDescription = null, tint = MLBlue, modifier = Modifier.size(16.dp))
                }
                Text("Ver todas", color = MLBlue, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }

            // Grid of products (Simulated with Columns/Rows because of verticalScroll parent)
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                val chunkedProducts = filteredProducts.chunked(2)
                chunkedProducts.forEach { pair ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        pair.forEach { product ->
                            ProductCard(
                                product, 
                                modifier = Modifier.weight(1f).clickable { onNavigateToDetail(product.id) }
                            )
                        }
                        if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            
            if (filteredProducts.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("No se encontraron productos.", color = MLGray)
                }
            }


            // Subscription Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B4D)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Suscribite a Meli+", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(
                            "Envíos gratis en millones de productos y más beneficios.",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        Text("Conocer más", color = Color(0xFF1B1B4D), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CategoryItem(name: String, iconRes: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(70.dp)
    ) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = name,
                modifier = Modifier.padding(12.dp),
                contentScale = ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            fontSize = 10.sp,
            color = MLBlack,
            textAlign = TextAlign.Center,
            lineHeight = 12.sp,
            maxLines = 2
        )
    }
}

@Composable
fun ProductCard(product: Product, modifier: Modifier = Modifier) {
    val isFavorite = ProductRepository.favorites.contains(product.id)


    Card(
        modifier = modifier.padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
                
                // Favorite Heart in Card
                IconButton(
                    onClick = { 
                        if (isFavorite) {
                            ProductRepository.favorites.remove(product.id)
                        } else {
                            ProductRepository.favorites.add(product.id)
                        }
                    },
                    modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (isFavorite) MLBlue else Color.LightGray,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Surface(
                    color = Color(0xFF00A650),
                    modifier = Modifier.align(Alignment.BottomStart).padding(8.dp).clip(RoundedCornerShape(2.dp))
                ) {
                    Text(
                        product.tag,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
            
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = product.currentPrice,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black    //MLBlack
                )
                Text(
                    text = product.oldPrice,
                    fontSize = 12.sp,
                    color = Color.Red,
                    textDecoration = TextDecoration.LineThrough
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.FlashOn, contentDescription = null, tint = Color(0xFF00A650), modifier = Modifier.size(12.dp))
                    Text(text = product.delivery, color = Color(0xFF00A650), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                
                if (product.freeShipping) {
                    Text(text = "Envío gratis", color = Color(0xFF00A650), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = product.name,
                    fontSize = 12.sp,
                    color = Color.Black,  //MLBlack // //MLGray
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
