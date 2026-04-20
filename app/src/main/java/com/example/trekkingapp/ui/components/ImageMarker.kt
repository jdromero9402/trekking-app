package com.example.trekkingapp.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.exifinterface.media.ExifInterface
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

fun fixBitmapRotation(bitmap: Bitmap, uri: Uri, context: Context): Bitmap {
    val inputStream = context.contentResolver.openInputStream(uri) ?: return bitmap
    val exif = ExifInterface(inputStream)
    inputStream.close()

    val rotation = when (
        exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
    ) {
        ExifInterface.ORIENTATION_ROTATE_90  -> 90f
        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
        else -> 0f
    }

    if (rotation == 0f) return bitmap

    val matrix = Matrix().apply { postRotate(rotation) }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}
@Composable
fun bitmapDescriptorFromUri(uri: Uri, size: Int = 120): BitmapDescriptor? {
    val context = LocalContext.current
    return remember(uri) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val rotated = fixBitmapRotation(bitmap, uri, context)
            val circular = makeCircularBitmap(rotated, size)

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

