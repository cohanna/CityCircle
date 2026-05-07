package com.csci448.kate_lundberg.citycircle.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.csci448.kate_lundberg.citycircle.R
import com.csci448.kate_lundberg.citycircle.data.User
import com.csci448.kate_lundberg.citycircle.ui.ImageSelect
import com.csci448.kate_lundberg.citycircle.ui.theme.Blue
import com.csci448.kate_lundberg.citycircle.ui.theme.Cream
import com.csci448.kate_lundberg.citycircle.utils.FirebaseStorageUtil
import com.csci448.kate_lundberg.citycircle.viewmodel.CityCircleViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileSetupScreen(
    currentUser: User,
    cityCircleViewModel: CityCircleViewModel,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var bio by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //header
        Text(
            text = "Make Your Profile",
            fontSize = 28.sp,
            color = Blue
        )

        Spacer(modifier = Modifier.height(24.dp))

        //display profile picture
        AsyncImage(
            model = currentUser.profilePictureUrl,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.citycircle_logo),
            error = painterResource(R.drawable.citycircle_logo),
            fallback = painterResource(R.drawable.citycircle_logo),
            onSuccess = { android.util.Log.d("ProfileSetup", "Image loaded successfully") },
            onError = { android.util.Log.e("ProfileSetup", "Image failed to load: ${it.result.throwable}") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        //display username
        Text(
            text = "@${currentUser.username}",
            style = MaterialTheme.typography.titleMedium,
            color = Blue
        )

        Spacer(modifier = Modifier.height(20.dp))


        //enter location
        Text(
            text = "Show your location?",
            color = Blue
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Enter your city and state (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        //user bio
        Text(
            text = "Tell us about you",
            color = Blue
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = bio,
            onValueChange = { if (it.length <= 300) bio = it }, //cap bio to 300 chars
            label = { Text("Bio") },
            placeholder = { Text("Write a short bio...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            supportingText = { Text("${bio.length} / 300") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        //back and next buttons
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            ElevatedButton(
                onClick = onBackClick,
                elevation = ButtonDefaults.elevatedButtonElevation(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue,
                    contentColor = Cream
                )
            ) {
                Text("Back")
            }
            Spacer(modifier = Modifier.width(24.dp))
            ElevatedButton(
                onClick = {
                    scope.launch {
                        isLoading = true
                        val profilePic = if (imageUri != null) {
                            FirebaseStorageUtil.uploadEventImage(imageUri!!)
                        } else null

                        val updatedUser = currentUser.copy(
                            bio = bio,
                            location = location,
                            profilePictureUrl = profilePic ?: currentUser.profilePictureUrl
                        )
                        cityCircleViewModel.updateUser(updatedUser)
                        isLoading = false
                        onNextClick()
                    }
                },
                enabled = !isLoading,
                elevation = ButtonDefaults.elevatedButtonElevation(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue,
                    contentColor = Cream
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Blue
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Next")
            }
        }
    }
}