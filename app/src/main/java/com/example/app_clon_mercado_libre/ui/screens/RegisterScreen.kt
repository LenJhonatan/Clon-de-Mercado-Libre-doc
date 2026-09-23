package com.example.app_clon_mercado_libre.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_clon_mercado_libre.ui.theme.MLBlue
import com.example.app_clon_mercado_libre.ui.theme.MLGray
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit,
    onNavigateToVerification: (email: String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Registro - Datos Personales", fontSize = 16.sp) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = MLBlue)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Color(0xFFE0E0E0))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .fillMaxHeight()
                            .background(MLBlue)
                    )
                }
            }
        },
        bottomBar = {
            Button(
                onClick = { 
                    if (password != confirmPassword) {
                        Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    isLoading = true
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener { result ->
                            val uid = result.user?.uid ?: ""
                            val userMap = hashMapOf(
                                "uid" to uid,
                                "nombre" to nombre,
                                "apellido" to apellido,
                                "email" to email,
                                "dni" to dni,
                                "createdAt" to Timestamp.now()
                            )
                            
                            db.collection("users").document(uid).set(userMap)
                                .addOnSuccessListener {
                                    isLoading = false
                                    onNavigateToVerification(email)
                                }
                                .addOnFailureListener { e ->
                                    isLoading = false
                                    Toast.makeText(context, "Error al guardar datos: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                        }
                        .addOnFailureListener { e ->
                            isLoading = false
                            Toast.makeText(context, "Error en registro: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MLBlue),
                enabled = !isLoading && termsAccepted && nombre.isNotEmpty() && email.isNotEmpty() && password.length >= 8
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Crear cuenta", fontSize = 16.sp, color = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Completa tus datos", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Solo unos pasos más para terminar.", fontSize = 14.sp, color = MLGray)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            RegisterField(label = "Nombre", value = nombre, onValueChange = { nombre = it }, placeholder = "Ej: Juan")
            RegisterField(label = "Apellido", value = apellido, onValueChange = { apellido = it }, placeholder = "Ej: Pérez")
            RegisterField(label = "Correo electrónico", value = email, onValueChange = { email = it }, placeholder = "Ej: juan.perez@ejemplo.com")
            RegisterField(label = "DNI / RUC", value = dni, onValueChange = { dni = it }, placeholder = "Ej: 74485358", isOptional = true)
            RegisterField(label = "Contraseña", value = password, onValueChange = { password = it }, placeholder = "Mínimo 8 caracteres", isPassword = true)
            
            Text("• Al menos 8 caracteres", fontSize = 12.sp, color = MLGray, modifier = Modifier.padding(start = 4.dp))
            
            RegisterField(label = "Confirmar contraseña", value = confirmPassword, onValueChange = { confirmPassword = it }, placeholder = "Repite tu contraseña", isPassword = true)

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = termsAccepted, onCheckedChange = { termsAccepted = it })
                Text(
                    text = "Acepto los Términos y Condiciones y Políticas de Privacidad",
                    fontSize = 13.sp,
                    color = MLBlue
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = Color(0xFFF5F5F5),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Tus datos están protegidos. No los compartiremos con terceros sin tu consentimiento.",
                        fontSize = 12.sp,
                        color = Color(0xFF616161)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


@Composable
fun RegisterField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isOptional: Boolean = false,
    isPassword: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            if (isOptional) {
                Text("Opcional", fontSize = 12.sp, color = MLGray)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = MLGray) },
            shape = RoundedCornerShape(8.dp),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MLBlue,
                unfocusedBorderColor = Color.LightGray
            )
        )
    }
}
