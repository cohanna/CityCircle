package com.csci448.kate_lundberg.citycircle.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.csci448.kate_lundberg.citycircle.data.Event
import com.csci448.kate_lundberg.citycircle.ui.theme.Blue
import com.csci448.kate_lundberg.citycircle.ui.theme.Cream
import com.csci448.kate_lundberg.citycircle.utils.FirebaseStorageUtil
import com.csci448.kate_lundberg.citycircle.utils.LocationUtils
import com.csci448.kate_lundberg.citycircle.R
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import java.io.File
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

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri ->
        imageUri = uri
    }
    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val resultUri = UCrop.getOutput(result.data ?: return@rememberLauncherForActivityResult)
        if (resultUri != null) imageUri = resultUri
    }

    fun launchCrop(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(
            File(context.cacheDir, "cropped_${UUID.randomUUID()}.jpg")
        )
        val options = UCrop.Options().apply {
            setHideBottomControls(false)
            setFreeStyleCropEnabled(false)
        }
        val intent = UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(8f, 8f)
            .withOptions(options)
            .getIntent(context)
        cropLauncher.launch(intent)
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

        //event image
        AsyncImage(
            model = imageUri,
            contentDescription = "Event Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(60.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            ElevatedButton(
                onClick = { imagePickerLauncher.launch("image/*") },
                enabled = !isUploading,
                elevation = ButtonDefaults.elevatedButtonElevation(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue,
                    contentColor = Cream
                ),
                modifier = Modifier.scale(0.75f)
            ) {
                Text("Change Image")
            }
            ElevatedButton(
                onClick = { launchCrop(imageUri!!) },
                enabled = !isUploading && imageUri != null,
                elevation = ButtonDefaults.elevatedButtonElevation(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue,
                    contentColor = Cream
                ),
                modifier = Modifier.scale(0.75f)
            ) {
                Icon(
                    imageVector = Icons.Default.Crop,
                    contentDescription = "Crop",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Crop Image")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

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
                        text = "Event must have a title",   //make strings.xml value
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

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

        //location
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

        Spacer(modifier = Modifier.height(24.dp))

        //create event button
        ElevatedButton(
            onClick = {
                if (title.isBlank()) { //add if for description and location too lowk
                    titleError = true
                    Toast.makeText(context, "Event must have a title", Toast.LENGTH_SHORT).show()
                } else {
                    scope.launch {
                        isUploading = true
                        if (locationName.isNotBlank()) {
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
                            imageUrl = uploadedImageUrl
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

@Preview(showBackground = true)
@Composable
fun CreateEventScreenPreview() {
    CreateEventScreen(currentUsername = "me", onCreateEvent = {})
}