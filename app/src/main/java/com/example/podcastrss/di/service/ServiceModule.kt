package com.example.podcastrss.di.service

import com.example.podcastrss.service.RssApiService
import com.example.podcastrss.service.RssApiServiceImpl
import org.koin.dsl.module

val serviceModule = module {
    factory<RssApiService> {
        RssApiServiceImpl()
    }
}