package com.example.podcastrss.use_case

import com.example.podcastrss.models.PodcastRss
import com.example.podcastrss.repository.PodcastRssFullInformationRespository
import com.example.podcastrss.result.ResponseResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class PodcastRssFullInformationResult(
    val isLoading: Boolean,
    val errorMsg: String,
    val podcastRss: PodcastRss?
)

class PodcastRssSearchUseCase(
    private val podcastRssFullInformationRespository: PodcastRssFullInformationRespository
) {
     suspend operator fun invoke(
        url: String,
        scope: CoroutineScope
    ): StateFlow<PodcastRssFullInformationResult> {
        return podcastRssFullInformationRespository.getPodcastRssFullInformation(url).map {
            PodcastRssFullInformationResult(
                isLoading = it is ResponseResult.Loading,
                errorMsg = if (it is ResponseResult.Error) it.msg.ifEmpty { "Error!" } else "",
                podcastRss = if (it is ResponseResult.Success) it.data else null
            )
        }.stateIn(scope)
    }
}