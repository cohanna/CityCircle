package com.csci448.kate_lundberg.citycircle.ui.screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csci448.kate_lundberg.citycircle.data.User
import com.csci448.kate_lundberg.citycircle.ui.ImageSelect
import com.csci448.kate_lundberg.citycircle.ui.UserAuthViewModel
import com.csci448.kate_lundberg.citycircle.ui.theme.Blue
import com.csci448.kate_lundberg.citycircle.ui.theme.Cream
import com.csci448.kate_lundberg.citycircle.utils.FirebaseStorageUtil
import com.csci448.kate_lundberg.citycircle.viewmodel.CityCircleViewModel
import kotlinx.coroutines.launch


@Composable
fun CreateAccountScreen(
    userAuthViewModel: UserAuthViewModel,
    cityCircleViewModel: CityCircleViewModel,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        //header
        Text(
            text = "Create Account",
            fontSize = 28.sp,
            color = Blue
        )

        Spacer(modifier = Modifier.height(20.dp))

        //select profile picture
        Text("Upload Profile Picture")
        Spacer(modifier = Modifier.height(8.dp))
        ImageSelect(
            imageUri = imageUri,
            onImageSelected = { imageUri = it },
            modifier = Modifier.padding(20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
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
                    if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Please enter all fields", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        isLoading = true
                        userAuthViewModel.createAccount(
                            email = email,
                            password = password,
                            onSuccess = {
                                userAuthViewModel.setCurrentUsername(username)
                                scope.launch {
                                    isLoading = true
                                    val uploadedUrl = try {
                                        if (imageUri != null) FirebaseStorageUtil.uploadEventImage(imageUri!!) else null
                                    } catch (e: Exception) {
                                        Log.e("CreateAccount", "Upload exception: ${e.message}")
                                        null
                                    }
                                    Log.d("CreateAccount", "uploadedUrl after upload: $uploadedUrl")
                                    val user = User(
                                        email = email,
                                        username = username,
                                        bio = "",
                                        profilePictureUrl = uploadedUrl
                                    )
                                    Log.d("448.CreateAccount", "Before addUser with profile picture: $uploadedUrl")
                                    cityCircleViewModel.addUser(user) {
                                        isLoading = false
                                        onNextClick()
                                    }
                                }
                            },
                            onError = { errorMessage ->
                                isLoading = false
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                enabled = !isLoading,
                elevation = ButtonDefaults.elevatedButtonElevation(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue,
                    contentColor = Cream
                )
            ) {
                Text("Next")
            }
        }
    }
}