package com.example.podcastrss.repository

import com.example.podcastrss.datasource.remote.PodcastRssFullInformationDataSource
import com.example.podcastrss.models.PodcastRss
import com.example.podcastrss.result.ResponseResult
import kotlinx.coroutines.flow.Flow

interface PodcastRssFullInformationRespository {
    fun getPodcastRssFullInformation(url: String): Flow<ResponseResult<PodcastRss>>
}

class PodcastRssFullInformationRespositoryImpl(
    private val dataSource: PodcastRssFullInformationDataSource
): PodcastRssFullInformationRespository {
    override fun getPodcastRssFullInformation(url: String): Flow<ResponseResult<PodcastRss>> {
        return dataSource.getPodcastRssFullInformation(url)
    }
}