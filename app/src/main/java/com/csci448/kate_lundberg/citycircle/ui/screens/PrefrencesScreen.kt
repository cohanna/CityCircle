package com.csci448.kate_lundberg.citycircle.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.csci448.kate_lundberg.citycircle.ui.theme.Blue
import com.csci448.kate_lundberg.citycircle.ui.theme.Cream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(onNextClick: () -> Unit, onBackClick: () -> Unit) {
    var distance by remember { mutableStateOf("") }
    var eventType by remember { mutableStateOf("") }

    var expandedCost by remember { mutableStateOf(false) }
    var expandedSize by remember { mutableStateOf(false) }

    var selectedCost by remember { mutableStateOf("Select Cost") }
    var selectedSize by remember { mutableStateOf("Select Size") }

    val costOptions = listOf("$ (> $5)", "$$ (~ $50)", "$$$ (< $75)")
    val sizeOptions = listOf("Small (> 15 attendees)", "Medium (~ 50 attendees)", "Large (> 75 attendees)")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "Preferences",
            fontSize = 28.sp,
            color = Blue
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = distance,
            onValueChange = { if ( it.toDoubleOrNull() != null ) distance = it },
            label = { Text("Distance (in miles)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(  //probs make a drop down
            value = eventType,
            onValueChange = { eventType = it },
            label = { Text("Type of Event") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = expandedCost,
            onExpandedChange = { expandedCost = !expandedCost }
        ) {

            OutlinedTextField(
                value = selectedCost,
                onValueChange = {},
                readOnly = true,
                label = { Text("Cost") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCost)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandedCost,
                onDismissRequest = { expandedCost = false }
            ) {
                costOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedCost = option
                            expandedCost = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = expandedSize,
            onExpandedChange = { expandedSize = !expandedSize }
        ) {

            OutlinedTextField(
                value = selectedSize,
                onValueChange = {},
                readOnly = true,
                label = { Text("Event Size") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSize)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandedSize,
                onDismissRequest = { expandedSize = false }
            ) {
                sizeOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedSize = option
                            expandedSize = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ElevatedButton (
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
            ElevatedButton (
                onClick = onNextClick,
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

@Preview(showBackground = true)
@Composable
fun PreferencesScreenPreview() {
    PreferencesScreen(onNextClick = {}, onBackClick = {})
}
