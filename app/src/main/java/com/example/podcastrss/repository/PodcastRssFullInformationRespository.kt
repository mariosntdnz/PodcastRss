package com.example.podcastrss.repository

import com.example.podcastrss.datasource.remote.PodcastRssFullInformationRemoteDataSource
import com.example.podcastrss.models.PodcastRss
import com.example.podcastrss.result.ResponseResult
import kotlinx.coroutines.flow.Flow

interface PodcastRssFullInformationRespository {
    fun getPodcastRssFullInformation(url: String): Flow<ResponseResult<PodcastRss>>
}

class PodcastRssFullInformationRespositoryImpl(
    private val remoteDataSource: PodcastRssFullInformationRemoteDataSource
): PodcastRssFullInformationRespository {
    override fun getPodcastRssFullInformation(url: String): Flow<ResponseResult<PodcastRss>> {
        return remoteDataSource.getPodcastRssFullInformation(url)
    }
}