package com.example.podcastrss.service

import okhttp3.OkHttpClient
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import rx.schedulers.Schedulers

interface RssApiService {
    fun getRssApiService(url: String): PodcastRssResponse
}

class RssApiServiceImpl:  RssApiService {
    private val okHttpClient = OkHttpClient()
    override fun getRssApiService(url: String): PodcastRssResponse {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(Persister(AnnotationStrategy())))
            .build()
            .create(PodcastRssResponse::class.java)
    }
}