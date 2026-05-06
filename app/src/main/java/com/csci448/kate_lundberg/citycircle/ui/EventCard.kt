package com.csci448.kate_lundberg.citycircle.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.csci448.kate_lundberg.citycircle.R
import com.csci448.kate_lundberg.citycircle.data.Event
import com.csci448.kate_lundberg.citycircle.data.EventRepo

@Composable
fun EventCard(
    event: Event,
    modifier: Modifier = Modifier,
    isSmall: Boolean = false,
    onClick: () -> Unit = {}
) {
    val imageSize = if (isSmall) 40.dp else 80.dp
    val padding = if (isSmall) 8.dp else 16.dp
    val titleStyle = if (isSmall) MaterialTheme.typography.titleSmall else MaterialTheme.typography.titleLarge
    val bodyStyle = if (isSmall) MaterialTheme.typography.bodySmall else MaterialTheme.typography.bodyMedium

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(if (isSmall) 4.dp else 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSmall) 2.dp else 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = event.title,
                    style = titleStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!isSmall) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = event.description,
                        style = bodyStyle,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.width(if (isSmall) 8.dp else 16.dp))
            
            AsyncImage(
                model = event.imageUrl ?: R.drawable.citycircle_logo,
                contentDescription = "Event Image",
                modifier = Modifier.size(imageSize),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.citycircle_logo),
                error = painterResource(R.drawable.citycircle_logo)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventCardListPreview() {
    Column {
        EventRepo.events.forEach { event ->
            EventCard(event = event, isSmall = false)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventCardSmallPreview() {
    EventCard(event = EventRepo.events[0], isSmall = true)
}