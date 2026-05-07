package com.csci448.kate_lundberg.citycircle.data

object UserRepo {
    val users: List<User> by lazy {
        listOf(
            User(
                username = "smurfette",
                bio = "the girl",
                profilePictureUrl = null,
                eventsAttended = listOf(),
                eventsHosted = listOf(EventRepo.events[0], EventRepo.events[1], EventRepo.events[2])
            ),
            User(
                username = "adventure-time",
                bio = "Finn Mertens (also known as Finn the Human) is the main protagonist in Adventure Time. He also appeared in the spin-off series Adventure Time: Distant Lands and returned in the spin-off series Adventure Time: Fionna & Cake. He was voiced by Jeremy Shada in most appearances. The character made his debut in the original pilot, where he is named \"Pen\" and voiced by Zack Shada, Jeremy's older brother. Jonathan Frakes voices Finn as an adult in some appearances and David Bradley voiced Finn in his old age in the Distant Lands special, \"Together Again\".\n" +
                        "\n" +
                        "Prior to the start of Adventure Time, Finn was adopted as an infant by two anthropomorphic dogs named Joshua and Margaret, who found him in the woods. His best friend and adoptive brother Jake the Dog (John DiMaggio) accompany him on many of his adventures in Ooo, the fictional world that the series is set in. The mystery surrounding Finn's origins and whether he is the last human left in Ooo is a major part of the character's narrative arc. Later in the series, it is revealed that there are other humans still living in Ooo and that Finn's parents Martin Mertens and Minerva Campbell came from a community of survivors living on a chain of islands.\n" +
                        "\n" +
                        "Finn has received universal praise for his character development and coming-of-age narrative, which saw the character mature from a boy into a wise young man. Adam Muto, former showrunner for Adventure Time, has stated that Finn's progression from an aggressive and reckless boy into a more mature wise person was one of his favorite character arcs to write. A gender-swapped version of Finn called Fionna appeared in the third-season episode \"Fionna and Cake\" and is the main character of the spin-off series Adventure Time: Fionna & Cake. Finn returns in this aforementioned series as an adult adventurer.",
                profilePictureUrl = null,
                eventsAttended = listOf(EventRepo.events[0]),
                eventsHosted = listOf()
            ),
            User(
                username = "spongebob_sqaure",
                bio = "a square",
                profilePictureUrl = null,
                eventsAttended = listOf(EventRepo.events[0], EventRepo.events[1], EventRepo.events[2]),
                eventsHosted = listOf(EventRepo.events[0])
            )
        )
    }
}