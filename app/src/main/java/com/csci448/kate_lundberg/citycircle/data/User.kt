package com.csci448.kate_lundberg.citycircle.data

data class User(
    val name: String,
    val username: String,
    val bio: String,
    val profilePictureResId: Int,
    val eventsAttended: List<Event>,
    val eventsHosted: List<Event>
)
