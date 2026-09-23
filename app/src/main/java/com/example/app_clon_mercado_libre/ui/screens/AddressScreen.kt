package com.example.app_clon_mercado_libre.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_clon_mercado_libre.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    productId: String?,
    onNavigateToCheckout: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var calle by remember { mutableStateOf("") }
    var departamento by remember { mutableStateOf("Lima") }
    var provincia by remember { mutableStateOf("Lima") }
    var distrito by remember { mutableStateOf("San Borja") }
    var piso by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }
    var nombreRecibe by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Domicilio", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) },
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MLBackground)
                .verticalScroll(rememberScrollState())
        ) {
            // GPS Section
            Surface(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(50.dp),
                            shape = CircleShape,
                            color = Color(0xFFE3F2FD)
                        ) {
                            Icon(Icons.Default.MyLocation, null, tint = MLBlue, modifier = Modifier.padding(12.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Usar mi ubicación actual", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(color = Color(0xFFE8EAF6), shape = RoundedCornerShape(4.dp)) {
                                    Text("Rápido", color = MLBlue, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 4.dp))
                                }
                            }
                            Text("Localizaremos tu dirección en el mapa para mayor precisión y envíos más ágiles.", fontSize = 12.sp, color = MLGray)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MLBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.PlayCircle, null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Activar GPS", color = Color.White)
                    }
                }
            }

            Text(
                "O INGRESA MANUALMENTE", 
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 12.sp,
                color = MLGray,
                fontWeight = FontWeight.Bold
            )

            // Address Form
            Surface(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = MLYellow, shape = RoundedCornerShape(4.dp), modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.LocationOn, null, tint = MLBlack, modifier = Modifier.padding(4.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Dirección del domicilio", fontWeight = FontWeight.Bold)
                            Text("Indica calle, número y referencias de llegada", fontSize = 12.sp, color = MLGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    AddressInputField(label = "Calle / Avenida y Número", value = calle, onValueChange = { calle = it }, isRequired = true, placeholder = "Ej: Av. Javier Prado Este 2450")
                    
                    AddressDropdownField(label = "Departamento", value = departamento)
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Box(modifier = Modifier.weight(1f)) {
                            AddressDropdownField(label = "Provincia", value = provincia)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            AddressDropdownField(label = "Distrito", value = distrito)
                        }
                    }

                    AddressInputField(label = "Piso / Dpto / Interior", value = piso, onValueChange = { piso = it }, placeholder = "Ejm: Torre B, Dpto 402, Intercom 12", isOptional = true)
                    AddressInputField(label = "Referencia de llegada", value = referencia, onValueChange = { referencia = it }, placeholder = "Ej: Frente al parque, reja de color negro", isOptional = true)
                }
            }

            // Receiver Info
            Surface(
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = Color(0xFFE3F2FD), shape = RoundedCornerShape(4.dp), modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Person, null, tint = MLBlue, modifier = Modifier.padding(4.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Datos de quien recibe", fontWeight = FontWeight.Bold)
                            Text("Para coordinar la entrega en puerta", fontSize = 12.sp, color = MLGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Surface(color = Color(0xFFF5F5F5), shape = RoundedCornerShape(4.dp), modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, null, tint = MLBlue, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Solo te llamaremos o enviaremos WhatsApp si el transportista tiene dificultades para encontrar la vivienda.", fontSize = 10.sp, color = MLGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AddressInputField(label = "Nombre y Apellido", value = nombreRecibe, onValueChange = { nombreRecibe = it }, isRequired = true, placeholder = "Ej: Carlos Mendoza Rossi")
                    AddressInputField(label = "Teléfono de contacto", value = telefono, onValueChange = { telefono = it }, isRequired = true, placeholder = "987 654 321", isPhone = true)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onNavigateToCheckout,
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MLBlue),
                enabled = calle.isNotEmpty() && nombreRecibe.isNotEmpty() && telefono.isNotEmpty()
            ) {
                Text("Guardar domicilio", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, null, tint = Color.White)
            }

            Text(
                "Tus datos están protegidos por el programa de compra protegida de Mercado Pago.",
                modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),
                fontSize = 10.sp,
                color = MLGray,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun AddressInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isRequired: Boolean = false,
    isOptional: Boolean = false,
    isPhone: Boolean = false
) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            if (isRequired) Text("* Obligatorio", color = Color.Red, fontSize = 12.sp)
            if (isOptional) Text("Opcional", color = MLGray, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = MLGray, fontSize = 14.sp) },
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MLBlue,
                unfocusedBorderColor = Color.LightGray,
                unfocusedContainerColor = Color(0xFFFFF9C4).copy(alpha = 0.1f)
            ),
            prefix = if (isPhone) { { Text("PE +51 ", fontWeight = FontWeight.Bold) } } else null
        )
    }
}

@Composable
fun AddressDropdownField(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, null, tint = MLGray) },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                unfocusedContainerColor = Color(0xFFFFF9C4).copy(alpha = 0.1f)
            )
        )
    }
}
