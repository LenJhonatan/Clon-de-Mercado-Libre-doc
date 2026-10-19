package com.example.app_clon_mercado_libre.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
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
import com.example.app_clon_mercado_libre.ui.components.ProductImageFromUrl
import com.example.app_clon_mercado_libre.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    productId: String?,
    onNavigateBack: () -> Unit,
    onSaved: () -> Unit
) {
    val context = LocalContext.current
    val storage = FirebaseStorage.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var isUploading by remember { mutableStateOf(false) }
    var notFound by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var oldPrice by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var delivery by remember { mutableStateOf("") }
    var freeShipping by remember { mutableStateOf(false) }
    var active by remember { mutableStateOf(true) }
    var newImageUrl by remember { mutableStateOf("") }
    val imageUrls = remember { mutableStateListOf<String>() }
    var showCategoryMenu by remember { mutableStateOf(false) }

    val categories = listOf("Tecnología", "Moda", "Hogar", "Construcción", "Supermercado", "Deportes")

    LaunchedEffect(productId) {
        ProductRepository.getProductById(productId) { product: Product? ->
            if (product == null) {
                notFound = true
            } else {
                name = product.name
                description = product.description
                price = if (product.price % 1.0 == 0.0) product.price.toLong().toString() else product.price.toString()
                oldPrice = product.oldPrice?.let {
                    if (it % 1.0 == 0.0) it.toLong().toString() else it.toString()
                } ?: ""
                stock = product.stock.toString()
                category = product.category.ifBlank { "Tecnología" }
                delivery = product.delivery.ifBlank { "Llega mañana" }
                freeShipping = product.freeShipping
                active = product.active
                imageUrls.addAll(product.images)
            }
            isLoading = false
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null && productId != null) {
            isUploading = true
            val ref = storage.reference.child("product_images/$userId/${productId}_${System.currentTimeMillis()}.jpg")
            ref.putFile(uri)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { url ->
                        imageUrls.add(url.toString())
                        isUploading = false
                        Toast.makeText(context, "Foto subida", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    isUploading = false
                    Toast.makeText(context, "Error al subir: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar producto", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = MLBlack)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MLYellow)
            )
        }
    ) { paddingValues ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = MLBlue) }
            }
            notFound -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) { Text("Producto no encontrado.", color = MLGray) }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    PublishFieldSimple("Nombre", name, { name = it })
                    PublishFieldSimple("Descripción", description, { description = it }, lines = 3)

                    Row(modifier = Modifier.fillMaxWidth()) {
                        PublishFieldSimple("Precio (S/)", price, { price = it }, modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(12.dp))
                        PublishFieldSimple("Precio anterior", oldPrice, { oldPrice = it }, modifier = Modifier.weight(1f))
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        PublishFieldSimple("Stock", stock, { stock = it }, modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Categoría", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedButton(
                                onClick = { showCategoryMenu = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(category, fontSize = 13.sp)
                                DropdownMenu(
                                    expanded = showCategoryMenu,
                                    onDismissRequest = { showCategoryMenu = false }
                                ) {
                                    categories.forEach { c ->
                                        DropdownMenuItem(
                                            text = { Text(c) },
                                            onClick = {
                                                category = c
                                                showCategoryMenu = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    PublishFieldSimple("Forma de entrega", delivery, { delivery = it })

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = freeShipping, onCheckedChange = { freeShipping = it })
                        Text("Envío gratis", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Checkbox(checked = active, onCheckedChange = { active = it })
                        Text("Visible (activo)", fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Fotos", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            enabled = !isUploading,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.CloudUpload, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isUploading) "Subiendo..." else "Subir foto", fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = newImageUrl,
                            onValueChange = { newImageUrl = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("o pega una URL", fontSize = 13.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                        IconButton(
                            onClick = {
                                if (newImageUrl.isNotBlank()) {
                                    imageUrls.add(newImageUrl.trim())
                                    newImageUrl = ""
                                }
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Agregar URL", tint = MLBlue)
                        }
                    }

                    imageUrls.forEachIndexed { index, url ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            ProductImageFromUrl(
                                url = url,
                                contentDescription = "Foto ${index + 1}",
                                modifier = Modifier.size(56.dp).clip(RoundedCornerShape(6.dp))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(url, fontSize = 11.sp, color = MLGray, modifier = Modifier.weight(1f), maxLines = 2)
                            IconButton(onClick = { imageUrls.removeAt(index) }) {
                                Icon(Icons.Default.Close, contentDescription = "Quitar foto", tint = Color.Red)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            val priceValue = price.toDoubleOrNull()
                            val stockValue = stock.toIntOrNull()
                            when {
                                name.isBlank() -> Toast.makeText(context, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
                                priceValue == null || priceValue <= 0 -> Toast.makeText(context, "Precio inválido", Toast.LENGTH_SHORT).show()
                                stockValue == null || stockValue < 0 -> Toast.makeText(context, "Stock inválido", Toast.LENGTH_SHORT).show()
                                else -> {
                                    isSaving = true
                                    val data = hashMapOf<String, Any?>(
                                        "name" to name.trim(),
                                        "description" to description.trim(),
                                        "price" to priceValue,
                                        "oldPrice" to (oldPrice.toDoubleOrNull()),
                                        "stock" to stockValue,
                                        "category" to category,
                                        "delivery" to delivery.trim(),
                                        "freeShipping" to freeShipping,
                                        "active" to active,
                                        "images" to imageUrls.toList(),
                                        "searchKeywords" to ProductRepository.buildKeywords(name, category)
                                    )
                                    ProductRepository.updateProduct(productId ?: "", data.filterValues { it != null }) { ok, message ->
                                        isSaving = false
                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                        if (ok) onSaved()
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        enabled = !isSaving && !isUploading,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MLBlue)
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                        } else {
                            Text("Guardar cambios", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun PublishFieldSimple(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    lines: Int = 1
) {
    Column(modifier = modifier.padding(bottom = 12.dp)) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = lines == 1,
            minLines = lines,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MLBlue,
                unfocusedBorderColor = Color.LightGray
            )
        )
    }
}
