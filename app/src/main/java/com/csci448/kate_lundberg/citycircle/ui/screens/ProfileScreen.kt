package com.csci448.kate_lundberg.citycircle.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.csci448.kate_lundberg.citycircle.data.User
import com.csci448.kate_lundberg.citycircle.data.UserRepo
import com.csci448.kate_lundberg.citycircle.ui.EventCard

@Composable
fun ProfileScreen(user: User) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = user.profilePictureResId),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = user.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = "@${user.username}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = user.bio,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        val hasAttended = user.eventsAttended.isNotEmpty()
        val hasHosted = user.eventsHosted.isNotEmpty()

        if (hasAttended || hasHosted) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (hasAttended) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Events Attended",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        user.eventsAttended.forEach { event ->
                            EventCard(event = event, isSmall = hasHosted)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                if (hasAttended && hasHosted) {
                    Spacer(modifier = Modifier.width(16.dp))
                }

                if (hasHosted) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Events Hosted",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        user.eventsHosted.forEach { event ->
                            EventCard(event = event, isSmall = hasAttended)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        } else {
            Text(
                text = "No events yet!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(top = 32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SmurfettePreview() {
    ProfileScreen(user = UserRepo.users[0])
}

@Preview(showBackground = true)
@Composable
fun FinnPreview() {
    ProfileScreen(user = UserRepo.users[1])
}

@Preview(showBackground = true)
@Composable
fun BobPreview() {
    ProfileScreen(user = UserRepo.users[2])
}
