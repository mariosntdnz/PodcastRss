package com.example.podcastrss.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.podcastrss.di.viewmodels_module.viewModelModule
import com.example.podcastrss.repository.SearchHistoryRepository
import com.example.podcastrss.use_case.PodcastRssSearchUseCase
import com.example.podcastrss.use_case.PodcastSearchTypeResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PodcastRssSearchState(
    val currentSearch: String,
    val searchTypeResult: PodcastSearchTypeResult?,
    val history: List<String> = emptyList()
) {
    val hasError get() = searchTypeResult is PodcastSearchTypeResult.Error
    val isLoading get() = searchTypeResult is PodcastSearchTypeResult.Loading
    val searchSuccess get() = searchTypeResult is PodcastSearchTypeResult.Success
    val errorMsg get() = if (hasError) (searchTypeResult as PodcastSearchTypeResult.Error).msg else ""
}

class PodcastRssSearchViewModel(
    private val podcastRssSearchUseCase: PodcastRssSearchUseCase,
    private val historyRepository: SearchHistoryRepository
): ViewModel() {
    val state = MutableStateFlow(
        PodcastRssSearchState(
            currentSearch = "",
            searchTypeResult = null
        )
    )

    var job: Job? = null

    fun searchPodcastRss() {
        viewModelScope.launch {
            val search = state.value.currentSearch
            if (search.isEmpty()) return@launch

            historyRepository.insertSearch(search)

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

        job?.cancel()
        job = viewModelScope.launch {
            historyRepository.getHistorySearch(search).collect { result ->
                state.update {
                    it.copy(
                        history = result
                    )
                }
            }
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

    fun deleteSearch(search: String) {
        viewModelScope.launch {
            historyRepository.deleteSearch(search)
            state.update {
                it.copy(
                    currentSearch = ""
                )
            }
        }
    }

    fun searchFromHistory(search: String) {
        viewModelScope.launch {
            podcastRssSearchUseCase.invoke(search,this).collect { result ->
                state.update {
                    it.copy(
                        searchTypeResult = result.result,
                        currentSearch = search
                    )
                }
            }
        }
    }

    fun onDispose() {
        state.update {
            PodcastRssSearchState(
                currentSearch = "",
                searchTypeResult = null
            )
        }
    }
}