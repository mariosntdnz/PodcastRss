package com.example.podcastrss.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.podcastrss.datasource.remote.PodcastRssFullInformationDataSourceImpl
import com.example.podcastrss.repository.PodcastRssFullInformationRespositoryImpl
import com.example.podcastrss.service.RssApiServiceImpl
import com.example.podcastrss.ui.theme.PodcastRssTheme
import com.example.podcastrss.use_case.PodcastRssSearchUseCase
import com.example.podcastrss.viewModel.PodcastRssSearchViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PodcastRssTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel = viewModel {
                        PodcastRssSearchViewModel(
                            PodcastRssSearchUseCase(
                                PodcastRssFullInformationRespositoryImpl(
                                    PodcastRssFullInformationDataSourceImpl(
                                        RssApiServiceImpl()
                                    )
                                )
                            )
                        )
                    }

                    val state by viewModel.state.collectAsState()

                    LaunchedEffect(Unit) {
                        viewModel.searchPodcastRss("https://anchor.fm/s/7a186bc/podcast/rss")
                    }

                    Greeting(
                        name = state.podcastRssResult.ifEmpty { state.errorMsg.ifEmpty { state.isLoading.toString() } },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "$name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PodcastRssTheme {
        Greeting("Android")
    }
}