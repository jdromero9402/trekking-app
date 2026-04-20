package com.example.trekkingapp.ui.components

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Camera
import androidx.compose.material.icons.rounded.Cameraswitch
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trekkingapp.R
import com.example.trekkingapp.utils.createImageOnPhotosFolder
import com.example.trekkingapp.viewmodels.LocationState
import com.example.trekkingapp.viewmodels.LocationViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow

const val TAG = "CameraComponent"
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraComponent(
    modifier: Modifier = Modifier,
    onPhotoTaken: (Uri, LatLng?) -> Unit,
    permission: PermissionState,
    locationViewModel: LocationViewModel = viewModel ()
) {
    val locationState by locationViewModel.state.collectAsState()

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // MutableStateFlow we can update from CameraX callbacks
    val surfaceRequests = remember { MutableStateFlow<SurfaceRequest?>(null) }
    val surfaceRequest by surfaceRequests.collectAsState(initial = null)
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    // remember current lens
    var useFront by rememberSaveable { mutableStateOf(false) }
    val selector =
        if (useFront) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA

    var isCameraReady by remember { mutableStateOf(false) }

    // Bind CameraX use cases once
    LaunchedEffect(selector) {
        isCameraReady = false  // 👈 reset al cambiar cámara
        val provider = ProcessCameraProvider.awaitInstance(context)
        val preview = Preview.Builder().build().apply {
            setSurfaceProvider { req ->
                surfaceRequests.value = req
                isCameraReady = true  // 👈 marcamos lista cuando llega el primer frame
            }
        }
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()

        provider.unbindAll()
        provider.bindToLifecycle(
            lifecycleOwner,
            selector,
            preview,
            imageCapture!!,
        )
    }

    Box(modifier) {
        if (permission.status.isGranted){
            Log.d("CameraComponent", "Permission granted")
            surfaceRequest?.let { req ->
                CameraXViewfinder(
                    surfaceRequest = req, modifier = Modifier
                        .fillMaxSize()
                )
            }
            if (!isCameraReady) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            if (isCameraReady) {
                FloatingActionButton(
                    onClick = { useFront = !useFront },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                ) {
                    Icon(
                        Icons.Rounded.Cameraswitch,
                        contentDescription = null
                    )
                }
                //Button to take the photo
                FloatingActionButton(
                    onClick = { capturePhoto(context, imageCapture, onPhotoTaken, locationState) },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Icon(
                        Icons.Rounded.Camera,
                        contentDescription = null
                    )
                }
            }

        } else {
            if (permission.status.shouldShowRationale) {
                Text(stringResource(R.string.screen_in_app_camera_rationale_label))
                Button(onClick = { permission.launchPermissionRequest() }) {
                    Text(stringResource(R.string.screen_in_app_camera_request_button_label))
                }
            } else {
                ErrorMessage(message = R.string.screen_in_app_camera_no_permission_label,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(
                            Alignment.Center

                        ))
            }
        }
    }
}

private fun capturePhoto(
    context: Context,
    imageCapture: ImageCapture?,
    onPhotoTaken: (uri: Uri, pos: LatLng?) -> Unit,
    locationState: LocationState
) {
    val capture = imageCapture ?: return

    val name = "PHOTOBOOTH_IMG_${System.currentTimeMillis()}.jpg"

    capture.takePicture(
        createImageOnPhotosFolder(name, context),
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                // Success: output.savedUri
                Log.d(TAG, "Photo saved: ${output.savedUri}")
                var latLng: LatLng? = null
                locationState.isRecording.let { _ ->
                    latLng = locationState.lastPos
                }
                onPhotoTaken(output.savedUri!!, latLng)
            }

            override fun onError(exception: ImageCaptureException) {
                // Handle error
                Log.e(TAG, "Photo error", exception)
            }
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@androidx.compose.ui.tooling.preview.Preview
@Composable
fun CameraComponentPreview(){
//    CameraComponent(
//
//    )
}