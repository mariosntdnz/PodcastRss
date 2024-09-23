package com.example.podcastrss.di.repository_module

import com.example.podcastrss.repository.PodcastRssFullInformationRespository
import com.example.podcastrss.repository.PodcastRssFullInformationRespositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    factory<PodcastRssFullInformationRespository> {
        PodcastRssFullInformationRespositoryImpl(
            remoteDataSource = get()
        )
    }
}