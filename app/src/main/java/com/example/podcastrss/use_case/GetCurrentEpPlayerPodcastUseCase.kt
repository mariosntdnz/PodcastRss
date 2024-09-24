package com.example.podcastrss.use_case

import com.example.podcastrss.models.Episode
import com.example.podcastrss.repository.PodcastPlayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class GetCurrentEpPlayerPodcastResult(
    val currentEp: Episode?,
    val hasPrevEp: Boolean,
    val hasNextEp: Boolean
)

class GetCurrentEpPlayerPodcastUseCase(
    private val podcastPlayerRepository: PodcastPlayerRepository
) {
    suspend operator fun invoke(
        urlPodcast: String,
        guidInitialEp: String,
        scope: CoroutineScope
    ): StateFlow<GetCurrentEpPlayerPodcastResult>{
        return combine(
            flow = podcastPlayerRepository.getCurrentEp(urlPodcast, guidInitialEp),
            flow2 = podcastPlayerRepository.hasNextEp(urlPodcast, guidInitialEp).stateIn(scope),
            flow3 = podcastPlayerRepository.hasPrevEp(urlPodcast, guidInitialEp).stateIn(scope)
        ) { ep, next, prev ->
            GetCurrentEpPlayerPodcastResult(
                currentEp = ep,
                hasPrevEp = prev,
                hasNextEp = next
            )
        }.stateIn(scope)
    }

}