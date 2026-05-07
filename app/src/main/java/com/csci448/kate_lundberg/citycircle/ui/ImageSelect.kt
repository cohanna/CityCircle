package com.csci448.kate_lundberg.citycircle.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.csci448.kate_lundberg.citycircle.ui.theme.Blue
import com.csci448.kate_lundberg.citycircle.ui.theme.Cream
import com.yalantis.ucrop.UCrop
import com.csci448.kate_lundberg.citycircle.R
import java.io.File
import java.util.UUID

//select images and crop them afterwards
@Composable
fun ImageSelect(
    imageUri: Uri?,
    onImageSelected: (Uri) -> Unit,
    modifier: Modifier = Modifier,
){
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri ->
        if (uri != null) onImageSelected(uri!!)
    }
    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val resultUri = UCrop.getOutput(result.data ?: return@rememberLauncherForActivityResult)
        if (resultUri != null) onImageSelected(resultUri!!)
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

    Column( modifier = modifier ) {
        AsyncImage(
            model = imageUri,
            contentDescription = "Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.citycircle_logo),
            fallback = painterResource(R.drawable.citycircle_logo),
            error = painterResource(R.drawable.citycircle_logo)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(60.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            ElevatedButton(
                onClick = { imagePickerLauncher.launch("image/*") },
//                enabled = !isUploading,
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
//                enabled = !isUploading && imageUri != null,
                enabled = imageUri != null,
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
    }
}