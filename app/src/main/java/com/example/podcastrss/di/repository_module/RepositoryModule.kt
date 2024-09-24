package com.example.podcastrss.di.repository_module

import com.example.podcastrss.application.App
import com.example.podcastrss.repository.PodcastPlayerRepository
import com.example.podcastrss.repository.PodcastPlayerRepositoryImpl
import com.example.podcastrss.repository.PodcastRssFullInformationRespository
import com.example.podcastrss.repository.PodcastRssFullInformationRespositoryImpl
import com.example.podcastrss.repository.SearchHistoryRepository
import com.example.podcastrss.repository.SearchHistoryRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    factory<PodcastRssFullInformationRespository> {
        PodcastRssFullInformationRespositoryImpl(
            remoteDataSource = get(),
            localDataSource = get()
        )
    }
    factory<PodcastPlayerRepository> {
        PodcastPlayerRepositoryImpl(
            playerPodcastMemoryCache = get(),
            podcastResponseMemoryCache = get()
        )
    }
    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(App.database?.historyDao()!!)
    }
}