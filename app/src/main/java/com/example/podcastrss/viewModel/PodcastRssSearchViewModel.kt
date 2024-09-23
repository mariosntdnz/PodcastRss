package com.example.podcastrss.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.podcastrss.use_case.PodcastRssSearchUseCase
import com.example.podcastrss.use_case.PodcastSearchTypeResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PodcastRssSearchState(
    val currentSearch: String,
    val searchTypeResult: PodcastSearchTypeResult?
) {
    val hasError = searchTypeResult is PodcastSearchTypeResult.Error
    val isLoading = searchTypeResult is PodcastSearchTypeResult.Loading
    val searchSuccess = searchTypeResult is PodcastSearchTypeResult.Success
    val errorMsg = if (hasError) (searchTypeResult as PodcastSearchTypeResult.Error).msg else ""
}

class PodcastRssSearchViewModel(
    private val podcastRssSearchUseCase: PodcastRssSearchUseCase
): ViewModel() {
    val state = MutableStateFlow(
        PodcastRssSearchState(
            currentSearch = "",
            searchTypeResult = null
        )
    )

    fun searchPodcastRss() {
        viewModelScope.launch {
            val search = state.value.currentSearch
            if (search.isEmpty()) return@launch

            podcastRssSearchUseCase.invoke(search,this).collect { result ->
                state.update {
                    it.copy(
                        searchTypeResult = result.result
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
                searchTypeResult = null
            )
        }
    }
}