package com.example.podcastrss.models

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.NamespaceList
import org.simpleframework.xml.Root
import org.simpleframework.xml.Text

@Root(name = "rss", strict = false)
class PodcastRss @JvmOverloads constructor(
    @field:Element(name = "channel", required = false)
    @param:Element(name = "channel", required = false)
    var channel: PodcastRssChannel? = null
)

@NamespaceList(
    Namespace(reference = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes")
)
@Root(name = "channel", strict = false)
class PodcastRssChannel @JvmOverloads constructor(
    @field:Element(name = "title", required = false)
    @param:Element(name = "title", required = false)
    var channelTitle: String? = "",

    @field:Element(name = "description", required = false)
    @param:Element(name = "description", required = false)
    var channelDescription: String = "",

    @field:ElementList(name = "image", required = false, inline = true)
    @param:ElementList(name = "image", required = false, inline = true)
    var imageChannel: List<Image>? = emptyList(),

    @field:ElementList(name = "category", required = false, inline = true)
    @param:ElementList(name = "category", required = false, inline = true)
    var categoryChannel: List<Category>? = emptyList(),

    @field:ElementList(name = "author", required = false, inline = true)
    @param:ElementList(name = "author", required = false, inline = true)
    var author: List<Author>? = emptyList(),

    @field:ElementList(required = false, name = "item", inline = true)
    @param:ElementList(required = false, name = "item", inline = true)
    var itemList: List<PodcastRssItem>? = null
)

@Root(name = "item", strict = false)
class PodcastRssItem @JvmOverloads constructor(

    @field:Element(name = "guid", required = false)
    @param:Element(name = "guid", required = false)
    var guid: String = "",

    @field:ElementList(name = "title", required = false, inline = true)
    @param:ElementList(name = "title", required = false, inline = true)
    var title: List<Title>? = null,

    @field:Element(name = "description", required = false)
    @param:Element(name = "description", required = false)
    var description: String = "",

    @field:Element(name = "link", required = false)
    @param:Element(name = "link", required = false)
    var podcastLink: String = "",

    @field:ElementList(name = "image", required = false, inline = true)
    @param:ElementList(name = "image", required = false, inline = true)
    var image: List<Image>? = emptyList(),

    @field:Element(name = "pubDate", required = false)
    @param:Element(name = "pubDate", required = false)
    var pubDate: String = "",

    @field:Element(name = "duration", required = false)
    @param:Element(name = "duration", required = false)
    var duration: String = "",

    @field:Element(name = "explicit", required = false)
    @param:Element(name = "explicit", required = false)
    var explicit: Boolean = false,

    @field:Element(name = "enclosure", required = false)
    @param:Element(name = "enclosure", required = false)
    var enclosure: Enclosure? = null
)

@Root(name="author", strict = false)
class Author @JvmOverloads constructor(
    @field:Text(required = false)
    var auth: String = ""
)

@Root(name="title", strict = false)
class Title @JvmOverloads constructor(
    @field:Text(required = false)
    var title: String = ""
)


@Root(name="image", strict = false)
class Image @JvmOverloads constructor(
    @field:Element(name = "url", required = false)
    var url: String = "",

    @field:Attribute(name = "href", required = false)
    var href: String = ""
)

@Root(name="category", strict = false)
class Category @JvmOverloads constructor(
    @field:Attribute(name = "text", required = false)
    var category: String = ""
)

@Root(name="enclosure", strict = false)
class Enclosure @JvmOverloads constructor(
    @field:Attribute(name = "url", required = false)
    var url: String = "",

    @field:Attribute(name = "type", required = false)
    var type: String = ""
)