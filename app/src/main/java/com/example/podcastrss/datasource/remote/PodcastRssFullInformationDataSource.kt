package com.example.podcastrss.datasource.remote

import com.example.podcastrss.models.PodcastRss
import com.example.podcastrss.result.ResponseResult
import com.example.podcastrss.service.RssApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface PodcastRssFullInformationDataSource {
    fun getPodcastRssFullInformation(url: String): Flow<ResponseResult<PodcastRss>>
}

class PodcastRssFullInformationDataSourceImpl(
    private val api: RssApiService
) : PodcastRssFullInformationDataSource {
    override fun getPodcastRssFullInformation(url: String): Flow<ResponseResult<PodcastRss>> =
        flow {
            emit(ResponseResult.Loading)
            val baseUrl = url.replaceAfterLast("/", "")
            val endPoint = url.substringAfterLast("/")
            val rssService = api.getRssApiService(baseUrl)
            val request = rssService.getPodcastRss(endPoint)
            if (request.isSuccessful) {
                val result = request.body()?.let { body ->
                    ResponseResult.Success(
                        data = body
                    )
                } ?: ResponseResult.Empty

                emit(result)
            } else {
                emit(ResponseResult.Error(request.message()))
            }
        }
}