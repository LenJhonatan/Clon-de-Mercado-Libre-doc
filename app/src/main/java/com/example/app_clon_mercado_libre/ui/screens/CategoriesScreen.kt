package com.example.app_clon_mercado_libre.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.QueueMusic
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_clon_mercado_libre.R
import com.example.app_clon_mercado_libre.ui.theme.MLBackground
import com.example.app_clon_mercado_libre.ui.theme.MLBlack
import com.example.app_clon_mercado_libre.ui.theme.MLBlue
import com.example.app_clon_mercado_libre.ui.theme.MLYellow

data class Subcategory(
    val name: String,
    val icon: ImageVector? = null,
    val imageRes: Int? = null
)

data class CategoryGroup(
    val name: String,
    val subcategories: List<Subcategory>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToPurchases: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    val categories = remember {
        listOf(
            CategoryGroup(
                name = "Accesorios Vehículos",
                subcategories = listOf(
                    Subcategory("Repuestos Autos", icon = Icons.Outlined.DirectionsCar),
                    Subcategory("Llantas y Rines", icon = Icons.Outlined.TireRepair),
                    Subcategory("Audio Autos", icon = Icons.Outlined.Speaker),
                    Subcategory("Cascos Moto", icon = Icons.Outlined.TwoWheeler),
                    Subcategory("Herramientas", icon = Icons.Outlined.Build),
                    Subcategory("Tuning y Acc.", icon = Icons.Outlined.Tune)
                )
            ),
            CategoryGroup(
                name = "Muebles y decoración",
                subcategories = listOf(
                    Subcategory("Muebles Hogar", icon = Icons.Outlined.Chair),
                    Subcategory("Iluminación", icon = Icons.Outlined.Light),
                    Subcategory("Decoración", imageRes = R.drawable.adorno),
                    Subcategory("Muebles Oficina", icon = Icons.Outlined.Desk),
                    Subcategory("Jardín Exterior", icon = Icons.Outlined.Yard),
                    Subcategory("Cuadros", icon = Icons.Outlined.Image)
                )
            ),
            CategoryGroup(
                name = "Celulares y Télefonos",
                subcategories = listOf(
                    Subcategory("Smartphones", imageRes = R.drawable.celular),
                    Subcategory("Fundas y Casos", icon = Icons.Outlined.Smartphone),
                    Subcategory("Cargadores", icon = Icons.Outlined.Power),
                    Subcategory("Protectores", icon = Icons.Outlined.Shield),
                    Subcategory("Smartwatches", imageRes = R.drawable.reloj),
                    Subcategory("Repuestos", icon = Icons.Outlined.Build)
                )
            ),
            CategoryGroup(
                name = "Construcción",
                subcategories = listOf(
                    Subcategory("Herramientas", imageRes = R.drawable.contruccion),
                    Subcategory("Ferretería", icon = Icons.Outlined.Build),
                    Subcategory("Pisos y Paredes", icon = Icons.Outlined.GridView),
                    Subcategory("Pinturas", icon = Icons.Outlined.FormatPaint),
                    Subcategory("Iluminación", icon = Icons.Outlined.Light),
                    Subcategory("Plomería", icon = Icons.Outlined.WaterDamage)
                )
            ),
            CategoryGroup(
                name = "Computación",
                subcategories = listOf(
                    Subcategory("Laptops PC", icon = Icons.Outlined.Laptop),
                    Subcategory("Componentes", icon = Icons.Outlined.Memory),
                    Subcategory("Monitores", icon = Icons.Outlined.Monitor),
                    Subcategory("Impresoras", icon = Icons.Outlined.Print),
                    Subcategory("Teclados y Mouses", icon = Icons.Outlined.Keyboard),
                    Subcategory("Almacenamiento", icon = Icons.Outlined.Usb),
                    Subcategory("Redes WiFi", icon = Icons.Outlined.Wifi),
                    Subcategory("Accesorios PC", icon = Icons.Outlined.Headphones)
                )
            ),
            CategoryGroup(
                name = "Electrodomésticos",
                subcategories = listOf(
                    Subcategory("Televisores", imageRes = R.drawable.televisor),
                    Subcategory("Refrigeración", icon = Icons.Outlined.Kitchen),
                    Subcategory("Lavadoras", icon = Icons.Outlined.LocalLaundryService),
                    Subcategory("Electros Pequeños", imageRes = R.drawable.tasa),
                    Subcategory("Climatización", icon = Icons.Outlined.AcUnit),
                    Subcategory("Microondas", icon = Icons.Outlined.Microwave)
                )
            ),
            CategoryGroup(
                name = "Belleza y Salud",
                subcategories = listOf(
                    Subcategory("Cuidado Facial", icon = Icons.Outlined.Face),
                    Subcategory("Maquillaje", icon = Icons.Outlined.Brush),
                    Subcategory("Perfumes", icon = Icons.Outlined.CleanHands),
                    Subcategory("Cuidado Capilar", icon = Icons.Outlined.ContentCut),
                    Subcategory("Salud y Médicos", icon = Icons.Outlined.MedicalServices),
                    Subcategory("Barbería", icon = Icons.Outlined.FaceRetouchingNatural)
                )
            ),
            CategoryGroup(
                name = "Moda y Accesorios",
                subcategories = listOf(
                    Subcategory("Ropa Hombre", imageRes = R.drawable.modas),
                    Subcategory("Ropa Mujer", imageRes = R.drawable.modas),
                    Subcategory("Calzado", imageRes = R.drawable.zapatillas),
                    Subcategory("Bolsos y Carteras", icon = Icons.Outlined.ShoppingBag),
                    Subcategory("Lentes de Sol", icon = Icons.Outlined.Visibility),
                    Subcategory("Relojes", imageRes = R.drawable.reloj)
                )
            ),
            CategoryGroup(
                name = "Deportes y Fitness",
                subcategories = listOf(
                    Subcategory("Fitness y Pesas", icon = Icons.Outlined.FitnessCenter),
                    Subcategory("Bicicletas", icon = Icons.Outlined.PedalBike),
                    Subcategory("Camping Pesca", icon = Icons.Outlined.Terrain),
                    Subcategory("Ropa Deportiva", imageRes = R.drawable.zapatillas),
                    Subcategory("Pelotas", icon = Icons.Outlined.SportsSoccer),
                    Subcategory("Deportes Múltiples", icon = Icons.Outlined.SportsBasketball)
                )
            ),
            CategoryGroup(
                name = "Audio y Video",
                subcategories = listOf(
                    Subcategory("Audífonos", imageRes = R.drawable.audifinos),
                    Subcategory("Parlantes", icon = Icons.Outlined.Speaker),
                    Subcategory("Audio Equipos", icon = Icons.Outlined.Headphones),
                    Subcategory("Proyectores", icon = Icons.Outlined.Videocam),
                    Subcategory("Micrófonos", icon = Icons.Outlined.Mic),
                    Subcategory("Cables y Audio", icon = Icons.Outlined.Cable)
                )
            ),
            CategoryGroup(
                name = "Oficina y Papeleria",
                subcategories = listOf(
                    Subcategory("Papelería", icon = Icons.AutoMirrored.Outlined.MenuBook),
                    Subcategory("Sillas Escritorio", icon = Icons.Outlined.Chair),
                    Subcategory("Papel Impresión", icon = Icons.Outlined.Description),
                    Subcategory("Agendas Cuadernos", icon = Icons.Outlined.Book),
                    Subcategory("Calculadoras", icon = Icons.Outlined.Calculate),
                    Subcategory("Organizadores", icon = Icons.Outlined.Folder)
                )
            ),
            CategoryGroup(
                name = "Videojuegos",
                subcategories = listOf(
                    Subcategory("Consolas", icon = Icons.Outlined.SportsEsports),
                    Subcategory("Juegos PlayStation", icon = Icons.Outlined.Gamepad),
                    Subcategory("Juegos Xbox", icon = Icons.Outlined.SportsEsports),
                    Subcategory("Juegos Nintendo", icon = Icons.Outlined.Gamepad),
                    Subcategory("Sillas Gamer", icon = Icons.Outlined.Chair),
                    Subcategory("Controles", icon = Icons.Outlined.Gamepad)
                )
            ),
            CategoryGroup(
                name = "Cámaras y Accesorios",
                subcategories = listOf(
                    Subcategory("Acc. Cámaras", icon = Icons.Outlined.CameraAlt),
                    Subcategory("Cámaras", icon = Icons.Outlined.PhotoCamera),
                    Subcategory("Lentes y Filtros", icon = Icons.Outlined.Camera),
                    Subcategory("Cámaras de Video", icon = Icons.Outlined.Videocam),
                    Subcategory("Inst. Ópticos", icon = Icons.Outlined.Visibility),
                    Subcategory("Drones y Accesorios", icon = Icons.Outlined.Air),
                    Subcategory("Álbumes y Portarretrat...", icon = Icons.Outlined.CropOriginal),
                    Subcategory("Equipo de Revelado", icon = Icons.Outlined.LocalSee),
                    Subcategory("Cables", icon = Icons.Outlined.Cable),
                    Subcategory("Rep. Cámaras", icon = Icons.Outlined.Build)
                )
            ),
            CategoryGroup(
                name = "Juegos y Juguetes",
                subcategories = listOf(
                    Subcategory("Muñecos Figuras", icon = Icons.Outlined.Toys),
                    Subcategory("Juegos de Mesa", icon = Icons.Outlined.Casino),
                    Subcategory("Autos Juguete", icon = Icons.Outlined.DirectionsCar),
                    Subcategory("Peluches", icon = Icons.Outlined.Face),
                    Subcategory("Bloques Armables", icon = Icons.Outlined.Category),
                    Subcategory("Bebés Juguetes", icon = Icons.Outlined.ChildCare)
                )
            ),
            CategoryGroup(
                name = "Alimento y bebidas",
                subcategories = listOf(
                    Subcategory("Supermercado", imageRes = R.drawable.supermercado),
                    Subcategory("Bebidas y Vinos", icon = Icons.Outlined.WineBar),
                    Subcategory("Snacks Dulces", icon = Icons.Outlined.Fastfood),
                    Subcategory("Café y Té", icon = Icons.Outlined.Coffee),
                    Subcategory("Granos Despensa", icon = Icons.Outlined.ShoppingBasket),
                    Subcategory("Orgánicos", icon = Icons.Outlined.Eco)
                )
            ),
            CategoryGroup(
                name = "Bebés",
                subcategories = listOf(
                    Subcategory("Coches Paseo", icon = Icons.Outlined.ChildFriendly),
                    Subcategory("Pañales Aseo", icon = Icons.Outlined.CleanHands),
                    Subcategory("Ropa Bebé", icon = Icons.Outlined.Checkroom),
                    Subcategory("Lactancia", icon = Icons.Outlined.BabyChangingStation),
                    Subcategory("Cunas Dormitorio", icon = Icons.Outlined.Bed),
                    Subcategory("Juguetes Bebés", icon = Icons.Outlined.ChildCare)
                )
            ),
            CategoryGroup(
                name = "Mascotas",
                subcategories = listOf(
                    Subcategory("Alimentos Perros", icon = Icons.Outlined.Pets),
                    Subcategory("Alimentos Gatos", icon = Icons.Outlined.Pets),
                    Subcategory("Accesorios Mascota", icon = Icons.Outlined.SportsCricket),
                    Subcategory("Camas y Casitas", icon = Icons.Outlined.Home),
                    Subcategory("Higiene Aseo", icon = Icons.Outlined.Bathtub),
                    Subcategory("Juguetes Mascota", icon = Icons.Outlined.SmartToy)
                )
            ),
            CategoryGroup(
                name = "Instrumentos",
                subcategories = listOf(
                    Subcategory("Guitarras Bajos", icon = Icons.Outlined.MusicNote),
                    Subcategory("Teclados Pianos", icon = Icons.Outlined.Piano),
                    Subcategory("Baterías Percusión", icon = Icons.AutoMirrored.Outlined.QueueMusic),
                    Subcategory("Micrófonos", icon = Icons.Outlined.Mic),
                    Subcategory("Audio Prof.", icon = Icons.Outlined.GraphicEq),
                    Subcategory("Accesorios Música", icon = Icons.Outlined.MusicVideo)
                )
            ),
            CategoryGroup(
                name = "Joyas y Relojes",
                subcategories = listOf(
                    Subcategory("Relojes Pulsera", imageRes = R.drawable.reloj),
                    Subcategory("Anillos Cadenas", icon = Icons.Outlined.Diamond),
                    Subcategory("Aretes Dijes", icon = Icons.Outlined.Grade),
                    Subcategory("Pulseras", icon = Icons.Outlined.Watch),
                    Subcategory("Joyería Fina", icon = Icons.Outlined.Star),
                    Subcategory("Estuches Joyas", icon = Icons.Outlined.CardGiftcard)
                )
            ),
            CategoryGroup(
                name = "Vehículos",
                subcategories = listOf(
                    Subcategory("Autos Usados", icon = Icons.Outlined.DirectionsCar),
                    Subcategory("Camionetas SUV", icon = Icons.Outlined.DirectionsCar),
                    Subcategory("Motos", icon = Icons.Outlined.TwoWheeler),
                    Subcategory("Camiones Buses", icon = Icons.Outlined.AirportShuttle),
                    Subcategory("Maquinaria", icon = Icons.Outlined.PrecisionManufacturing),
                    Subcategory("Náutica", icon = Icons.Outlined.Sailing)
                )
            ),
            CategoryGroup(
                name = "Inmuebles",
                subcategories = listOf(
                    Subcategory("Departamentos Venta", icon = Icons.Outlined.Apartment),
                    Subcategory("Casas Alquiler", icon = Icons.Outlined.House),
                    Subcategory("Terrenos Lotes", icon = Icons.Outlined.Landscape),
                    Subcategory("Locales Comerciales", icon = Icons.Outlined.Storefront),
                    Subcategory("Oficinas", icon = Icons.Outlined.Business),
                    Subcategory("Estacionamientos", icon = Icons.Outlined.LocalParking)
                )
            ),
            CategoryGroup(
                name = "Servicios",
                subcategories = listOf(
                    Subcategory("Mantenimiento Hogar", icon = Icons.Outlined.HomeRepairService),
                    Subcategory("Clases Cursos", icon = Icons.Outlined.School),
                    Subcategory("Fiestas Eventos", icon = Icons.Outlined.Celebration),
                    Subcategory("Diseño Web", icon = Icons.Outlined.Computer),
                    Subcategory("Transporte Fletes", icon = Icons.Outlined.LocalShipping),
                    Subcategory("Belleza Domicilio", icon = Icons.Outlined.ContentCut)
                )
            ),
            CategoryGroup(
                name = "Otras categorías",
                subcategories = listOf(
                    Subcategory("Coleccionables", icon = Icons.Outlined.MilitaryTech),
                    Subcategory("Arte Antigüedades", icon = Icons.Outlined.Palette),
                    Subcategory("Libros Revistas", icon = Icons.Outlined.MenuBook),
                    Subcategory("Industria Comercio", icon = Icons.Outlined.Domain),
                    Subcategory("Cotillón Fiestas", icon = Icons.Outlined.Celebration),
                    Subcategory("Varios", icon = Icons.Outlined.Widgets)
                )
            )
        )
    }

    // Default selection is 12 -> "Cámaras y Accesorios" (matching screenshot)
    var selectedCategoryIndex by remember { mutableIntStateOf(12) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Categorías",
                        fontWeight = FontWeight.Bold,
                        color = MLBlack,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = MLBlack
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MLYellow)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Inicio", fontSize = 10.sp) },
                    selected = false,
                    onClick = onNavigateToHome
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.GridView, contentDescription = null, tint = MLBlue) },
                    label = { Text("Categorías", color = MLBlue, fontSize = 10.sp) },
                    selected = true,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.ShoppingCart, contentDescription = null) },
                    label = { Text("Carrito", fontSize = 10.sp) },
                    selected = false,
                    onClick = onNavigateToPurchases
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.PlayCircle, contentDescription = null) },
                    label = { Text("Videos", fontSize = 10.sp) },
                    selected = false,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Menu, contentDescription = null) },
                    label = { Text("Más", fontSize = 10.sp) },
                    selected = false,
                    onClick = onNavigateToProfile
                )
            }
        }
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MLBackground)
        ) {
            // Left Sidebar: Categories List
            LazyColumn(
                modifier = Modifier
                    .width(130.dp)
                    .fillMaxHeight()
                    .background(Color(0xFFF3F3F5))
            ) {
                itemsIndexed(categories) { index, category ->
                    val isSelected = index == selectedCategoryIndex
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp)
                            .background(if (isSelected) Color.White else Color(0xFFF3F3F5))
                            .clickable { selectedCategoryIndex = index },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .width(4.dp)
                                        .fillMaxHeight()
                                        .background(MLBlue)
                                )
                            } else {
                                Spacer(modifier = Modifier.width(4.dp))
                            }

                            Text(
                                text = category.name,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MLBlue else Color(0xFF333333),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFE5E5E5))
                }
            }

            // Right Panel: Subcategories Grid
            val currentCategory = categories.getOrNull(selectedCategoryIndex) ?: categories.first()
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color.White)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(currentCategory.subcategories) { subcategory ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(58.dp)
                                .clip(CircleShape),
                            color = Color(0xFFF4F6FB),
                            shape = CircleShape
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (subcategory.imageRes != null) {
                                    Image(
                                        painter = painterResource(id = subcategory.imageRes),
                                        contentDescription = subcategory.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                    )
                                } else if (subcategory.icon != null) {
                                    Icon(
                                        imageVector = subcategory.icon,
                                        contentDescription = subcategory.name,
                                        tint = Color(0xFF4A4A4A),
                                        modifier = Modifier.size(28.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Outlined.Category,
                                        contentDescription = subcategory.name,
                                        tint = Color(0xFF4A4A4A),
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = subcategory.name,
                            fontSize = 11.sp,
                            color = Color(0xFF333333),
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 13.sp
                        )
                    }
                }
            }
        }
    }
}
