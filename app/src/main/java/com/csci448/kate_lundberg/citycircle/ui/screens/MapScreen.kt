package com.csci448.kate_lundberg.citycircle.ui.screens

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.csci448.kate_lundberg.citycircle.ui.theme.Blue
import com.csci448.kate_lundberg.citycircle.ui.theme.Cream
import com.csci448.kate_lundberg.citycircle.utils.LocationUtils
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationUtils = remember { LocationUtils(context) }

    // Permission state
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    // Camera position state
    val cameraPositionState = rememberCameraPositionState()

    // UI states
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Request permission and get location
    LaunchedEffect(Unit) {
        locationPermissionState.launchPermissionRequest()

        if (locationPermissionState.status.isGranted) {
            try {
                val location = locationUtils.getCurrentLocation()
                if (location != null) {
                    userLocation = LatLng(location.latitude, location.longitude)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(userLocation!!, 15f)
                } else {
                    errorMessage = "Unable to get current location. Default location will be shown."
                    // Default to a central location (New York City)
                    userLocation = LatLng(40.7128, -74.0060)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(userLocation!!, 10f)
                }
            } catch (e: Exception) {
                errorMessage = "Error getting location: ${e.message}"
                userLocation = LatLng(40.7128, -74.0060)
                cameraPositionState.position = CameraPosition.fromLatLngZoom(userLocation!!, 10f)
            }
            isLoading = false
        } else {
            isLoading = false
            errorMessage = "Location permission is required to show your location"
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                // Loading state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Blue)
                }
            }

            locationPermissionState.status.isGranted && userLocation != null -> {
                // Map with user location
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = true
                    ),
                    uiSettings = MapUiSettings(
                        myLocationButtonEnabled = true  // This enables the default My Location button
                    )
                ) {
                    // Add marker for user location
                    if (userLocation != null) {
                        Marker(
                            state = MarkerState(position = userLocation!!),
                            title = "Your Location",
                            snippet = "You are here"
                        )
                    }
                }
            }

            else -> {
                // Permission denied or error state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = errorMessage ?: "Location permission required",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                locationPermissionState.launchPermissionRequest()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue,
                            contentColor = Cream
                        )
                    ) {
                        Text("Grant Location Permission")
                    }

                    if (!locationPermissionState.status.isGranted && locationPermissionState.status.shouldShowRationale) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Location permission is needed to show events near you and help you find local events.",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MapScreenPreview() {
    MaterialTheme {
        MapScreen()
    }
}