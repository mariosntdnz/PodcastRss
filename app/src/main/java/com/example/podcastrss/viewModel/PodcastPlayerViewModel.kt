package com.example.podcastrss.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.podcastrss.models.Episode
import com.example.podcastrss.repository.PodcastPlayerRepository
import com.example.podcastrss.use_case.GetCurrentEpPlayerPodcastUseCase
import com.example.podcastrss.use_case.getEpDurationLabel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PlayerPodcastState(
    val isPlay: Boolean,
    val currentEp: Episode?,
    val hasNextEp: Boolean,
    val hasPrevEp: Boolean,
    val currentProgress: Float,
    val currentProgressUserSeek: Float,
    val currentDuration: Long
) {
    val durationLabel get() = currentDuration.toString().getEpDurationLabel()
}

class PodcastPlayerViewModel(
    private val getCurrentEpPlayerPodcastUseCase: GetCurrentEpPlayerPodcastUseCase,
    private val playerRepository: PodcastPlayerRepository
) : ViewModel() {

    val state = MutableStateFlow(PlayerPodcastState(true, null, true, true, 0f,0f, 0L))

    fun initPlayer(
        urlPodcast: String,
        guid: String
    ) {
        viewModelScope.launch {
            getCurrentEpPlayerPodcastUseCase.invoke(urlPodcast, guid, this).collect { result ->
                state.update {
                    it.copy(
                        currentEp = result.currentEp,
                        hasNextEp = result.hasNextEp,
                        hasPrevEp = result.hasPrevEp
                    )
                }
            }
        }
    }

    fun nextEp(urlPodcast: String) {
        if (state.value.hasNextEp) {
            viewModelScope.launch {
                state.value.currentEp?.id?.let { playerRepository.nextEp(urlPodcast, it) }
            }
        }
    }

    fun prevEp(urlPodcast: String) {
        if (state.value.hasPrevEp) {
            viewModelScope.launch {
                state.value.currentEp?.id?.let { playerRepository.prevEp(urlPodcast, it) }
            }
        }
    }

    fun playOrPause() {
        state.update {
            val isPlay = it.isPlay
            it.copy(isPlay = !isPlay)
        }
    }

    fun replayTenSec() {
        setCurrentProgressUserSeek( state.value.currentProgress - (10f/ state.value.currentEp?.duration!!))
    }

    fun forwardTenSec() {
        setCurrentProgressUserSeek( state.value.currentProgress + (10f/ state.value.currentEp?.duration!!))
    }

    fun setCurrentProgress(
        progress: Float
    ) {
        state.update {
            it.copy(
                currentProgress = progress
            )
        }
    }
    fun setCurrentProgressUserSeek(
        progress: Float
    ) {
        state.update {
            it.copy(
                currentProgressUserSeek = progress
            )
        }
    }

    fun setCurrentDuration(duration: Long) {
        state.update {
            val durationInSec = duration/1000
            it.copy(
                currentDuration = durationInSec,
                currentProgress = durationInSec / (state.value.currentEp?.duration?.toFloat() ?: 1f)
            )
        }
    }
}