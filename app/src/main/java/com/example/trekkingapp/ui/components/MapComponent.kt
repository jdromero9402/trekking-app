package com.example.trekkingapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.SatelliteAlt
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.trekkingapp.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PinConfig
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberUpdatedMarkerState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapComponent(modifier: Modifier = Modifier, permission: PermissionState){
    Box {
        if (permission.status.isGranted){
//            GoogleMap(
//                modifier = Modifier.fillMaxSize(),
//                cameraPositionState = cameraPositionState,
//                properties = mapProperties,
//                uiSettings = MapUiSettings(
//                    zoomControlsEnabled = true,
//                    zoomGesturesEnabled = true,
//                    mapToolbarEnabled = false,
//                    compassEnabled = true,
//                ),
//                onMapClick = { eventList.add(it) },
//                onMapLongClick = { poiList.add(it) }
//            )
//            {
//
//
//                //Markers
//                Marker(
//                    state = elDoradoMarkerState,
//                    title = "Aeropuerto El Dorado",
//                    snippet = "Marcador en el Aeropuerto",
//                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
//                )
//                //Advanced Marker with custom icon
//                AdvancedMarker(
//                    state = campinMarkerState,
//                    title = "Estadio El Campin",
//                    snippet = "Marcador de Estadio",
//                    pinConfig = PinConfig.builder()
//                        .setBackgroundColor(android.graphics.Color.BLUE)
//                        .setBorderColor(android.graphics.Color.WHITE)
//                        .build()
//                )
//                //Draggable Marker
//                Marker(
//                    state = draggableMarkerState,
//                    title = "Marcador Arrastrable",
//                    zIndex = 1f,
//                    snippet = "${"%.4f".format(draggableMarkerState.position.latitude)} - ${
//                        "%.4f".format(
//                            draggableMarkerState.position.longitude
//                        )
//                    }",
//                    draggable = true,
//                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)
//                )
//
//                //Draw POIs
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
//                //Draw Events
//                eventList.forEach { pos ->
//                    Marker(
//                        state = rememberUpdatedMarkerState(pos),
//                        title = "${"%.2f".format(pos.latitude)}, ${"%.2f".format(pos.longitude)}",
//                        snippet = "Un evento sobre el mapa",
//                        anchor = Offset(0.5f, 0.5f),
//                        icon = createBitmapDescriptor(context, R.drawable.tee_marker)
//                    )
//                }
//                //Draw Route
//                Polyline(
//                    clickable = true,
//                    points = locationState.route,
//                    color = MaterialTheme.colorScheme.primary,
//                    width = 15f,
//                    startCap = RoundCap(),
//                    endCap = RoundCap(),
//                )
//                //Draw current location
//                locationState.lastPos?.let {
//                    Marker(
//                        state = rememberUpdatedMarkerState(LatLng(it.latitude, it.longitude)),
//                        title = "Current Location",
//                        snippet = "Speed: ${"%.2f".format(locationState.speed * 3.6)} km/h",
//                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
//                    )
//                    /**
//                    MarkerComposable(
//                    state = rememberUpdatedMarkerState(locationState.lastPos!!),
//                    anchor = Offset(0.5f, 0.5f),
//                    zIndex = 1f
//                    ) {
//                    Icon(
//                    imageVector = Icons.Rounded.MyLocation,
//                    contentDescription = null,
//                    Modifier.size(18.dp),
//                    tint = MaterialTheme.colorScheme.primary
//                    )
//                    }*/
//                }
//            }
//            // Map button Bar
//            ElevatedCard(
//                Modifier
//                    .align(Alignment.TopEnd)
//                    .padding(20.dp)
//            )
//            {
//                Column {
//                    Row(
//                        Modifier.padding(4.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.ScreenRotation,
//                            contentDescription = null,
//                            modifier = Modifier.size(SwitchDefaults.IconSize),
//                        )
//                        Switch(
//                            checked = shouldRotate,
//                            onCheckedChange = { shouldRotate = it },
//                            thumbContent = if (shouldRotate) {
//                                {
//                                    Icon(
//                                        imageVector = Icons.Filled.Check,
//                                        contentDescription = null,
//                                        modifier = Modifier.size(SwitchDefaults.IconSize),
//                                    )
//                                }
//                            } else {
//                                null
//                            })
//                    }
//                    Row(
//                        Modifier.padding(4.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.MyLocation,
//                            contentDescription = null,
//                            modifier = Modifier.size(SwitchDefaults.IconSize),
//                        )
//                        Switch(
//                            checked = locationState.isRecording, onCheckedChange = {
//                                if (locationState.isRecording) locationViewModel.stopLocationUpdates()
//                                else locationViewModel.startLocationUpdates()
//                            }, thumbContent = if (locationState.isRecording) {
//                                {
//                                    Icon(
//                                        imageVector = Icons.Filled.Check,
//                                        contentDescription = null,
//                                        modifier = Modifier.size(SwitchDefaults.IconSize),
//                                    )
//                                }
//                            } else {
//                                null
//                            })
//                    }
//                    Row(
//                        Modifier.padding(4.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.SatelliteAlt,
//                            contentDescription = null,
//                            modifier = Modifier.size(SwitchDefaults.IconSize),
//                        )
//                        Switch(
//                            checked = showSat,
//                            onCheckedChange = { showSat = it },
//                            thumbContent = if (showSat) {
//                                {
//                                    Icon(
//                                        imageVector = Icons.Filled.Check,
//                                        contentDescription = null,
//                                        modifier = Modifier.size(SwitchDefaults.IconSize),
//                                    )
//                                }
//                            } else {
//                                null
//                            })
//                    }
//                }
//            }
//            //Clear Button Bar
//            ElevatedCard(
//                Modifier
//                    .align(Alignment.BottomStart)
//                    .padding(20.dp)
//            )
//            {
//                Column{
//                    IconButton(
//                        onClick = {
//                            locationViewModel.clearRoute()
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Delete,
//                            contentDescription = null,
//                            modifier = Modifier,
//                        )
//                    }
//                    IconButton(
//                        onClick = {
//                            eventList.clear()
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Event,
//                            contentDescription = null,
//                            modifier = Modifier,
//                        )
//                    }
//                    IconButton(
//                        onClick = {
//                            poiList.clear()
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.FrontHand,
//                            contentDescription = null,
//                            modifier = Modifier,
//                        )
//                    }
//                }
//            }
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