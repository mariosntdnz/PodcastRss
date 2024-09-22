package com.example.podcastrss.models

data class Podcast(
    val name: String,
    val description: String,
    val bannerUrl: String,
    val author: String,
    val category: List<String>,
    val episodes: List<Episode>
)
