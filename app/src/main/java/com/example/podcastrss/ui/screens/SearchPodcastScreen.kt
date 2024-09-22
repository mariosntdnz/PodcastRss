package com.example.podcastrss.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.podcastrss.viewModel.PodcastRssSearchViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun SearchPodcastScreen() {
    val viewModel = getViewModel<PodcastRssSearchViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.searchPodcastRss("https://anchor.fm/s/7a186bc/podcast/rss")
    }

    Greeting(
        name = state.podcast.toString()
    )
}