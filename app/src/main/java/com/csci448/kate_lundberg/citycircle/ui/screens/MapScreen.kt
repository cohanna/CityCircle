package com.csci448.kate_lundberg.citycircle.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.csci448.kate_lundberg.citycircle.ui.theme.Blue
import com.csci448.kate_lundberg.citycircle.ui.theme.Cream

@Composable
fun MapScreen() {

    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Button(
            onClick = {
                Toast.makeText(
                    context,
                    "Your location will be plotted on a Google Map",
                    Toast.LENGTH_LONG
                ).show()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue,
                contentColor = Cream
            )
        ) {
            Text("Show My Location")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    MapScreen()
}