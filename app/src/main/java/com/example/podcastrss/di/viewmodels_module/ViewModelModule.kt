package com.example.podcastrss.di.viewmodels_module

import com.example.podcastrss.viewModel.PodcastRssSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        PodcastRssSearchViewModel(
            podcastRssSearchUseCase = get()
        )
    }
}