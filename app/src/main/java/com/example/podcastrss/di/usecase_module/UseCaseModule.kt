package com.example.podcastrss.di.usecase_module

import com.example.podcastrss.use_case.PodcastRssSearchUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory<PodcastRssSearchUseCase>{
        PodcastRssSearchUseCase(
            podcastRssFullInformationRespository = get()
        )
    }
}