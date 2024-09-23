package com.example.podcastrss.di.datasource_module

import com.example.podcastrss.datasource.remote.PodcastRssFullInformationRemoteDataSource
import com.example.podcastrss.datasource.remote.PodcastRssFullInformationRemoteDataSourceImpl
import org.koin.dsl.module

val dataSourceModule = module {
    factory<PodcastRssFullInformationRemoteDataSource> {
        PodcastRssFullInformationRemoteDataSourceImpl(
            api = get()
        )
    }
}