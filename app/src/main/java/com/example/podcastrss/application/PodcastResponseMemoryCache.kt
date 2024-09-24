package com.example.podcastrss.application

import com.example.podcastrss.models.PodcastRss
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface PodcastResponseMemoryCache {
    suspend fun storePodcastRss(key: String, podcastRss: PodcastRss)
    fun getPodcastRss(key: String): StateFlow<PodcastRss>?
}

class PodcastResponseMemoryCacheImpl: PodcastResponseMemoryCache {

    private val podcastRssMemoryCache = HashMap<String, MutableStateFlow<PodcastRss>>()
    private val maxSize = 5

    override suspend fun storePodcastRss(key: String, podcastRss: PodcastRss){
        val oldEp = podcastRssMemoryCache[key]
        if (oldEp == null) {
            if (podcastRssMemoryCache.keys.size >= maxSize) {
                podcastRssMemoryCache.clear()
            }
            podcastRssMemoryCache[key] = MutableStateFlow(podcastRss)
        } else {
            podcastRssMemoryCache[key]?.emit(podcastRss)
        }
    }

    override fun getPodcastRss(key: String): StateFlow<PodcastRss>?{
        return podcastRssMemoryCache[key]
    }

}