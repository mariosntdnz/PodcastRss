package com.example.podcastrss.models

data class Episode(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val pubDate: String,
    val duration: Long,
    val durationLabel: String,
    val explicit: Boolean,
    val episodeUrl: String
)
