package com.example.podcastrss.repository

import com.example.podcastrss.application.PodcastPlayerMemoryCache
import com.example.podcastrss.application.PodcastResponseMemoryCache
import com.example.podcastrss.models.Episode
import com.example.podcastrss.use_case.toEpisode
import com.example.podcastrss.use_case.toPodcast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

interface PodcastPlayerRepository {
    suspend fun getCurrentEp(podcastUrl: String): StateFlow<Episode?>
    suspend fun setEp(podcastUrl: String, guid: String)
    fun hasPrevEp(podcastUrl: String, guid: String): Flow<Boolean>
    fun hasNextEp(podcastUrl: String, guid: String): Flow<Boolean>
    suspend fun nextEp(podcastUrl: String, guid: String)
    suspend fun prevEp(podcastUrl: String, guid: String)
}

class PodcastPlayerRepositoryImpl(
    private val playerPodcastMemoryCache: PodcastPlayerMemoryCache,
    private val podcastResponseMemoryCache: PodcastResponseMemoryCache
): PodcastPlayerRepository {

    override suspend fun getCurrentEp(podcastUrl: String): StateFlow<Episode?> {
        return playerPodcastMemoryCache.getEp(podcastUrl)
    }

    override suspend fun setEp(podcastUrl: String, guid: String) {
        val podcast = podcastResponseMemoryCache.getPodcastRss(podcastUrl)?.value
        val eps = podcast?.channel?.itemList
        val myEp = eps?.firstOrNull { it.guid == guid }
        val ep = myEp?.let {
            myEp.toEpisode(
                podcast
                    .channel?.imageChannel?.map {
                        it.url.ifEmpty { it.href }
                    }?.firstOrNull() ?: ""
            )
        }
        if (ep != null) {
            playerPodcastMemoryCache.storeEp(podcastUrl, ep)
        }
    }

    override fun hasPrevEp(podcastUrl: String, guid: String): Flow<Boolean> {
        return playerPodcastMemoryCache.getEp(podcastUrl).map {
            it?.id != podcastResponseMemoryCache.getPodcastRss(podcastUrl)?.value?.toPodcast()?.episodes?.firstOrNull()?.id
        }
    }

    override fun hasNextEp(podcastUrl: String, guid: String): Flow<Boolean> {
        return playerPodcastMemoryCache.getEp(podcastUrl).map {
            it?.id != podcastResponseMemoryCache.getPodcastRss(podcastUrl)?.value?.toPodcast()?.episodes?.lastOrNull()?.id
        }
    }

    override suspend fun nextEp(podcastUrl: String, guid: String) {
        val podcast = podcastResponseMemoryCache.getPodcastRss(podcastUrl)?.value
        val eps = podcast?.toPodcast()?.episodes
        val index = eps?.indexOfFirst { it.id == guid }!!
        playerPodcastMemoryCache.storeEp(podcastUrl, eps[index + 1])
    }

    override suspend fun prevEp(podcastUrl: String, guid: String) {
        val podcast = podcastResponseMemoryCache.getPodcastRss(podcastUrl)?.value
        val eps = podcast?.toPodcast()?.episodes
        val index = eps?.indexOfFirst { it.id == guid }!!
        playerPodcastMemoryCache.storeEp(podcastUrl, eps[index - 1])
    }

}