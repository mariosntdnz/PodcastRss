package com.example.podcastrss.di.usecase_module

import com.example.podcastrss.use_case.PodcastRssSearchUseCase
import com.example.podcastrss.use_case.ShowPodcastDetailsUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory<PodcastRssSearchUseCase>{
        PodcastRssSearchUseCase(
            podcastRssFullInformationRespository = get()
        )
    }
    factory<ShowPodcastDetailsUseCase>{
        ShowPodcastDetailsUseCase(
            podcastRssFullInformationRespository = get()
        )
    }
}