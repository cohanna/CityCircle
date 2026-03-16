package com.csci448.kate_lundberg.citycircle.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csci448.kate_lundberg.citycircle.ui.theme.Blue
import com.csci448.kate_lundberg.citycircle.ui.theme.Cream

@Composable
fun ProfileSetupScreen(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit
) {

    val context = LocalContext.current

    var handle by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

    var showLocation by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Make Your Profile",
            fontSize = 28.sp,
            color = Blue
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable {
                    Toast.makeText(
                        context,
                        "Profile image picker coming soon",
                        Toast.LENGTH_SHORT
                    ).show()
                },
            contentAlignment = Alignment.Center
        ) {
            Text("Tap to Add Photo")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "@handle",
            style = MaterialTheme.typography.titleMedium,
            color = Blue
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Show your location?",
            color = Blue
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {

            FilterChip(
                selected = showLocation,
                onClick = { showLocation = true },
                label = { Text("Yes") }
            )

            Spacer(modifier = Modifier.width(12.dp))

            FilterChip(
                selected = !showLocation,
                onClick = { showLocation = false },
                label = { Text("No") }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Tell us about you",
            color = Blue
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            placeholder = { Text("Write a short bio...") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.Center
        ) {

            ElevatedButton(
                onClick = {
                    Toast.makeText(
                        context,
                        "Profile saved (alpha placeholder)",
                        Toast.LENGTH_SHORT
                    ).show()

                    onNextClick()
                },
                elevation = ButtonDefaults.elevatedButtonElevation(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue,
                    contentColor = Cream
                )
            ) {
                Text("Next")
            }

            Spacer(modifier = Modifier.width(24.dp))

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
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ProfileSetupScreenPreview() {
    ProfileSetupScreen(
        onNextClick = {},
        onBackClick = {}
    )
}