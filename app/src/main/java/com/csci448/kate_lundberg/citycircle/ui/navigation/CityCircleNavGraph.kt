package com.csci448.kate_lundberg.citycircle.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.csci448.kate_lundberg.citycircle.ui.screens.LoginScreen
import com.csci448.kate_lundberg.citycircle.ui.screens.CreateAccountScreen
import com.csci448.kate_lundberg.citycircle.ui.screens.PreferencesScreen
import com.csci448.kate_lundberg.citycircle.ui.screens.ProfileSetupScreen

@Composable
fun CityCircleNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(
                onCreateAccountClick = {
                    navController.navigate("create_account")
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
        composable("profile_setup") {
            ProfileSetupScreen(
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
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
