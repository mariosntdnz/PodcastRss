package com.example.podcastrss.di.datasource_module

import com.example.podcastrss.datasource.remote.PodcastRssFullInformationDataSource
import com.example.podcastrss.datasource.remote.PodcastRssFullInformationDataSourceImpl
import org.koin.dsl.module

val dataSourceModule = module {
    factory<PodcastRssFullInformationDataSource> {
        PodcastRssFullInformationDataSourceImpl(
            api = get()
        )
    }
}