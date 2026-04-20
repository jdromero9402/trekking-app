package com.example.trekkingapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.trekkingapp.ui.components.dataclass.PhotoLocation

@Composable
fun Route (
    modifier: Modifier = Modifier,
    points : List<PhotoLocation>
){
    for (point in points){
        point.pos?.let {
            ImageMarker(position = it, photoUri = point.photo)
        }
    }
}