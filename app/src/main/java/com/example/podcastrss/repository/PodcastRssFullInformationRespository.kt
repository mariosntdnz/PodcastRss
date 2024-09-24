package com.example.podcastrss.repository

import com.example.podcastrss.application.PodcastPlayerMemoryCache
import com.example.podcastrss.application.PodcastResponseMemoryCache
import com.example.podcastrss.datasource.remote.PodcastRssFullInformationRemoteDataSource
import com.example.podcastrss.models.PodcastRss
import com.example.podcastrss.result.ResponseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

interface PodcastRssFullInformationRespository {
    suspend fun getPodcastRssFullInformation(url: String): Flow<ResponseResult<PodcastRss>>
}

class PodcastRssFullInformationRespositoryImpl(
    private val remoteDataSource: PodcastRssFullInformationRemoteDataSource,
    private val localDataSource: PodcastResponseMemoryCache
): PodcastRssFullInformationRespository {

    override suspend fun getPodcastRssFullInformation(url: String): Flow<ResponseResult<PodcastRss>> {
        localDataSource.getPodcastRss(url)?.let {
            return it.map { data -> ResponseResult.Success(data) }
        }

        val fullInfoFlow = remoteDataSource.getPodcastRssFullInformation(url)
        val lastFlow = fullInfoFlow.last()

        if (lastFlow is ResponseResult.Success) {
            store(url, lastFlow.data)
        }

        return fullInfoFlow
    }

    private suspend fun store(url: String, response: PodcastRss) {
        localDataSource.storePodcastRss(url, response)
    }
}