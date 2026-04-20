package com.example.trekkingapp.ui.components

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trekkingapp.R
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
import androidx.core.net.toUri

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapComponent(modifier: Modifier = Modifier, permission: PermissionState, Homecontext: Context, locationViewModel: LocationViewModel = viewModel()){
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

//        locationState.lastPos?.let { position = CameraPosition.fromLatLngZoom( it, zoomLevel) }
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
                locationState.lastPos?.let {
                    ImageMarker(position = it, photoUri = "content://media/external/images/media/1000776015".toUri())
                 }
                //Draw POIs
//                poiList.forEach { pos ->
//                    //Custom Marker with vector
//                    Marker(
//                        state = rememberUpdatedMarkerState(pos),
//                        title = "${"%.2f".format(pos.latitude)}, ${"%.2f".format(pos.longitude)}",
//                        snippet = "Aqui paso algo feo :(",
//                        anchor = Offset(0.5f, 1f),
//                        icon = createBitmapDescriptor(context, R.drawable.poi_marker)
//                    )
//                }
                //Draw Events
//                eventList.forEach { pos ->
//                    Marker(
//                        state = rememberUpdatedMarkerState(pos),
//                        title = "${"%.2f".format(pos.latitude)}, ${"%.2f".format(pos.longitude)}",
//                        snippet = "Un evento sobre el mapa",
//                        anchor = Offset(0.5f, 0.5f),
//                        icon = createBitmapDescriptor(context, R.drawable.tee_marker)
//                    )
//                }
                //Draw Route
                Polyline(
                    clickable = true,
                    points = locationState.route,
                    color = MaterialTheme.colorScheme.primary,
                    width = 15f,
                    startCap = RoundCap(),
                    endCap = RoundCap(),
                )
                //Draw current location
                locationState.lastPos?.let {
//                    Marker(
//                        state = rememberUpdatedMarkerState(LatLng(it.latitude, it.longitude)),
//                        title = "Current Location",
//                        snippet = "Speed: ${"%.2f".format(locationState.speed * 3.6)} km/h",
//                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
//                    )
                    /**
                    MarkerComposable(
                    state = rememberUpdatedMarkerState(locationState.lastPos!!),
                    anchor = Offset(0.5f, 0.5f),
                    zIndex = 1f
                    ) {
                    Icon(
                    imageVector = Icons.Rounded.MyLocation,
                    contentDescription = null,
                    Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary
                    )
                    }*/
                }
            }
        } else {
            if (permission.status.shouldShowRationale) {
                Text(stringResource(R.string.location_rationale_label))
                Button(onClick = { permission.launchPermissionRequest() }) {
                    Text(stringResource(R.string.location_request_button_label))
                }
            } else {
                ErrorMessage(message = R.string.location_no_permission_label,
                    modifier = Modifier.fillMaxSize().align(
                        Alignment.Center
                    ))
            }
        }
    }
}