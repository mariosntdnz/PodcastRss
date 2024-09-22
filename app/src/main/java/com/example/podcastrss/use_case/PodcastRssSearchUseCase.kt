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

data class PodcastRssFullInformationResult(
    val isLoading: Boolean,
    val errorMsg: String,
    val podcast: Podcast?
)

class PodcastRssSearchUseCase(
    private val podcastRssFullInformationRespository: PodcastRssFullInformationRespository
) {
     suspend operator fun invoke(
        url: String,
        scope: CoroutineScope
    ): StateFlow<PodcastRssFullInformationResult> {
        return podcastRssFullInformationRespository.getPodcastRssFullInformation(url).map {
            PodcastRssFullInformationResult(
                isLoading = it is ResponseResult.Loading,
                errorMsg = if (it is ResponseResult.Error) it.msg.ifEmpty { "Error!" } else "",
                podcast = if (it is ResponseResult.Success) it.data.toPodcast() else null
            )
        }.stateIn(scope)
    }
}

private fun PodcastRss.toPodcast(): Podcast? {
    val rssChannel = this.channel
    return rssChannel?.let { channel ->

        val podcastBanner = channel.imageChannel?.map {
            it.url.ifEmpty { it.href }
        }?.first() ?: ""

        Podcast(
            name = channel.channelTitle ?: "",
            description = channel.channelDescription,
            bannerUrl = podcastBanner,
            author = channel.author?.first()?.auth ?: "",
            category = channel.categoryChannel?.map { it.category } ?: emptyList(),
            episodes = channel.itemList?.map { item ->
                Episode(
                    id = item.guid,
                    title = item.title?.first()?.title ?: "",
                    description = item.description,
                    duration = item.duration.getEpDurationInSeconds(),
                    durationLabel = item.duration.getEpDurationLabel(),
                    imageUrl = item.image?.map { it.url.ifEmpty { it.href } }?.first() ?: podcastBanner,
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
            sum += s.toLong() * 60.0.pow(index).toLong()
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
    } else {
        String.format("%02ds", sec)
    }
}