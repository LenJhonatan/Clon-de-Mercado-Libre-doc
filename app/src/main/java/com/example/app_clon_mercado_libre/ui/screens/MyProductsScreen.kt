package com.example.app_clon_mercado_libre.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.example.app_clon_mercado_libre.data.Product
import com.example.app_clon_mercado_libre.data.ProductRepository
import com.example.app_clon_mercado_libre.ui.components.ProductImage
import com.example.app_clon_mercado_libre.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProductsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    onNavigateToPublish: () -> Unit
) {
    val context = LocalContext.current
    val myProducts by ProductRepository.myProducts.collectAsState()
    val isLoading by ProductRepository.myProductsLoading.collectAsState()
    var productToDelete by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(Unit) {
        ProductRepository.loadMyProducts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis publicaciones", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = MLBlack)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MLYellow)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToPublish,
                containerColor = MLBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Publicar producto")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MLBackground)
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MLBlue)
                }
            } else if (myProducts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No has publicado productos aún.", color = MLGray, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Toca \"Publicar producto\" para empezar.", color = MLGray, fontSize = 13.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(myProducts, key = { it.id }) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                ProductImage(
                                    product = product,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(product.name, fontSize = 14.sp, fontWeight = FontWeight.Medium, maxLines = 2)
                                    Text(
                                        product.currentPrice,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MLBlue
                                    )
                                    Text(
                                        "Stock: ${product.stock}",
                                        fontSize = 12.sp,
                                        color = if (product.stock > 0) MLGray else Color.Red
                                    )
                                    if (!product.active) {
                                        Text("Inactivo", fontSize = 12.sp, color = Color.Red, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Column {
                                    IconButton(onClick = { onNavigateToEdit(product.id) }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MLBlue)
                                    }
                                    IconButton(onClick = { productToDelete = product }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }

    // Confirmación previa a eliminar (RF17)
    productToDelete?.let { product ->
        AlertDialog(
            onDismissRequest = { productToDelete = null },
            title = { Text("Eliminar publicación", fontWeight = FontWeight.Bold) },
            text = {
                Text("¿Seguro que quieres eliminar \"${product.name}\"? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        ProductRepository.deleteProduct(product.id) { ok, message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                        productToDelete = null
                    }
                ) {
                    Text("Eliminar", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { productToDelete = null }) {
                    Text("Cancelar", color = MLGray)
                }
            },
            containerColor = Color.White
        )
    }
}
