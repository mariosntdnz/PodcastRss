package com.example.podcastrss.application

import com.example.podcastrss.models.Episode
import com.example.podcastrss.models.PodcastRss
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface PodcastPlayerMemoryCache {
    suspend fun storeEp(key: String, ep: Episode)
    fun getEp(key: String): StateFlow<Episode?>
}

class PodcastPlayerMemoryCacheImpl: PodcastPlayerMemoryCache {

    private val podcastPlayerMemoryCache = HashMap<String, MutableStateFlow<Episode>>()

    override suspend fun storeEp(key: String, ep: Episode) {
        val oldEp = podcastPlayerMemoryCache[key]
        if (oldEp == null) {
            podcastPlayerMemoryCache[key] = MutableStateFlow(ep)
        } else {
            podcastPlayerMemoryCache[key]?.emit(ep)
        }
    }

    override fun getEp(key: String): StateFlow<Episode?> {
        return podcastPlayerMemoryCache[key] ?: MutableStateFlow(null)
    }

}