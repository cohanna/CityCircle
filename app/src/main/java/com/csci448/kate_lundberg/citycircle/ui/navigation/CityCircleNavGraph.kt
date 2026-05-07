package com.csci448.kate_lundberg.citycircle.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.csci448.kate_lundberg.citycircle.ui.screens.LoginScreen
import com.csci448.kate_lundberg.citycircle.ui.screens.CreateAccountScreen
import com.csci448.kate_lundberg.citycircle.ui.screens.PreferencesScreen
import com.csci448.kate_lundberg.citycircle.ui.screens.ProfileSetupScreen
import com.csci448.kate_lundberg.citycircle.data.UserRepo
import com.csci448.kate_lundberg.citycircle.ui.CityCircleBottomBar
import com.csci448.kate_lundberg.citycircle.ui.screens.*
import com.csci448.kate_lundberg.citycircle.viewmodel.CityCircleViewModel

@Composable
fun CityCircleNavGraph() {
    val viewModel: CityCircleViewModel = viewModel()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isKeyboardVisible = WindowInsets.ime.asPaddingValues().calculateBottomPadding() > 0.dp

    val shouldShowBottomBar = when {
        currentRoute == null -> false
        currentRoute.startsWith("event_detail") -> true
        isKeyboardVisible -> false
        else -> currentRoute in listOf("home", "search", "profile", "map", "create_event")
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
            composable("home") {
                val events by viewModel.events.collectAsState()
                HomeScreen(
                    events = events.filter { it.title.isNotBlank() },
                    onCreateEventClick = { navController.navigate("create_event") },
                    onEventClick = { event -> navController.navigate("event_detail/${event.id}")}
                )
            }

            composable("map") {
                val events by viewModel.events.collectAsState()
                MapScreen(
                    events.filter { it.latitude != null && it.longitude != null },
                    onEventClick = { event -> navController.navigate("event_detail/${event.id}")}
                )
            }

            composable("search") {
                val events by viewModel.events.collectAsState()
                EventSearchScreen(
                    events = events.filter { it.title.isNotBlank() },
                    onEventClick = { event -> navController.navigate("event_detail/${event.id}")}
                )
            }

            composable("profile") {
                val users by viewModel.users.collectAsState()
                val user = users.firstOrNull { it.username == "smurfette" }
                    ?: UserRepo.users[0] //just in case nothing else exists
                ProfileScreen(
                    user = user,
                    onEventClick = { event -> navController.navigate("event_detail/${event.id}")}
                )
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
                        navController.navigate("profile_setup")
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
                    onNextClick = {
                        navController.navigate("home")
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable("create_event") {
                CreateEventScreen(
                    currentUsername = "smurfette", //CHANGE TO DYNAMIC ONCE AUTH SET UP!!!
                    onCreateEvent = { event->
                        viewModel.addEvent(event)
                        navController.navigate("home")
                    }
                )
            }

            composable("event_detail/{eventId}") { backStackEntry ->
                val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable
                val events by viewModel.events.collectAsState()
                val users by viewModel.users.collectAsState()
                val event = events.firstOrNull { it.id == eventId } ?: return@composable
                val host = users.firstOrNull { it.username == event.createdBy }
                EventDetailScreen(
                    event = event,
                    onBack = { navController.popBackStack() },
                    onHostClick = { username ->
                        navController.navigate("profile/$username")
                    }
                )
            }

            composable("profile/{username}") { backStackEntry ->
                val username = backStackEntry.arguments?.getString("username") ?: return@composable
                val users by viewModel.users.collectAsState()
                val user = users.firstOrNull { it.username == username } ?: return@composable
                ProfileScreen(
                    user = user,
                    onEventClick = { event -> navController.navigate("event_detail/${event.id}")}
                )
            }
        }
    }
}
