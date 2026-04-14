package com.example.trekkingapp.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.camera.core.ImageCapture
import androidx.core.content.FileProvider
import java.io.File

fun createTempImageFileInInternalPicturesFolder(name:String, context: Context): Uri {
    val tmpFile = File.createTempFile(
        name,
        ".jpg",
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    )

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        tmpFile
    )
}

fun createImageOnPhotosFolder(name:String, context: Context): ImageCapture.OutputFileOptions {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, name)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }

    return ImageCapture.OutputFileOptions.Builder(
        context.contentResolver,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    ).build()
}

fun saveImgBothPlaces(name:String, context: Context): ImageCapture.OutputFileOptions {
    createTempImageFileInInternalPicturesFolder(name, context)
    return createImageOnPhotosFolder(name, context)
}
