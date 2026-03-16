package com.csci448.kate_lundberg.citycircle.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.csci448.kate_lundberg.citycircle.data.UserRepo
import com.csci448.kate_lundberg.citycircle.ui.CityCircleBottomBar
import com.csci448.kate_lundberg.citycircle.ui.screens.*

@Composable
fun CityCircleNavGraph() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isKeyboardVisible = WindowInsets.ime.asPaddingValues().calculateBottomPadding() > 0.dp

    val shouldShowBottomBar = when (currentRoute) {
        "home", "search", "profile", "map" -> !isKeyboardVisible
        else -> false
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                CityCircleBottomBar(
                    currentRoute = currentRoute,
                    onHomeClick = {
                        navController.navigate("home")
                    },
                    onSearchClick = {
                        navController.navigate("search")
                    },
                    onMapClick = {
                        navController.navigate("map")
                    },
                    onProfileClick = {
                        navController.navigate("profile")
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen() }
            
            composable("search") { EventSearchScreen() }

            composable("map") { 
                Text("Map Screen coming soon", modifier = Modifier.padding(16.dp))
            }

            composable("profile") {
                //hardcoded for now, change later
                ProfileScreen(user = UserRepo.users[1])
            }

            composable("login") {
                LoginScreen(
                    onCreateAccountClick = {
                        navController.navigate("create_account")
                    },
                    onSignInClick = {
                        navController.navigate("home")
                    }
                )
            }

            composable("create_account") {
                CreateAccountScreen(
                    onNextClick = {
                        navController.navigate("preferences")
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable("preferences") {
                PreferencesScreen(
                    onNextClick = {
                        navController.navigate("home")
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}