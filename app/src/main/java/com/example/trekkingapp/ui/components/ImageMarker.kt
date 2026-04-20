package com.example.trekkingapp.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberUpdatedMarkerState

fun makeCircularBitmap(bitmap: Bitmap, size: Int): Bitmap {
    val output = createBitmap(size, size)
    val canvas = Canvas(output)
    val paint = Paint().apply {
        isAntiAlias = true
    }
    val scaled = bitmap.scale(size, size, false)

    canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(scaled, 0f, 0f, paint)

    return output
}
@Composable
fun bitmapDescriptorFromUri(uri: Uri, size: Int = 120): BitmapDescriptor? {
    val context = LocalContext.current
    return remember(uri) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val circular = makeCircularBitmap(bitmap, size)

            BitmapDescriptorFactory.fromBitmap(circular)
        } catch (e: Exception) {
            Log.e("Marker", "Error cargando imagen", e)
            null
        }
    }
}

@Composable
fun ImageMarker (modifier: Modifier = Modifier, position: LatLng, photoUri: Uri) {


    val icon = bitmapDescriptorFromUri(photoUri)
    Marker(
        state = rememberUpdatedMarkerState(position = position),
        icon = icon,
    )
}

