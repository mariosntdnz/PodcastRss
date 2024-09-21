package com.example.podcastrss.models

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
class PodcastRss @JvmOverloads constructor(
    @field:Element(name = "channel", required = false)
    @param:Element(name = "channel", required = false)
    var channel: PodcastRssChannel? = null
)

@Root(name = "channel", strict = false)
class PodcastRssChannel @JvmOverloads constructor(
    @field:Element(name = "title", required = false)
    @param:Element(name = "title", required = false)
    var channelTitle: String? = "",
    @field:Element(name = "description", required = false)
    @param:Element(name = "description", required = false)
    var channelDescription: String = "",
    @field:ElementList(required = false, name = "item", inline = true)
    @param:ElementList(required = false, name = "item", inline = true)
    var itemList: List<PodcastRssItem>? = null
)

@Root(name = "item", strict = false)
class PodcastRssItem @JvmOverloads constructor(
    @field:Element(name = "title", required = false)
    @param:Element(name = "title", required = false)
    @field:Path("item")
    var title: String? = "",
    @field:Element(name = "description", required = false)
    @param:Element(name = "description", required = false)
    var description: String = "",
    @field:Element(name = "link", required = false)
    @param:Element(name = "link", required = false)
    var link: String = ""
)