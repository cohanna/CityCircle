package com.csci448.kate_lundberg.citycircle.ui.screens

import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csci448.kate_lundberg.citycircle.data.Event
import com.csci448.kate_lundberg.citycircle.ui.theme.Blue
import com.csci448.kate_lundberg.citycircle.ui.theme.Cream
import com.csci448.kate_lundberg.citycircle.utils.FirebaseStorageUtil
import com.csci448.kate_lundberg.citycircle.utils.LocationUtils
import com.csci448.kate_lundberg.citycircle.R
import com.csci448.kate_lundberg.citycircle.ui.ImageSelect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale
import java.util.UUID

@Composable
fun CreateEventScreen(
    currentUsername: String,
    onCreateEvent: (Event) -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var locationName by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val locationUtils = remember { LocationUtils(context) }
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }
    var locationError by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    // Location autocomplete state
    var locationSuggestions by remember { mutableStateOf<List<Address>>(emptyList()) }
    var showSuggestions by remember { mutableStateOf(false) }
    val geocoder = remember { Geocoder(context, Locale.getDefault()) }
    var suggestionJob by remember { mutableStateOf<Job?>(null) }

    // Fetch suggestions when locationName changes, with debounce
    LaunchedEffect(locationName) {
        suggestionJob?.cancel()
        if (locationName.length < 3) {
            locationSuggestions = emptyList()
            showSuggestions = false
            return@LaunchedEffect
        }
        suggestionJob = launch {
            delay(300) // debounce
            try {
                val addresses = withContext(Dispatchers.IO) {
                    geocoder.getFromLocationName(locationName, 5)
                }
                locationSuggestions = addresses ?: emptyList()
                showSuggestions = locationSuggestions.isNotEmpty()
            } catch (e: Exception) {
                locationSuggestions = emptyList()
                showSuggestions = false
            }
        }
    }

    LaunchedEffect(Unit) {
        val logoFile = File(context.cacheDir, "default_logo.jpg")
        if (!logoFile.exists()) {
            context.resources.openRawResource(R.drawable.citycircle_logo.let {
                context.resources.getIdentifier("citycircle_logo", "drawable", context.packageName)
            }).use { input ->
                logoFile.outputStream().use { output -> input.copyTo(output) }
            }
        }
        imageUri = Uri.fromFile(logoFile)
    }

    LazyColumn {
        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                //header
                Text(
                    text = "Create Event",
                    fontSize = 28.sp,
                    color = Blue
                )

                Spacer(modifier = Modifier.height(24.dp))

                ImageSelect(
                    imageUri = imageUri,
                    onImageSelected = { imageUri = it },
                )

                Spacer(modifier = Modifier.height(16.dp))

                //title
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if (it.isNotBlank()) titleError = false
                    },
                    label = { Text("Event Title *") },
                    isError = titleError,
                    supportingText = {
                        if (titleError) {
                            Text(
                                text = "Event must have a title",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                //description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Event Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Location with autocomplete – using DropdownMenu to avoid nested scrollable crash
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = locationName,
                        onValueChange = {
                            locationName = it
                            locationError = false
                        },
                        label = { Text("Location:") },
                        isError = locationError,
                        modifier = Modifier.fillMaxWidth()
                    )

                    DropdownMenu(
                        expanded = showSuggestions && locationSuggestions.isNotEmpty(),
                        onDismissRequest = { showSuggestions = false }
                    ) {
                        locationSuggestions.forEach { address ->
                            DropdownMenuItem(
                                text = { Text(address.getAddressLine(0) ?: address.featureName) },
                                onClick = {
                                    locationName = address.getAddressLine(0) ?: address.featureName
                                    latitude = address.latitude
                                    longitude = address.longitude
                                    locationSuggestions = emptyList()
                                    showSuggestions = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                //Date
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date (optional)") },
                    placeholder = { Text("May 10, 2026, 5 PM") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                //Cost
                OutlinedTextField(
                    value = cost,
                    onValueChange = { cost = it },
                    label = { Text("Cost (optional)") },
                    placeholder = { Text("\$10") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                //create event button
                ElevatedButton(
                    onClick = {
                        if (title.isBlank()) {
                            titleError = true
                            Toast.makeText(context, "Event must have a title", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            scope.launch {
                                isUploading = true
                                // If the user typed manually without choosing a suggestion, geocode now
                                if (latitude == null && longitude == null && locationName.isNotBlank()) {
                                    val coords = locationUtils.getLocationFromAddress(locationName)
                                    if (coords != null) {
                                        latitude = coords.first
                                        longitude = coords.second
                                        locationError = false
                                    } else {
                                        locationError = true
                                        isUploading = false
                                        return@launch
                                    }
                                }
                                val uploadedImageUrl = if (imageUri != null) {
                                    FirebaseStorageUtil.uploadEventImage(imageUri!!)
                                } else null
                                val newEvent = Event(
                                    id = "event_${UUID.randomUUID()}",
                                    title = title,
                                    description = description,
                                    createdBy = currentUsername,
                                    locationName = locationName,
                                    latitude = latitude,
                                    longitude = longitude,
                                    imageUrl = uploadedImageUrl,
                                    date = date,
                                    cost = cost
                                )
                                isUploading = false
                                onCreateEvent(newEvent)
                                Toast.makeText(context, "Event created!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    enabled = !isUploading,
                    elevation = ButtonDefaults.elevatedButtonElevation(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue,
                        contentColor = Cream
                    )
                ) {
                    if (isUploading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Blue
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Uploading...")
                    } else {
                        Text("Create Event")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateEventScreenPreview() {
    CreateEventScreen(currentUsername = "me", onCreateEvent = {})
}