package com.example.trekkingapp.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LocationState(
    val isRecording: Boolean = false,
    val route: List<LatLng> = emptyList(),
    val lastPos: LatLng? = null,
    val speed : Float = 0f
)

class LocationViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LocationState())
    val state = _uiState.asStateFlow()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private  lateinit var getPosition: LocationCallback

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }

    fun setup(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000).apply {
            setMinUpdateDistanceMeters(5F)
            setMinUpdateIntervalMillis(1000)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                _uiState.update { currentState ->
                    Log.d("LocationViewModel", "onLocationResult: $locationResult")
                    currentState.copy(
                        route = currentState.route.plus(locationResult.locations.map {
                            LatLng(
                                it.latitude,
                                it.longitude
                            )
                        }),
                        lastPos = LatLng(
                            locationResult.locations.last().latitude,
                            locationResult.locations.last().longitude
                        ),
                        speed = locationResult.locations.last().speed
                    )
                }
            }
        }
        getPosition = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                _uiState.update { currentState ->
                    Log.d("LocationViewModel", "onLocationResult: $locationResult")
                    currentState.copy(
                        lastPos = LatLng(
                            locationResult.locations.last().latitude,
                            locationResult.locations.last().longitude
                        )
                    )
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startRecordingLocation() {
        _uiState.update {
            it.copy(isRecording = true)
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        _uiState.update {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                getPosition,
                Looper.getMainLooper()
            )
            it.copy()
        }
    }

    fun stopLocationUpdates() {
        _uiState.update {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            it.copy(isRecording = false)
        }
    }

    fun stopRecording(){
        _uiState.update {
            it.copy(isRecording = false)
        }
    }

    fun clearRoute() {
        _uiState.update {
            it.copy(route = emptyList())
        }
    }
}