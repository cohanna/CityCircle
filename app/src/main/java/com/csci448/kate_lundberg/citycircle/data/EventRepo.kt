package com.csci448.kate_lundberg.citycircle.data

import com.csci448.kate_lundberg.citycircle.R

object EventRepo {
    val events = listOf(
        Event(
            id          = "1",
            title       = "Event 1",
            description = "event one stuff",
            imageResId = R.drawable.event_one_image
        ),
        Event(
            id          = "2",
            title       = "Event 2",
            description = "event two stuff",
            imageResId = R.drawable.event_two_image
        ),
        Event(
            id          = "3",
            title       = "Event 3",
            description = "event three stuff",
            imageResId = R.drawable.event_three_image
        )

    )
}