package com.csci448.kate_lundberg.citycircle.data

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String? = null
)
