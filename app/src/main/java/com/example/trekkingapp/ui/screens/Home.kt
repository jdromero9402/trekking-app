package com.example.trekkingapp.ui.screens

import android.Manifest
import android.content.Context
import android.content.res.Configuration
import android.util.Log
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
import com.example.trekkingapp.ui.components.dataclass.PhotoLocation
import com.example.trekkingapp.ui.theme.AppTheme
import com.example.trekkingapp.viewmodels.LocationViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Home(modifier: Modifier = Modifier,
         locationViewModel: LocationViewModel = viewModel()
         ) {
    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val photos = rememberSaveable { mutableStateListOf<PhotoLocation>() }

    val permissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )



    LaunchedEffect(Unit) {
        permissionsState.launchMultiplePermissionRequest()
    }

    val cameraPermission = permissionsState.permissions
        .first { it.permission == Manifest.permission.CAMERA }

    val locationPermission = permissionsState.permissions
        .first { it.permission == Manifest.permission.ACCESS_FINE_LOCATION }

    LaunchedEffect(locationPermission.status.isGranted) {
        if (locationPermission.status.isGranted) {
            locationViewModel.setup(context)
            locationViewModel.startLocationUpdates()

        }
    }

    if (isLandscape) {
        LandscapeCameraLayout(
            photos = photos,
            cameraPermission = cameraPermission,
            locationPermission = locationPermission,
            context = context,
            locationViewModel = locationViewModel
        )
    } else {
        PortraitCameraLayout(
            photos = photos,
            cameraPermission = cameraPermission,
            locationPermission = locationPermission,
            context = context,
            locationViewModel = locationViewModel
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LandscapeCameraLayout(modifier: Modifier = Modifier,
                          photos: SnapshotStateList<PhotoLocation>,
                          cameraPermission: PermissionState,
                          locationPermission: PermissionState,
                          context: Context,
                          locationViewModel: LocationViewModel = viewModel()
) {
    Row (
        modifier.padding(20.dp),
    ) {
        OutlinedCard(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.6f)
        ) {
            MapComponent(Modifier,locationPermission,locationViewModel, photos, {photos.clear()})
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
                CameraComponent(onPhotoTaken = { uri, pos->
                    photos.add(PhotoLocation(uri, pos))
                }, permission = cameraPermission)
            }
            if ( !photos.isEmpty() ) {
                Log.d("Photo",photos.toString())
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
                        items(items = photos, key = { it.toString() }) { photoLocation ->
                            AsyncImage(model = photoLocation.photo, contentDescription = null)
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
fun PortraitCameraLayout(
    modifier: Modifier = Modifier,
    photos: SnapshotStateList<PhotoLocation>,
    cameraPermission: PermissionState,
    locationPermission: PermissionState,
    context: Context,
    locationViewModel: LocationViewModel = viewModel()) {
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
            CameraComponent(onPhotoTaken = { uri, pos->
                photos.add(PhotoLocation(uri, pos))
            }, permission = cameraPermission)
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
                    items(items = photos, key = { it.toString() }) { photoLocation ->
                        AsyncImage(model = photoLocation.photo, contentDescription = null)
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
        ) {
            MapComponent(Modifier,locationPermission, locationViewModel, photos, { photos.clear() })
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