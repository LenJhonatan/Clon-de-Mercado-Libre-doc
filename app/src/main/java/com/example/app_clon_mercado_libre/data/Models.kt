package com.example.app_clon_mercado_libre.data

import com.google.firebase.Timestamp
import java.util.Locale

data class Product(
    val id: String,
    val name: String,
    val description: String = "",
    val price: Double,
    val oldPrice: Double? = null,
    val stock: Int = 0,
    val category: String = "",
    val images: List<String> = emptyList(),
    val tag: String = "OFERTA",
    val delivery: String = "",
    val freeShipping: Boolean = true,
    val active: Boolean = true,
    val sellerId: String = "",
    val sellerName: String = "",
    val sellerRating: Double = 0.0,
    val sellerSales: Int = 0,
    val searchKeywords: List<String> = emptyList()
) {
    val currentPrice: String get() = formatPrice(price)
    val oldPriceFormatted: String get() = oldPrice?.let { formatPrice(it) } ?: ""
    val hasDiscount: Boolean get() = oldPrice != null && oldPrice > price
    val imageUrl: String? get() = images.firstOrNull()
    val hasStock: Boolean get() = stock > 0
}

data class CartItem(
    val productId: String,
    val name: String,
    val price: Double,
    val imageUrl: String?,
    val quantity: Int,
    val stock: Int,
    val sellerId: String = ""
) {
    val subtotalFormatted: String get() = formatPrice(price * quantity)

    fun toMap(): Map<String, Any> = mapOf(
        "productId" to productId,
        "name" to name,
        "price" to price,
        "imageUrl" to (imageUrl ?: ""),
        "quantity" to quantity,
        "stock" to stock,
        "sellerId" to sellerId
    )
}

data class OrderItem(
    val productId: String,
    val name: String,
    val price: Double,
    val quantity: Int
)

data class Order(
    val id: String,
    val buyerId: String,
    val buyerName: String,
    val items: List<OrderItem>,
    val total: Double,
    val status: String,
    val paymentMethod: String,
    val createdAt: Timestamp?
) {
    val totalFormatted: String get() = formatPrice(total)
    val itemsSummary: String get() =
        items.firstOrNull()?.let { first ->
            if (items.size == 1) "${first.name} (x${first.quantity})"
            else "${first.name} (x${first.quantity}) + ${items.size - 1} más"
        } ?: "Sin productos"
}

fun formatPrice(value: Double): String =
    "S/ " + String.format(Locale.US, "%,.2f", value)
