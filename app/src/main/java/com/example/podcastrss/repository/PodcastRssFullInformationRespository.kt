package com.example.podcastrss.repository

import com.example.podcastrss.datasource.remote.PodcastRssFullInformationRemoteDataSource
import com.example.podcastrss.models.PodcastRss
import com.example.podcastrss.result.ResponseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

interface PodcastRssFullInformationRespository {
    suspend fun getPodcastRssFullInformation(url: String): Flow<ResponseResult<PodcastRss>>
}

class PodcastRssFullInformationRespositoryImpl(
    private val remoteDataSource: PodcastRssFullInformationRemoteDataSource
): PodcastRssFullInformationRespository {

    private val cacheMaxSize = 5

    val searchCache = HashMap<String, Flow<PodcastRss>>()

    override suspend fun getPodcastRssFullInformation(url: String): Flow<ResponseResult<PodcastRss>> {
        searchCache[url]?.let {
            return it.map { data -> ResponseResult.Success(data) }
        }

        val fullInfoFlow = remoteDataSource.getPodcastRssFullInformation(url)
        val lastFlow = fullInfoFlow.last()

        if (lastFlow is ResponseResult.Success) {
            store(url, lastFlow.data)
        }

        return fullInfoFlow
    }

    private fun store(url: String, response: PodcastRss) {
        if (searchCache.keys.size < cacheMaxSize) {
            searchCache[url] = flowOf(response)
        }
    }
}