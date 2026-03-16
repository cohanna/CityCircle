package com.csci448.kate_lundberg.citycircle.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csci448.kate_lundberg.citycircle.ui.theme.Blue
import com.csci448.kate_lundberg.citycircle.ui.theme.Cream

@Composable
fun CreateEventScreen() {

    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Create Event",
            fontSize = 28.sp,
            color = Blue
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Event Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Event Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        ElevatedButton(
            onClick = {
                Toast.makeText(
                    context,
                    "Event created (alpha placeholder)",
                    Toast.LENGTH_SHORT
                ).show()
            },
            elevation = ButtonDefaults.elevatedButtonElevation(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue,
                contentColor = Cream
            )
        ) {
            Text("Create Event")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateEventScreenPreview() {
    CreateEventScreen()
}