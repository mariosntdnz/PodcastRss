package com.example.podcastrss.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.podcastrss.models.Podcast
import com.example.podcastrss.use_case.PodcastRssSearchUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PodcastRssSearchState(
    val isLoading: Boolean,
    val errorMsg: String,
    val podcast: Podcast?
)

class PodcastRssSearchViewModel(
    private val podcastRssSearchUseCase: PodcastRssSearchUseCase
): ViewModel() {
    val state = MutableStateFlow(
        PodcastRssSearchState(
            isLoading = true,
            errorMsg = "",
            podcast = null
        )
    )

    fun searchPodcastRss(url: String) {
        viewModelScope.launch {
            podcastRssSearchUseCase.invoke(url,this).collect { result ->
                state.update {
                    it.copy(
                        isLoading = result.isLoading,
                        errorMsg = result.errorMsg,
                        podcast = result.podcast
                    )
                }
            }
        }
    }
}