package com.example.trekkingapp.ui.components.dataclass

import android.net.Uri
import com.google.android.gms.maps.model.LatLng

data class PhotoLocation(
    val photo: Uri,
    val pos: LatLng?,
    val name: String
)
