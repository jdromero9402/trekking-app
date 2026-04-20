package com.example.trekkingapp.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trekkingapp.R
import com.example.trekkingapp.ui.components.dataclass.PhotoLocation
import com.example.trekkingapp.viewmodels.LocationViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapComponent(
    modifier: Modifier = Modifier,
    permission: PermissionState,
    locationViewModel: LocationViewModel = viewModel(),
    photos: List<PhotoLocation>,
    onClearPhotos: () -> Unit
){
    val locationState by locationViewModel.state.collectAsState()

    val context = LocalContext.current

    val zoomLevel = 15f
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isBuildingEnabled = true,
                isTrafficEnabled = false,
                mapType = MapType.NORMAL,
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                    context,
                    R.raw.map_day
                )
            )
        )
    }

    val cameraPositionState = rememberCameraPositionState {

        position = CameraPosition.fromLatLngZoom(LatLng(locationState.lastPos?.latitude ?: 0.0,
            locationState.lastPos?.longitude ?: 0.0
        ), zoomLevel)
        Log.i("MiTag", locationState.lastPos.toString())
    }

    LaunchedEffect(locationState.lastPos) {
        locationState.lastPos?.let {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition(
                        it,
                        cameraPositionState.position.zoom,
                        cameraPositionState.position.tilt,
                        cameraPositionState.position.bearing
                    )
                ), durationMs = 1000
            )
        }
    }

    fun handleRecording(){
        if (locationState.isRecording) {
            locationViewModel.stopLocationUpdates()
        }else{
            locationViewModel.startRecordingLocation()
        }

    }

    Box {
        if (permission.status.isGranted){
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    zoomGesturesEnabled = true,
                    mapToolbarEnabled = false,
                    compassEnabled = true,
                )
            )
            {
                Route(points = photos)

                locationState.lastPos?.let {
                    Marker(
                        state = rememberUpdatedMarkerState(locationState.lastPos!!),
                        title = "Punto inicial",
                        snippet = "Marcador en el Aeropuerto",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                    )
                }


                Polyline(
                    clickable = true,
                    points = locationState.route,
                    color = MaterialTheme.colorScheme.primary,
                    width = 15f,
                    startCap = RoundCap(),
                    endCap = RoundCap(),
                )
            }
            Row(modifier.align(Alignment.TopEnd).padding(20.dp)) {
                if (!photos.isEmpty()){
                    FloatingActionButton(
                        modifier = Modifier.size(64.dp),
                        onClick = {
                            locationViewModel.clearRoute()
                            onClearPhotos()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                        )
                    }

                    Spacer(Modifier.size(1.dp))
                }
                PlayStopButton(recording = locationState.isRecording, onClick = { handleRecording() }, photoCount = photos.size)
            }
        } else {
            if (permission.status.shouldShowRationale) {
                Text(stringResource(R.string.location_rationale_label))
                Button(onClick = { permission.launchPermissionRequest() }) {
                    Text(stringResource(R.string.location_request_button_label))
                }
            } else {
                ErrorMessage(message = R.string.location_no_permission_label,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(
                            Alignment.Center
                        ))
            }
        }
    }
}