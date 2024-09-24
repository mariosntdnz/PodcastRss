package com.example.podcastrss.repository

import com.example.podcastrss.application.PodcastPlayerMemoryCache
import com.example.podcastrss.application.PodcastResponseMemoryCache
import com.example.podcastrss.models.Episode
import com.example.podcastrss.use_case.toEpisode
import com.example.podcastrss.use_case.toPodcast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

interface PodcastPlayerRepository {
    suspend fun getCurrentEp(podcastUrl: String, guid: String): StateFlow<Episode?>
    fun hasPrevEp(podcastUrl: String, guid: String): Flow<Boolean>
    fun hasNextEp(podcastUrl: String, guid: String): Flow<Boolean>
    suspend fun nextEp(podcastUrl: String, guid: String)
    suspend fun prevEp(podcastUrl: String, guid: String)
}

class PodcastPlayerRepositoryImpl(
    private val playerPodcastMemoryCache: PodcastPlayerMemoryCache,
    private val podcastResponseMemoryCache: PodcastResponseMemoryCache
): PodcastPlayerRepository {

    override suspend fun getCurrentEp(podcastUrl: String, guid: String): StateFlow<Episode?> {
        playerPodcastMemoryCache.getEp(podcastUrl)?.let {
            return it
        }

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
        return MutableStateFlow(ep)
    }

    override fun hasPrevEp(podcastUrl: String, guid: String): Flow<Boolean> {
        return podcastResponseMemoryCache.getPodcastRss(podcastUrl)?.map {
            it.toPodcast()?.episodes?.first()?.id != playerPodcastMemoryCache.getEp(podcastUrl)?.value?.id
        } ?: flowOf(false)
    }

    override fun hasNextEp(podcastUrl: String, guid: String): Flow<Boolean> {
        return podcastResponseMemoryCache.getPodcastRss(podcastUrl)?.map {
            it.toPodcast()?.episodes?.last()?.id != playerPodcastMemoryCache.getEp(podcastUrl)?.value?.id
        } ?: flowOf(false)
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