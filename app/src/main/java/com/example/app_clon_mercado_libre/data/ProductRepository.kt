package com.example.app_clon_mercado_libre.data

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ProductRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth get() = FirebaseAuth.getInstance()
    private val uid get() = auth.currentUser?.uid

    // ---------- Catálogo (RF04 / RF06) ----------
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val favorites = mutableStateListOf<String>()
    val purchases = mutableStateListOf<Product>()

    fun loadActiveProducts(forceRefresh: Boolean = false) {
        if (_isLoading.value) return
        if (!forceRefresh && _products.value.isNotEmpty()) return
        _isLoading.value = true
        _error.value = null
        db.collection("products")
            .whereEqualTo("active", true)
            .get()
            .addOnSuccessListener { snapshot ->
                _products.value = snapshot.documents.mapNotNull { it.toProduct() }
                _isLoading.value = false
            }
            .addOnFailureListener { e ->
                _error.value = e.message ?: "Error al cargar productos"
                _isLoading.value = false
            }
    }

    fun getProductById(id: String?, onResult: (Product?) -> Unit) {
        if (id.isNullOrBlank()) {
            onResult(null)
            return
        }
        _products.value.find { it.id == id }?.let {
            onResult(it)
            return
        }
        db.collection("products").document(id).get()
            .addOnSuccessListener { doc -> onResult(doc.toProduct()) }
            .addOnFailureListener { onResult(null) }
    }

    fun searchProducts(query: String): List<Product> {
        val q = query.trim()
        return _products.value.filter { matchesQuery(it, q) }
    }

    fun matchesQuery(product: Product, query: String): Boolean {
        val q = query.trim()
        if (q.isEmpty()) return true
        return product.name.contains(q, ignoreCase = true) ||
                product.category.contains(q, ignoreCase = true) ||
                product.searchKeywords.any { it.contains(q, ignoreCase = true) }
    }

    fun toggleFavorite(productId: String) {
        if (favorites.contains(productId)) favorites.remove(productId) else favorites.add(productId)
    }

    // ---------- Carrito (RF07) ----------
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _cartLoading = MutableStateFlow(false)
    val cartLoading: StateFlow<Boolean> = _cartLoading

    fun loadCart() {
        val userId = uid ?: return
        _cartLoading.value = true
        db.collection("carts").document(userId).collection("items")
            .get()
            .addOnSuccessListener { snapshot ->
                _cartItems.value = snapshot.documents.mapNotNull { it.toCartItem() }
                _cartLoading.value = false
            }
            .addOnFailureListener { _cartLoading.value = false }
    }

    fun addToCart(product: Product, quantity: Int, onResult: ((String?) -> Unit)? = null) {
        val userId = uid
        if (userId == null) {
            onResult?.invoke("Debes iniciar sesión para comprar")
            return
        }
        if (product.stock <= 0) {
            onResult?.invoke("Sin stock disponible")
            return
        }
        val qty = quantity.coerceIn(1, product.stock)
        val existing = _cartItems.value.find { it.productId == product.id }
        val newQty = ((existing?.quantity ?: 0) + qty).coerceAtMost(product.stock)
        val item = CartItem(
            productId = product.id,
            name = product.name,
            price = product.price,
            imageUrl = product.imageUrl,
            quantity = newQty,
            stock = product.stock,
            sellerId = product.sellerId
        )
        db.collection("carts").document(userId).collection("items")
            .document(product.id)
            .set(item.toMap())
            .addOnSuccessListener {
                loadCart()
                onResult?.invoke(null)
            }
            .addOnFailureListener { e ->
                onResult?.invoke(e.message ?: "Error al agregar al carrito")
            }
    }

    fun updateCartQuantity(productId: String, newQuantity: Int, onResult: ((String?) -> Unit)? = null) {
        val userId = uid ?: return
        val item = _cartItems.value.find { it.productId == productId } ?: return
        if (newQuantity <= 0) {
            removeCartItem(productId)
            return
        }
        val qty = newQuantity.coerceAtMost(item.stock)
        db.collection("carts").document(userId).collection("items")
            .document(productId)
            .update("quantity", qty)
            .addOnSuccessListener {
                loadCart()
                onResult?.invoke(null)
            }
            .addOnFailureListener { e ->
                onResult?.invoke(e.message ?: "Error al actualizar cantidad")
            }
    }

    fun removeCartItem(productId: String, onResult: ((String?) -> Unit)? = null) {
        val userId = uid ?: return
        db.collection("carts").document(userId).collection("items")
            .document(productId)
            .delete()
            .addOnSuccessListener {
                loadCart()
                onResult?.invoke(null)
            }
            .addOnFailureListener { e ->
                onResult?.invoke(e.message ?: "Error al quitar producto")
            }
    }

    fun cartTotal(): Double = _cartItems.value.sumOf { it.price * it.quantity }

    // ---------- Pedidos (RF09) ----------
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _ordersLoading = MutableStateFlow(false)
    val ordersLoading: StateFlow<Boolean> = _ordersLoading

    fun loadMyOrders() {
        val userId = uid ?: return
        _ordersLoading.value = true
        db.collection("orders")
            .whereEqualTo("buyerId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                _orders.value = snapshot.documents.mapNotNull { it.toOrder() }
                _ordersLoading.value = false
            }
            .addOnFailureListener { _ordersLoading.value = false }
    }

    fun purchaseCart(
        paymentMethod: String,
        onResult: (success: Boolean, message: String) -> Unit
    ) {
        val userId = uid
        if (userId == null) {
            onResult(false, "Debes iniciar sesión")
            return
        }
        val items = _cartItems.value
        if (items.isEmpty()) {
            onResult(false, "Tu carrito está vacío")
            return
        }
        val total = items.sumOf { it.price * it.quantity }
        val buyerName = auth.currentUser?.displayName ?: auth.currentUser?.email ?: "Comprador"
        val orderRef = db.collection("orders").document()

        db.runTransaction { transaction ->
            items.forEach { item ->
                val productRef = db.collection("products").document(item.productId)
                val snap = transaction.get(productRef)
                val stock = (snap.getLong("stock") ?: 0L).toInt()
                if (stock < item.quantity) {
                    throw Exception("Stock insuficiente para \"${item.name}\" (quedan $stock)")
                }
                transaction.update(productRef, "stock", stock - item.quantity)
                transaction.update(productRef, "updatedAt", FieldValue.serverTimestamp())
            }
            val orderItems = items.map { item ->
                hashMapOf(
                    "productId" to item.productId,
                    "name" to item.name,
                    "price" to item.price,
                    "quantity" to item.quantity
                )
            }
            transaction.set(
                orderRef,
                hashMapOf(
                    "buyerId" to userId,
                    "buyerName" to buyerName,
                    "items" to orderItems,
                    "total" to total,
                    "status" to "pendiente",
                    "paymentMethod" to paymentMethod,
                    "createdAt" to FieldValue.serverTimestamp()
                )
            )
            items.forEach { item ->
                transaction.delete(
                    db.collection("carts").document(userId).collection("items").document(item.productId)
                )
            }
        }.addOnSuccessListener {
            _cartItems.value = emptyList()
            loadMyOrders()
            loadActiveProducts(forceRefresh = true)
            onResult(true, "Compra realizada correctamente. Pedido ${orderRef.id}")
        }.addOnFailureListener { e ->
            onResult(false, e.message ?: "No se pudo completar la compra")
        }
    }

    // ---------- Publicaciones del vendedor (RF16 / RF17) ----------
    private val _myProducts = MutableStateFlow<List<Product>>(emptyList())
    val myProducts: StateFlow<List<Product>> = _myProducts

    private val _myProductsLoading = MutableStateFlow(false)
    val myProductsLoading: StateFlow<Boolean> = _myProductsLoading

    fun loadMyProducts() {
        val userId = uid ?: return
        _myProductsLoading.value = true
        db.collection("products")
            .whereEqualTo("sellerId", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                _myProducts.value = snapshot.documents.mapNotNull { it.toProduct() }
                _myProductsLoading.value = false
            }
            .addOnFailureListener { _myProductsLoading.value = false }
    }

    fun publishProduct(
        data: Map<String, Any?>,
        onResult: (success: Boolean, message: String) -> Unit
    ) {
        val userId = uid
        if (userId == null) {
            onResult(false, "Debes iniciar sesión para publicar")
            return
        }
        db.collection("products")
            .add(data + mapOf("sellerId" to userId, "createdAt" to FieldValue.serverTimestamp()))
            .addOnSuccessListener { onResult(true, "Producto publicado correctamente") }
            .addOnFailureListener { e -> onResult(false, e.message ?: "Error al publicar") }
    }

    fun updateProduct(
        productId: String,
        data: Map<String, Any?>,
        onResult: (success: Boolean, message: String) -> Unit
    ) {
        db.collection("products").document(productId)
            .update(data + mapOf("updatedAt" to FieldValue.serverTimestamp()))
            .addOnSuccessListener { onResult(true, "Producto actualizado correctamente") }
            .addOnFailureListener { e -> onResult(false, e.message ?: "Error al actualizar") }
    }

    fun deleteProduct(
        productId: String,
        onResult: (success: Boolean, message: String) -> Unit
    ) {
        db.collection("products").document(productId)
            .delete()
            .addOnSuccessListener {
                loadMyProducts()
                loadActiveProducts(forceRefresh = true)
                onResult(true, "Producto eliminado")
            }
            .addOnFailureListener { e -> onResult(false, e.message ?: "Error al eliminar") }
    }

    fun buildKeywords(name: String, category: String): List<String> {
        val words = (name.lowercase().split(Regex("\\s+")) + category.lowercase()
            .split(Regex("\\s+")))
            .filter { it.length >= 3 }
            .toMutableSet()
        // también cada fragmento del nombre con más de 3 letras (ej. "128gb")
        return words.toList()
    }

    // ---------- Mapeo ----------
    private fun DocumentSnapshot.toProduct(): Product? {
        val price = getDouble("price") ?: return null
        return Product(
            id = id,
            name = getString("name") ?: "",
            description = getString("description") ?: "",
            price = price,
            oldPrice = getDouble("oldPrice"),
            stock = (getLong("stock") ?: 0L).toInt(),
            category = getString("category") ?: "",
            images = (get("images") as? List<*>)?.mapNotNull { it as? String } ?: emptyList(),
            tag = getString("tag") ?: "OFERTA",
            delivery = getString("delivery") ?: "",
            freeShipping = getBoolean("freeShipping") ?: true,
            active = getBoolean("active") ?: true,
            sellerId = getString("sellerId") ?: "",
            sellerName = getString("sellerName") ?: "",
            sellerRating = getDouble("sellerRating") ?: 0.0,
            sellerSales = (getLong("sellerSales") ?: 0L).toInt(),
            searchKeywords = (get("searchKeywords") as? List<*>)?.mapNotNull { it as? String } ?: emptyList()
        )
    }

    private fun DocumentSnapshot.toCartItem(): CartItem? {
        val price = getDouble("price") ?: return null
        return CartItem(
            productId = getString("productId") ?: id,
            name = getString("name") ?: "",
            price = price,
            imageUrl = getString("imageUrl"),
            quantity = (getLong("quantity") ?: 1L).toInt(),
            stock = (getLong("stock") ?: 1L).toInt(),
            sellerId = getString("sellerId") ?: ""
        )
    }

    private fun DocumentSnapshot.toOrder(): Order? {
        val rawItems = get("items") as? List<*> ?: emptyList<Any>()
        val items = rawItems.mapNotNull { raw ->
            val map = raw as? Map<*, *> ?: return@mapNotNull null
            OrderItem(
                productId = map["productId"] as? String ?: "",
                name = map["name"] as? String ?: "",
                price = (map["price"] as? Double) ?: 0.0,
                quantity = ((map["quantity"] as? Long) ?: (map["quantity"] as? Double) ?: 0).toInt()
            )
        }
        return Order(
            id = id,
            buyerId = getString("buyerId") ?: "",
            buyerName = getString("buyerName") ?: "",
            items = items,
            total = getDouble("total") ?: 0.0,
            status = getString("status") ?: "pendiente",
            paymentMethod = getString("paymentMethod") ?: "",
            createdAt = getTimestamp("createdAt")
        )
    }
}
