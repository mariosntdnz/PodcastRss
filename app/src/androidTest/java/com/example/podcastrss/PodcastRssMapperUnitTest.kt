package com.example.podcastrss

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

// Usa o arquivo podcastrssitunesmock.xml como resposta de teste
// O arquivo é um ecemplo de teste da apple dos padrões de rss do itunes
//fonte: https://help.apple.com/itc/podcasts_connect/#/itcbaf351599

@RunWith(AndroidJUnit4::class)
class PodcastRssMapperUnitTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun verifyChannelFields() = runTest {

        //arrange
        val context = ApplicationProvider.getApplicationContext<Context?>().applicationContext
        val api  = ServiceMockWithRSSFeedSampleRsponse(context)
            .getRssApiServiceMock()

        //action
        val response = api.getPodcastRss("")
        advanceUntilIdle()
        val body = response.body()
        val channel = body?.channel

        //assert
        assertEquals("Hiking Treks",channel?.channelTitle)
        assertEquals("The Sunset Explorers",channel?.author?.first()?.auth) // pode ter author e itunes:author, pegamos apenas 1
        assertEquals(listOf("Sports"),channel?.categoryChannel?.map { it.category }) // pode ter subcategorias mas pegamos só a categoria
        assertEquals(listOf("https://applehosted.podcasts.apple.com/hiking_treks/artwork.png "),channel?.imageChannel?.map { it.href + " " + it.url }) // pode vir na url ou href
        assertEquals("Love to get outdoors and discover nature's treasures? Hiking Treks is the show for you. We review hikes and excursions, review outdoor gear and interview a variety of naturalists and adventurers. Look for new episodes each week.",channel?.channelDescription)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun verifyItemFields() = runTest {

        //arrange
        val context = ApplicationProvider.getApplicationContext<Context?>().applicationContext
        val api  = ServiceMockWithRSSFeedSampleRsponse(context)
            .getRssApiServiceMock()

        //action
        val response = api.getPodcastRss("")
        advanceUntilIdle()
        val body = response.body()
        val channel = body?.channel
        val firstItem = channel?.itemList?.getOrNull(0)

        //assert
        assertEquals("Tue, 8 Jan 2019 01:15:00 GMT",firstItem?.pubDate)
        assertEquals("1079",firstItem?.duration)
        assertEquals(false,firstItem?.explicit)
        assertEquals("D03EEC9B-B1B4-475B-92C8-54F853FA2A22",firstItem?.guid)
        assertEquals("http://example.com/podcasts/everything/AllAboutEverythingEpisode4.mp3",firstItem?.enclosure?.url)
        assertEquals("audio/mpeg",firstItem?.enclosure?.type)
        assertEquals(emptyList<String>(),firstItem?.image?.map { it.href + " " + it.url })
    }
}