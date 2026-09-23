package com.example.app_clon_mercado_libre.data

import com.example.app_clon_mercado_libre.R

import androidx.compose.runtime.mutableStateListOf

data class Product(
    val id: String,
    val name: String,
    val currentPrice: String,
    val oldPrice: String,
    val imageRes: Int,
    val category: String,
    val tag: String = "OFERTA",
    val delivery: String = "Llega hoy",
    val freeShipping: Boolean = true
)

object ProductRepository {
    val favorites = mutableStateListOf<String>()
    val purchases = mutableStateListOf<Product>()

    val products = listOf(
        Product(
            id = "1",
            name = "Smartphone 128GB 8GB RAM",
            currentPrice = "S/ 145.999",
            oldPrice = "S/ 189.999",
            imageRes = R.drawable.celular,
            category = "Tecnología",
            delivery = "Llega mañana"
        ),
        Product(
            id = "2",
            name = "Wireless Headphones Noise Cancelling",
            currentPrice = "S/ 32.499",
            oldPrice = "S/ 45.000",
            imageRes = R.drawable.audifinos,
            category = "Tecnología"
        ),
        Product(
            id = "3",
            name = "Zapatillas Running Pro Series Advance",
            currentPrice = "S/ 89.900",
            oldPrice = "S/ 120.000",
            imageRes = R.drawable.zapatillas,
            category = "Moda",
            delivery = "Llega mañana"
        ),
        Product(
            id = "4",
            name = "Smart TV 55\" 4K UHD Ultra Slim",
            currentPrice = "S/ 450.000",
            oldPrice = "S/ 580.000",
            imageRes = R.drawable.televisor,
            category = "Tecnología"
        ),
        Product(
            id = "5",
            name = "Smartphone 5G Pro Max",
            currentPrice = "S/ 2.450",
            oldPrice = "S/ 2.999",
            imageRes = R.drawable.smartphone5g,
            category = "Tecnología",
            delivery = "Llega hoy"
        ),
        Product(
            id = "6",
            name = "Reloj Inteligente Sport Pro",
            currentPrice = "S/ 199",
            oldPrice = "S/ 250",
            imageRes = R.drawable.reloj,
            category = "Tecnología",
            delivery = "Llega mañana"
        ),
        Product(
            id = "7",
            name = "Taza de Cerámica Diseño Especial",
            currentPrice = "S/ 35",
            oldPrice = "S/ 45",
            imageRes = R.drawable.tasa,
            category = "Hogar",
            delivery = "Llega hoy"
        ),
        Product(
            id = "8",
            name = "Adorno Moderno Sala de Estar",
            currentPrice = "S/ 120",
            oldPrice = "S/ 160",
            imageRes = R.drawable.adorno,
            category = "Hogar",
            delivery = "Llega mañana"
        )
    )

    fun getProductById(id: String?) = products.find { it.id == id }
}
