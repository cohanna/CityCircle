package com.csci448.kate_lundberg.citycircle.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CityCircleBottomBar(
    currentRoute: String?,
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit,
    onMapClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == "home",
            onClick = onHomeClick
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search") },
            selected = currentRoute == "search",
            onClick = onSearchClick
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Map, contentDescription = "Map") },
            label = { Text("Map") },
            selected = currentRoute == "map",
            onClick = onMapClick
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentRoute == "profile",
            onClick = onProfileClick
        )
    }
}

@Preview
@Composable
fun CityCircleBottomBarPreview() {
    CityCircleBottomBar(
        currentRoute = "home",
        onHomeClick = {},
        onSearchClick = {},
        onMapClick = {},
        onProfileClick = {}
    )
}
