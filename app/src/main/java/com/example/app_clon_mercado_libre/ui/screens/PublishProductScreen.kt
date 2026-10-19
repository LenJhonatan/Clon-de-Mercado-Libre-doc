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
import com.example.app_clon_mercado_libre.data.ProductRepository
import com.example.app_clon_mercado_libre.ui.components.ProductImageFromUrl
import com.example.app_clon_mercado_libre.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishProductScreen(
    onNavigateBack: () -> Unit,
    onPublishSuccess: () -> Unit
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var oldPrice by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Tecnología") }
    var delivery by remember { mutableStateOf("Llega mañana") }
    var freeShipping by remember { mutableStateOf(true) }
    var newImageUrl by remember { mutableStateOf("") }
    val imageUrls = remember { mutableStateListOf<String>() }
    var isSaving by remember { mutableStateOf(false) }
    var isUploading by remember { mutableStateOf(false) }
    var showCategoryMenu by remember { mutableStateOf(false) }

    val categories = listOf("Tecnología", "Moda", "Hogar", "Construcción", "Supermercado", "Deportes")

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val userId = auth.currentUser?.uid ?: return@rememberLauncherForActivityResult
            isUploading = true
            val ref = storage.reference.child("product_images/$userId/${System.currentTimeMillis()}.jpg")
            ref.putFile(uri)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { url ->
                        imageUrls.add(url.toString())
                        isUploading = false
                        Toast.makeText(context, "Foto subida correctamente", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    isUploading = false
                    Toast.makeText(context, "Error al subir foto: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun validate(): String? {
        if (name.isBlank()) return "Ingresa el nombre del producto"
        if (description.isBlank()) return "Ingresa la descripción"
        val priceValue = price.toDoubleOrNull()
        if (priceValue == null || priceValue <= 0) return "El precio debe ser un número mayor a 0"
        val stockValue = stock.toIntOrNull()
        if (stockValue == null || stockValue < 0) return "El stock debe ser un número mayor o igual a 0"
        return null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publicar producto", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = MLBlack)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MLYellow)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            SectionTitle("Datos del producto")

            PublishField("Nombre del producto", name, { name = it }, "Ej: Smartphone 128GB")
            PublishField("Descripción", description, { description = it }, "Características principales...", lines = 3)

            Row(modifier = Modifier.fillMaxWidth()) {
                PublishField(
                    "Precio (S/)", price, { price = it }, "Ej: 1459.99",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                PublishField(
                    "Precio anterior (opcional)", oldPrice, { oldPrice = it }, "Ej: 1899.99",
                    modifier = Modifier.weight(1f)
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                PublishField(
                    "Stock", stock, { stock = it }, "Ej: 25",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Categoría", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedButton(
                        onClick = { showCategoryMenu = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(category, fontSize = 14.sp)
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

            PublishField("Forma de entrega", delivery, { delivery = it }, "Ej: Llega mañana")

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = freeShipping, onCheckedChange = { freeShipping = it })
                Text("Ofrece envío gratis", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle("Fotos del producto")

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    enabled = !isUploading,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isUploading) "Subiendo..." else "Subir foto")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newImageUrl,
                    onValueChange = { newImageUrl = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("o pega una URL de imagen", fontSize = 13.sp) },
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

            if (imageUrls.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
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
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val error = validate()
                    if (error != null) {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    isSaving = true
                    val userId = auth.currentUser?.uid ?: run {
                        isSaving = false
                        Toast.makeText(context, "Debes iniciar sesión", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    db.collection("users").document(userId).get()
                        .addOnSuccessListener { userDoc ->
                            val sellerName = listOfNotNull(
                                userDoc.getString("nombre"),
                                userDoc.getString("apellido")
                            ).joinToString(" ").ifBlank { userDoc.getString("email") ?: "Vendedor" }

                            val data = hashMapOf(
                                "name" to name.trim(),
                                "description" to description.trim(),
                                "price" to (price.toDoubleOrNull() ?: 0.0),
                                "oldPrice" to (oldPrice.toDoubleOrNull()),
                                "stock" to (stock.toIntOrNull() ?: 0),
                                "category" to category,
                                "images" to imageUrls.toList(),
                                "tag" to "OFERTA",
                                "delivery" to delivery.trim(),
                                "freeShipping" to freeShipping,
                                "active" to true,
                                "sellerId" to userId,
                                "sellerName" to sellerName,
                                "sellerRating" to 0.0,
                                "sellerSales" to 0,
                                "searchKeywords" to ProductRepository.buildKeywords(name, category)
                            )
                            // quitar nulls (oldPrice vacío)
                            val cleanData: Map<String, Any?> = data.filterValues { it != null }

                            ProductRepository.publishProduct(cleanData) { ok, message ->
                                isSaving = false
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                if (ok) onPublishSuccess()
                            }
                        }
                        .addOnFailureListener { e ->
                            isSaving = false
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
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
                    Text("Publicar producto", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MLBlack)
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun PublishField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
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
            placeholder = { Text(placeholder, color = MLGray, fontSize = 13.sp) },
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
