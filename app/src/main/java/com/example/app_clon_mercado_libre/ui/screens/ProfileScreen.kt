package com.example.app_clon_mercado_libre.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.app_clon_mercado_libre.R
import com.example.app_clon_mercado_libre.data.ProductRepository
import com.example.app_clon_mercado_libre.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToPurchases: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser

    var userName by remember { mutableStateOf("Cargando...") }
    var userPhotoUrl by remember { mutableStateOf("") }
    var nombreState by remember { mutableStateOf("") }
    var apellidoState by remember { mutableStateOf("") }
    var ciudadState by remember { mutableStateOf("") }
    var telefonoState by remember { mutableStateOf("") }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showEditAccountDialog by remember { mutableStateOf(false) }
    var isSavingAccount by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    val favoriteProducts = ProductRepository.products.filter { 
        ProductRepository.favorites.contains(it.id) 
    }
    val purchases = ProductRepository.purchases

    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nombre = document.getString("nombre") ?: ""

                        val apellido = document.getString("apellido") ?: ""
                        val ciudad = document.getString("ciudad") ?: ""
                        val telefono = document.getString("telefono") ?: ""
                       /// val photoUrl = document.getString("photoUrl") ?: ""

                        nombreState = nombre
                        apellidoState = apellido
                        ciudadState = ciudad
                        telefonoState = telefono
                        //userPhotoUrl = photoUrl

                        val fullName = "$nombre $apellido".trim()
                        userName = if (fullName.isNotEmpty()) fullName else (user.email ?: "Usuario")
                    } else {
                        userName = user.email ?: "Usuario"
                    }
                }
                .addOnFailureListener {
                    userName = user.email ?: "Usuario"
                }
        }
    }

    fun saveUserData() {
        val user = currentUser ?: return
        isSavingAccount = true

        val userData = hashMapOf(
            "nombre" to nombreState,
            "apellido" to apellidoState,
            "ciudad" to ciudadState,
            "telefono" to telefonoState
        )

        db.collection("users").document(user.uid)
            .set(userData, SetOptions.merge())
            .addOnSuccessListener {
                isSavingAccount = false
                showEditAccountDialog = false
                val fullName = "$nombreState $apellidoState".trim()
                userName = if (fullName.isNotEmpty()) fullName else (user.email ?: "Usuario")
                Toast.makeText(context, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                isSavingAccount = false
                Toast.makeText(context, "Error al guardar en Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
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
                    Icon(Icons.Outlined.Notifications, contentDescription = "Notificaciones", tint = MLBlack)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showEditAccountDialog = true },
                color = Color.White
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile Image Avatar
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (userPhotoUrl.isNotEmpty()) {
                            AsyncImage(
                                model = userPhotoUrl,
                                contentDescription = "Foto de perfil",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(40.dp), tint = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = userName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        if (ciudadState.isNotEmpty()) {
                            Text(text = ciudadState, fontSize = 13.sp, color = MLBlue)
                        }
                        Text(text = "Nivel 4 - Mercado Puntos", fontSize = 13.sp, color = MLGray)
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
                    MenuItem(Icons.Outlined.Person, "Mi cuenta", onClick = { showEditAccountDialog = true })
                    MenuItem(Icons.Outlined.Shield, "Seguridad")
                    MenuItem(Icons.Outlined.CreditCard, "Tarjetas")
                    MenuItem(Icons.Outlined.GridView, "Categorías", onClick =  onNavigateToCategories)
                    MenuItem(Icons.Outlined.LocationOn, "Direcciones")
                    MenuItem(Icons.Outlined.Checkroom, "Moda")
                    MenuItem(Icons.Outlined.Settings, "Configuración")
                    MenuItem(Icons.Outlined.MenuBook, "Libro de reclamaciones")
                    MenuItem(Icons.Outlined.HelpOutline, "Ayuda")
                    MenuItem(Icons.AutoMirrored.Outlined.ExitToApp, "Cerrar sesión", onClick = { showLogoutDialog = true })
                }
            }

            // Edit Account Dialog ("Mi cuenta")
            if (showEditAccountDialog) {
                AlertDialog(
                    onDismissRequest = { if (!isSavingAccount) showEditAccountDialog = false },
                    title = {
                        Text(
                            text = "Mi cuenta",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    text = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Avatar Picker
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray)
                                    .clickable { imagePickerLauncher.launch("image/*") },
                                contentAlignment = Alignment.Center
                            ) {
                                if (selectedImageUri != null) {
                                    AsyncImage(
                                        model = selectedImageUri,
                                        contentDescription = "Foto elegida",
                                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else if (userPhotoUrl.isNotEmpty()) {
                                    AsyncImage(
                                        model = userPhotoUrl,
                                        contentDescription = "Foto de perfil",
                                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(50.dp)
                                    )
                                }
                            }

                            TextButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                                Text("Seleccionar foto de perfil", color = MLBlue)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = nombreState,
                                onValueChange = { nombreState = it },
                                label = { Text("Nombre") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = apellidoState,
                                onValueChange = { apellidoState = it },
                                label = { Text("Apellidos") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = ciudadState,
                                onValueChange = { ciudadState = it },
                                label = { Text("Ciudad") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = telefonoState,
                                onValueChange = { telefonoState = it },
                                label = { Text("Teléfono") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = { saveUserData() },
                            enabled = !isSavingAccount,
                            colors = ButtonDefaults.buttonColors(containerColor = MLBlue)
                        ) {
                            if (isSavingAccount) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Guardar", color = Color.White)
                            }
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showEditAccountDialog = false },
                            enabled = !isSavingAccount
                        ) {
                            Text("Cancelar", color = MLGray)
                        }
                    },
                    containerColor = Color.White
                )
            }

            // Logout Confirmation Dialog
            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = { showLogoutDialog = false },
                    title = {
                        Text(
                            text = "¿Estas seguro que quieres salir?",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showLogoutDialog = false
                                auth.signOut()
                                onLogout()
                            }
                        ) {
                            Text("Sí", color = MLBlue, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showLogoutDialog = false }
                        ) {
                            Text("No", color = MLGray)
                        }
                    },
                    containerColor = Color.White
                )
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
fun MenuItem(icon: ImageVector, title: String, onClick: () -> Unit = {}) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
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
