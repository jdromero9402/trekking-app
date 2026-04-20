package com.example.trekkingapp.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberUpdatedMarkerState
import androidx.core.graphics.scale
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun bitmapDescriptorFromUri(uri: Uri, size: Int = 120): BitmapDescriptor? {
    val context = LocalContext.current
    return remember(uri) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            val scaled = bitmap.scale(size, size)
            BitmapDescriptorFactory.fromBitmap(scaled)
        } catch (e: Exception) {
            Log.e("Marker", "Error cargando imagen", e)
            null
        }
    }
}

@Composable
fun ImageMarker (modifier: Modifier = Modifier, position: LatLng, photoUri: Uri) {
    Log.i("Marker", photoUri.toString())

    Log.d("Marker", "URI recibido: $photoUri")
    Log.d("Marker", "URI scheme: ${photoUri.scheme}")
    Log.d("Marker", "URI host: ${photoUri.host}")
    Log.d("Marker", "Clase real: ${photoUri::class.java.name}")

    val icon = bitmapDescriptorFromUri(photoUri)
    Marker(
        state = rememberUpdatedMarkerState(position = position),
        icon = icon
    )
}

