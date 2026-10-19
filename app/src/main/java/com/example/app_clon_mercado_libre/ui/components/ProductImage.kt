package com.example.app_clon_mercado_libre.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.app_clon_mercado_libre.R
import com.example.app_clon_mercado_libre.data.Product

@Composable
fun ProductImage(
    product: Product,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = product.name
) {
    val url = product.imageUrl
    if (url.isNullOrBlank()) {
        Image(
            painter = painterResource(id = R.drawable.mdl),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        AsyncImage(
            model = url,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
            placeholder = painterResource(id = R.drawable.mdl),
            error = painterResource(id = R.drawable.mdl)
        )
    }
}
