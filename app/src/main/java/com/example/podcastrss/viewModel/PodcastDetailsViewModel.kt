package com.example.podcastrss.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.podcastrss.models.Podcast
import com.example.podcastrss.use_case.ShowPodcastDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PodcastDetailsState(
    val podcast: Podcast?
)

class PodcastDetailsViewModel(
    private val showPodcastDetailsUseCase: ShowPodcastDetailsUseCase
): ViewModel() {

    val state = MutableStateFlow(
        PodcastDetailsState(
            podcast = null
        )
    )

    fun showDetail(url: String) {
        viewModelScope.launch {
            showPodcastDetailsUseCase.invoke(url,this).collect { result ->
                state.update {
                    it.copy(
                        podcast = result.podcast
                    )
                }
            }
        }
    }

}