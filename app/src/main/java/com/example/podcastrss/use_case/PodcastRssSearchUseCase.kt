package com.example.podcastrss.use_case

import com.example.podcastrss.models.Episode
import com.example.podcastrss.models.Podcast
import com.example.podcastrss.models.PodcastRss
import com.example.podcastrss.repository.PodcastRssFullInformationRespository
import com.example.podcastrss.result.ResponseResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.math.pow
import kotlin.time.DurationUnit
import kotlin.time.toDuration

sealed class PodcastSearchTypeResult() {
    data object Loading: PodcastSearchTypeResult()
    data class Error(val msg: String): PodcastSearchTypeResult()
    data object Success: PodcastSearchTypeResult()
}

data class PodcastSearchResult(
    val result: PodcastSearchTypeResult
)

class PodcastRssSearchUseCase(
    private val podcastRssFullInformationRespository: PodcastRssFullInformationRespository
) {
     suspend operator fun invoke(
        url: String,
        scope: CoroutineScope
    ): StateFlow<PodcastSearchResult> {
        return podcastRssFullInformationRespository.getPodcastRssFullInformation(url).map {
            PodcastSearchResult(
                result = when (it) {
                    is ResponseResult.Error -> PodcastSearchTypeResult.Error(it.msg)
                    is ResponseResult.Loading -> PodcastSearchTypeResult.Loading
                    is ResponseResult.Success -> PodcastSearchTypeResult.Success
                    is ResponseResult.Empty -> PodcastSearchTypeResult.Error("Erro inesperado")
                }
            )
        }.stateIn(scope)
    }
}
