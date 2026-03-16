package com.csci448.kate_lundberg.citycircle.ui.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.csci448.kate_lundberg.citycircle.ui.theme.Blue
import com.csci448.kate_lundberg.citycircle.ui.theme.Cream
import androidx.compose.ui.res.painterResource
import com.csci448.kate_lundberg.citycircle.R

@Composable
fun LoginScreen(onSignInClick: () -> Unit, onCreateAccountClick: () -> Unit) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        ElevatedButton (onClick = onSignInClick,
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

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onSignInClick = {}, onCreateAccountClick = {})
}
