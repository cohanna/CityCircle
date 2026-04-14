package com.csci448.kate_lundberg.citycircle.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String = "",
    val username: String = "",
    val bio: String = "",
    val profilePictureUrl: String? = null,
    val eventsAttended: List<Event> = emptyList(),
    val eventsHosted: List<Event> = emptyList()
)
