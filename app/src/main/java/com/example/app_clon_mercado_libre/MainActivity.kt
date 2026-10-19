package com.example.app_clon_mercado_libre

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.app_clon_mercado_libre.ui.screens.*
import com.example.app_clon_mercado_libre.ui.theme.App_clon_mercado_libreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App_clon_mercado_libreTheme {
                MainNavigation()
            }
        }
    }
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateToPassword = { email ->
                    navController.navigate("password/$email")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToVerification = { email ->
                    navController.navigate("verification/$email")
                }
            )
        }
        composable(
            route = "verification/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            VerificationScreen(
                email = email,
                onConfirmCode = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "password/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            PasswordScreen(
                email = email,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("home") {
            HomeScreen(
                onNavigateToDetail = { productId ->
                    navController.navigate("productDetail/$productId")
                },
                onNavigateToFavorites = { navController.navigate("favorites") },
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToPurchases = { navController.navigate("purchases") },
                onNavigateToCategories = { navController.navigate("categories") },
                onNavigateToCart = { navController.navigate("cart") }
            )
        }
        composable("favorites") {
            FavoritesScreen(
                onNavigateToDetail = { productId ->
                    navController.navigate("productDetail/$productId")
                },
                onNavigateToHome = {
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                },
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToPurchases = { navController.navigate("purchases") },
                onNavigateToCategories = { navController.navigate("categories") },
                onNavigateToCart = { navController.navigate("cart") }
            )
        }
        composable("profile") {
            ProfileScreen(
                onNavigateToHome = {
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                },
                onNavigateToFavorites = { navController.navigate("favorites") },
                onNavigateToPurchases = { navController.navigate("purchases") },
                onNavigateToCategories = { navController.navigate("categories") },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToPublish = { navController.navigate("publish") },
                onNavigateToMyProducts = { navController.navigate("myProducts") },
                onNavigateToCart = { navController.navigate("cart") }
            )
        }
        composable("categories") {
            CategoriesScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToHome = { navController.navigate("home") { popUpTo("home") { inclusive = true } } },
                onNavigateToPurchases = { navController.navigate("purchases") },
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToCart = { navController.navigate("cart") }
            )
        }
        composable("purchases") {
            PurchasesScreen(
                onNavigateToDetail = { productId ->
                    navController.navigate("productDetail/$productId")
                },
                onNavigateToHome = {
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                },
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToFavorites = { navController.navigate("favorites") },
                onNavigateToPurchases = { },
                onNavigateToCategories = { navController.navigate("categories") },
                onNavigateToCart = { navController.navigate("cart") }
            )
        }
        composable("cart") {
            CartScreen(
                onNavigateToDetail = { productId ->
                    navController.navigate("productDetail/$productId")
                },
                onNavigateToHome = {
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                },
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToCategories = { navController.navigate("categories") },
                onNavigateToCheckout = { navController.navigate("checkout") }
            )
        }
        composable(
            route = "productDetail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            ProductDetailScreen(
                productId = productId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToFavorites = { navController.navigate("favorites") },
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToAddress = { navController.navigate("address") },
                onNavigateToPurchases = { navController.navigate("purchases") },
                onNavigateToCategories = { navController.navigate("categories") },
                onNavigateToCart = { navController.navigate("cart") }
            )
        }
        composable("address") {
            AddressScreen(
                onNavigateToCheckout = { navController.navigate("checkout") },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("checkout") {
            CheckoutScreen(
                onPurchaseSuccess = {
                    navController.navigate("purchases") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                onNavigateBack = { navController.popBackStack() },
                onNavigateToHome = { navController.navigate("home") },
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToFavorites = { navController.navigate("favorites") },
                onNavigateToPurchases = { navController.navigate("purchases") },
                onNavigateToCategories = { navController.navigate("categories") },
                onNavigateToCart = { navController.navigate("cart") }
            )
        }
        composable("publish") {
            PublishProductScreen(
                onNavigateBack = { navController.popBackStack() },
                onPublishSuccess = {
                    navController.navigate("myProducts") {
                        popUpTo("profile")
                    }
                }
            )
        }
        composable("myProducts") {
            MyProductsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { productId ->
                    navController.navigate("editProduct/$productId")
                },
                onNavigateToPublish = { navController.navigate("publish") }
            )
        }
        composable(
            route = "editProduct/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            EditProductScreen(
                productId = productId,
                onNavigateBack = { navController.popBackStack() },
                onSaved = {
                    navController.navigate("myProducts") {
                        popUpTo("myProducts") { inclusive = true }
                    }
                }
            )
        }
    }
}
