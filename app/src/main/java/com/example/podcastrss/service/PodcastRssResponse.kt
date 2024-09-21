package com.example.podcastrss.service

import com.example.podcastrss.models.PodcastRss
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PodcastRssResponse {
    @GET("{endPoint}")
    suspend fun getPodcastRss(@Path("endPoint") endPoint: String): Response<PodcastRss>
}