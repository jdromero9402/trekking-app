package com.example.trekkingapp.ui.screens

import android.Manifest
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.trekkingapp.ui.components.CameraComponent
import com.example.trekkingapp.ui.theme.AppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun InAppCameraScreen(modifier: Modifier = Modifier) {
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)
    val photos = rememberSaveable { mutableStateListOf<Uri>() }

    LaunchedEffect(null) {
        cameraPermission.launchPermissionRequest()
    }

    val cameraWeight = if (photos.isEmpty()) 0.5f else 0.35f
    val galleryWeight = 0.15f

    Column(
        modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
//        if (cameraPermission.status.isGranted) {
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
            ) { }
//        } else {
//            if (cameraPermission.status.shouldShowRationale) {
//                Text(stringResource(R.string.screen_in_app_camera_rationale_label))
//                Button(onClick = { cameraPermission.launchPermissionRequest() }) {
//                    Text(stringResource(R.string.screen_in_app_camera_request_button_label))
//                }
//            } else {
//                Text(stringResource(R.string.screen_in_app_camera_no_permission_label))
//            }
//        }
    }
}

@PreviewLightDark
@Composable
fun InAppCameraScreenPreview() {
    AppTheme() {
        InAppCameraScreen()
    }
}