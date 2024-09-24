package com.example.podcastrss.di.store

import com.example.podcastrss.application.PodcastPlayerMemoryCache
import com.example.podcastrss.application.PodcastPlayerMemoryCacheImpl
import com.example.podcastrss.application.PodcastResponseMemoryCache
import com.example.podcastrss.application.PodcastResponseMemoryCacheImpl
import org.koin.dsl.module

val storeModule = module {
    single<PodcastResponseMemoryCache>{
        PodcastResponseMemoryCacheImpl()
    }
    single<PodcastPlayerMemoryCache>{
        PodcastPlayerMemoryCacheImpl()
    }
}