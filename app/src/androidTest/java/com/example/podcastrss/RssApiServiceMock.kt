package com.example.podcastrss

import android.content.Context
import com.example.podcastrss.service.PodcastRssResponse
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import rx.schedulers.Schedulers

class ServiceMockWithRSSFeedSampleRsponse(context: Context) {
    private val podcastRssInterceptor = Interceptor { chain ->
        val responseString: String = context.assets.open("podcastrssitunesmock.xml").bufferedReader().use { it.readText() }
        Response.Builder()
            .code(200)
            .message(responseString)
            .request(chain.request())
            .protocol(Protocol.HTTP_1_0)
            .body(ResponseBody.create(MediaType.get("application/xml"), responseString.toByteArray()))
            .addHeader("content-type", "application/xml")
            .build()

    }

    fun getRssApiServiceMock(): PodcastRssResponse {
        val client = OkHttpClient.Builder()
            .addInterceptor(podcastRssInterceptor)
            .build()
        val retrofit = getRetrofit(client)
        return retrofit.create(PodcastRssResponse::class.java)
    }

    private fun getRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
        .baseUrl("https://example.com.br/")
        .client(okHttpClient)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(Persister(AnnotationStrategy())))
        .build()
}