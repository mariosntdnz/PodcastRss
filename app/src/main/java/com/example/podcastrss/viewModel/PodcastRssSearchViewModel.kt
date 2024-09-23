package com.example.podcastrss.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.podcastrss.models.Podcast
import com.example.podcastrss.use_case.PodcastRssSearchUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PodcastRssSearchState(
    val currentSearch: String,
    val isLoading: Boolean,
    val errorMsg: String,
    val podcast: Podcast?
) {
    val hasError = errorMsg.isNotEmpty()
}

class PodcastRssSearchViewModel(
    private val podcastRssSearchUseCase: PodcastRssSearchUseCase
): ViewModel() {
    val state = MutableStateFlow(
        PodcastRssSearchState(
            isLoading = false,
            errorMsg = "",
            podcast = null,
            currentSearch = ""
        )
    )

    fun searchPodcastRss() {
        viewModelScope.launch {
            val search = state.value.currentSearch
            if (search.isEmpty()) return@launch

            podcastRssSearchUseCase.invoke(search,this).collect { result ->
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

    fun updateSearch(
        search: String
    ) {
        state.update {
            it.copy(
                currentSearch = search
            )
        }
    }

    fun clearSearch() {
        updateSearch("")
    }

    fun retry() {
        state.update {
            it.copy(
                errorMsg = ""
            )
        }
    }
}