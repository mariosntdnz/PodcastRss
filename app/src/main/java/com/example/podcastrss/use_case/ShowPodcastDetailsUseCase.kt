package com.example.podcastrss.use_case

import com.example.podcastrss.models.Episode
import com.example.podcastrss.models.Podcast
import com.example.podcastrss.models.PodcastRss
import com.example.podcastrss.repository.PodcastRssFullInformationRespository
import com.example.podcastrss.result.ResponseResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.math.pow
import kotlin.time.DurationUnit
import kotlin.time.toDuration

data class ShowPodcastDetailsResult(
    val podcast: Podcast?
)

class ShowPodcastDetailsUseCase(
    private val podcastRssFullInformationRespository: PodcastRssFullInformationRespository
) {
    suspend operator fun invoke(
        url: String,
        scope: CoroutineScope
    ): StateFlow<ShowPodcastDetailsResult> {
        return podcastRssFullInformationRespository.getPodcastRssFullInformation(url).map {
            ShowPodcastDetailsResult(
                podcast = (it as? ResponseResult.Success)?.data?.toPodcast()
            )
        }.stateIn(scope)
    }
}


private fun PodcastRss.toPodcast(): Podcast? {
    val rssChannel = this.channel
    return rssChannel?.let { channel ->

        val podcastBanner = channel.imageChannel?.map {
            it.url.ifEmpty { it.href }
        }?.firstOrNull() ?: ""

        Podcast(
            name = channel.channelTitle ?: "",
            description = channel.channelDescription,
            bannerUrl = podcastBanner,
            author = channel.author?.firstOrNull()?.auth ?: "",
            category = channel.categoryChannel?.map { it.category }?.distinct() ?: emptyList(),
            episodes = channel.itemList?.map { item ->
                Episode(
                    id = item.guid,
                    title = item.title?.firstOrNull()?.title ?: "",
                    description = item.description,
                    duration = item.duration.getEpDurationInSeconds(),
                    durationLabel = item.duration.getEpDurationLabel(),
                    imageUrl = item.image?.map { it.url.ifEmpty { it.href } }?.firstOrNull() ?: podcastBanner,
                    pubDate = item.pubDate,
                    explicit = item.explicit,
                    episodeUrl = item.enclosure?.url ?: ""
                )
            } ?: emptyList()
        )
    }
}

private fun String.getEpDurationInSeconds(): Long {
    return toLongOrNull() ?: run {
        var sum = 0L
        this.split(":").reversed().forEachIndexed { index, s ->
            sum += (s.toLongOrNull() ?: 0L) * 60.0.pow(index).toLong()
        }
        return sum
    }
}

private fun String.getEpDurationLabel(): String {
    val duration = getEpDurationInSeconds().toDuration(DurationUnit.SECONDS)
    val  hour = duration.inWholeSeconds / 3600
    val  min = (duration.inWholeSeconds  % 3600) / 60
    val  sec = duration.inWholeSeconds  % 60

    return if (hour > 0) {
        String.format("%02dh %02dmin %02ds", hour, min, sec)
    } else if (min > 0) {
        String.format("%02dmin %02ds", min, sec)
    } else if (sec > 0){
        String.format("%02ds", sec)
    } else {
        ""
    }
}