package com.example.app_clon_mercado_libre.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_clon_mercado_libre.ui.theme.MLBlue
import com.example.app_clon_mercado_libre.ui.theme.MLGray
import com.google.firebase.auth.FirebaseAuth

@Composable
fun VerificationScreen(
    email: String,
    onConfirmCode: () -> Unit
) {
    var code by remember { mutableStateOf(List(6) { "" }) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    // Enviar email de verificación al cargar la pantalla
    LaunchedEffect(Unit) {
        auth.currentUser?.sendEmailVerification()
            ?.addOnSuccessListener {
                Toast.makeText(context, "Se envió un enlace de verificación a su correo", Toast.LENGTH_LONG).show()
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF)),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Green Check Icon
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(0xFFE8F5E9), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("¡Ya casi terminas!", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Enviamos un enlace de verificación a $email. Por favor, revísalo antes de continuar.",
                    fontSize = 14.sp,
                    color = MLGray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 6 Code inputs (Simulado)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    code.forEachIndexed { index, value ->
                        OutlinedTextField(
                            value = value,
                            onValueChange = { newValue ->
                                if (newValue.length <= 1) {
                                    val newCode = code.toMutableList()
                                    newCode[index] = newValue
                                    code = newCode
                                }
                            },
                            modifier = Modifier
                                .width(40.dp)
                                .height(56.dp),
                            shape = RoundedCornerShape(8.dp),
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MLBlue,
                                unfocusedBorderColor = Color.LightGray
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        isLoading = true
                        // Recargamos el usuario para ver si ya verificó el email
                        auth.currentUser?.reload()?.addOnCompleteListener {
                            isLoading = false
                            // En un clon visual, dejamos pasar al usuario a Home
                            onConfirmCode()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MLBlue),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Confirmar código", fontSize = 16.sp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { 
                    auth.currentUser?.sendEmailVerification()
                    Toast.makeText(context, "Correo reenviado", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Reenviar código", color = MLBlue, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

