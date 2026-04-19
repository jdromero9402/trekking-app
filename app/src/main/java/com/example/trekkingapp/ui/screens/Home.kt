package com.example.trekkingapp.ui.screens

import android.Manifest
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.trekkingapp.ui.components.CameraComponent
import com.example.trekkingapp.ui.components.MapComponent
import com.example.trekkingapp.ui.theme.AppTheme
import com.example.trekkingapp.viewmodels.LocationViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Home(modifier: Modifier = Modifier,
         locationViewModel: LocationViewModel = viewModel()
         ) {
    val context = LocalContext.current


    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)
    val photos = rememberSaveable { mutableStateListOf<Uri>() }

    LaunchedEffect(null) {
        cameraPermission.launchPermissionRequest()
    }
    val locationPermission =
        rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(Unit) {
        locationPermission.launchPermissionRequest()
    }

    LaunchedEffect(locationPermission.status.isGranted) {
        if (locationPermission.status.isGranted) locationViewModel.setup(context)
    }


    if (isLandscape) {
        LandscapeCameraLayout(
            photos = photos,
            cameraPermission = cameraPermission,
            locationPermission = locationPermission
        )
    } else {
        PortraitCameraLayout(
            photos = photos,
            cameraPermission = cameraPermission,
            locationPermission = locationPermission
        )
    }


}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LandscapeCameraLayout(modifier: Modifier = Modifier,
                          photos: SnapshotStateList<Uri>,
                          cameraPermission: PermissionState,
                          locationPermission: PermissionState) {
    Row (
        modifier.padding(20.dp),
    ) {
        OutlinedCard(
            modifier = Modifier.fillMaxHeight().weight(0.6f)
        ) {
            MapComponent(Modifier,locationPermission)
        }
        Spacer(Modifier.width(8.dp))
        Column  (
            modifier.weight(0.4f),
        ){
            OutlinedCard(
                Modifier
                    .fillMaxHeight()
                    .weight(0.7f)
            ) {
                CameraComponent(onPhotoTaken = { photos.add(it) }, permission = cameraPermission)
            }
            if ( !photos.isEmpty() ) {
                Spacer(Modifier.height(8.dp))
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.3f)
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        reverseLayout = true
                    ) {
                        items(items = photos, key = { it.toString() }) { photo ->
                            AsyncImage(model = photo, contentDescription = null)
                        }
                    }
                }
            }
        }
        Spacer(Modifier.width(8.dp))

    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PortraitCameraLayout(modifier: Modifier = Modifier,
                         photos: SnapshotStateList<Uri>,
                         cameraPermission: PermissionState,
                         locationPermission: PermissionState) {
    val cameraWeight = if (photos.isEmpty()) 0.5f else 0.35f
    val galleryWeight = 0.15f
    Column(
        modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedCard(
            Modifier
                .fillMaxWidth()
                .weight(cameraWeight)
        ) {
            CameraComponent(onPhotoTaken = { photos.add(it) }, permission = cameraPermission)
        }
        Spacer(Modifier.height(8.dp))
        if ( !photos.isEmpty() ) {
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(galleryWeight)
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    reverseLayout = true
                ) {
                    items(items = photos, key = { it.toString() }) { photo ->
                        AsyncImage(model = photo, contentDescription = null)
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        OutlinedCard(
            modifier = Modifier.fillMaxWidth().weight(0.5f)
        ) {
            MapComponent(Modifier,locationPermission)
        }
    }
}

@PreviewLightDark
@Composable
fun HomePreview() {
    AppTheme() {
        Home()
    }
}