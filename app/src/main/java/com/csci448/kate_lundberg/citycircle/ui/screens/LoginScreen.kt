package com.csci448.kate_lundberg.citycircle.ui.screens
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.csci448.kate_lundberg.citycircle.ui.theme.Blue
import com.csci448.kate_lundberg.citycircle.ui.theme.Cream
import androidx.compose.ui.res.painterResource
import com.csci448.kate_lundberg.citycircle.R
import com.csci448.kate_lundberg.citycircle.ui.UserAuthViewModel

@Composable
fun LoginScreen(
    userAuthViewModel: UserAuthViewModel,
    onSignInSuccess: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.citycircle_logo),
            contentDescription = "CityCircle Logo",
            modifier = Modifier.size(400.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        ElevatedButton (
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Please enter a username and password", Toast.LENGTH_SHORT).show()
                } else {
                    isLoading = true
                    userAuthViewModel.signIn(
                        email = email,
                        password = password,
                        onSuccess = {
                            isLoading = false
                            onSignInSuccess()
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
            )) {
            Text("Sign In")
        }

        Spacer(modifier = Modifier.height(12.dp))

        ElevatedButton(onClick = {onCreateAccountClick()},
            elevation = ButtonDefaults.elevatedButtonElevation(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue,
                contentColor = Cream
            )) {
            Text("Create Account")
        }
    }
}
