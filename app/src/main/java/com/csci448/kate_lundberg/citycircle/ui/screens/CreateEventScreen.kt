package com.csci448.kate_lundberg.citycircle.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Event Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = locationName,
            onValueChange = { locationName = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        ElevatedButton(
            onClick = {
                if (title.isBlank()) { //add if for description and location too lowk
                    titleError = true
                    Toast.makeText(context, "Event must have a title", Toast.LENGTH_SHORT).show()
                } else {
                    val newEvent = Event(
                        id = "event_${UUID.randomUUID()}",
                        title = title,
                        description = description,
                        createdBy = currentUsername,
                        locationName = locationName
                    )
                    onCreateEvent(newEvent)
                    Toast.makeText(context, "Event created!", Toast.LENGTH_SHORT).show()
                }
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
    CreateEventScreen( currentUsername = "me", onCreateEvent = {} )
}