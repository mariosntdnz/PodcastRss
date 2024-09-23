package com.example.podcastrss.ui.screens

import ExpandableText
import android.text.util.Linkify
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.podcastrss.R
import com.example.podcastrss.ui.theme.RedDetails
import com.example.podcastrss.ui.utils.SpaceGrow
import com.example.podcastrss.ui.utils.SpaceHeight
import com.example.podcastrss.ui.utils.SpaceWidth
import com.example.podcastrss.viewModel.PodcastDetailsViewModel
import com.google.android.material.textview.MaterialTextView
import isHTML
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PodcastDetailsScreen(
    url: String,
    navController: NavController
) {
    val viewModel = getViewModel<PodcastDetailsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    var openTextDetails by remember {
        mutableStateOf(false)
    }

    var titleDetails by remember {
        mutableStateOf("")
    }

    var descrDetails by remember {
        mutableStateOf("")
    }

    SideEffect {
        viewModel.showDetail(url)
    }

    state.podcast?.let { pod ->
        LazyColumn {
            item {
                PodcastDetailsHeader(
                    imageUrl = pod.bannerUrl,
                    name = pod.name,
                    description = pod.description,
                    author = pod.author,
                    categories = pod.category,
                    seeMore = { title, descr ->
                        openTextDetails = true
                        titleDetails = title
                        descrDetails = descr
                    }
                )
            }

            items(pod.episodes) { ep ->
                Box(
                    Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    PodcastDetailsEpisodeItem(
                        imageUrl = ep.imageUrl,
                        title = ep.title,
                        description = ep.description,
                        duration = ep.durationLabel,
                        explicit = ep.explicit,
                        date = ep.pubDate,
                        onClick = {},
                        seeMore = { title, descr ->
                            openTextDetails = true
                            titleDetails = title
                            descrDetails = descr
                        }
                    )
                }
                SpaceHeight(h = 4.dp)
            }
        }
    }

    if (openTextDetails) {
        BasicAlertDialog(
            onDismissRequest = {
                openTextDetails = false
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(.75f)
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(4.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row {
                    SpaceGrow()
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            openTextDetails = false
                        }
                    )
                }
                SpaceHeight(h = 4.dp)
                Text(text = titleDetails)
                SpaceHeight(h = 32.dp)
                if (descrDetails.isHTML()) {
                    val spannedText = HtmlCompat.fromHtml(descrDetails, 0)
                    AndroidView(
                        factory = {
                            MaterialTextView(it).apply {
                                autoLinkMask = Linkify.WEB_URLS
                                linksClickable = true
                                setLinkTextColor(RedDetails.toArgb())
                            }
                        },
                        update = {
                            it.text = spannedText
                        }
                    )
                } else {
                    Text(text = descrDetails)
                }
            }
        }
    }
}

@Composable
fun PodcastDetailsHeader(
    imageUrl: String,
    name: String,
    description: String,
    author: String,
    categories: List<String>,
    seeMore: (String, String) -> Unit
) {
    val context = LocalContext.current
    val imageRequest = remember(imageUrl) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .memoryCacheKey(imageUrl)
            .diskCacheKey(imageUrl)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    }

    Column(
        Modifier
            .background(Color.White)
    ) {
        SpaceHeight(h = 16.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SpaceWidth(w = 16.dp)
            AsyncImage(
                model = imageRequest,
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            SpaceWidth(w = 16.dp)
            Column {
                Text(
                    text = name,
                    fontSize = 20.sp,
                    color = Color.Black
                )
                SpaceHeight(h = 8.dp)
                if (description.isHTML()) {
                    val spannedText = HtmlCompat.fromHtml(description, 0)
                    AndroidView(
                        factory = {
                            MaterialTextView(it).apply {
                                // links
                                autoLinkMask = Linkify.WEB_URLS
                                linksClickable = true
                                // setting the color to use forr highlihting the links
                                setLinkTextColor(RedDetails.toArgb())
                            }
                        },
                        update = {
                            it.maxLines = 3
                            it.text = spannedText
                        }
                    )
                } else {
                    ExpandableText(
                        text = description,
                        fontSize = 16.sp,
                        color = Color.Black,
                        maximizedMaxLines = 3,
                        minimizedMaxLines = 3,
                        onClick = {seeMore(name,description)}
                    )
                }
                SpaceHeight(h = 16.dp)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = ""
                    )
                    SpaceWidth(w = 4.dp)
                    Text(
                        text = author,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }
        }
        SpaceHeight(h = 16.dp)
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
        ) {
            categories.forEach { category ->
                SpaceWidth(w = 16.dp)
                SuggestionChip(
                    onClick = {},
                    label = {
                        Text(
                            text = category,
                            color = Color.White
                        )
                    },
                    shape = RoundedCornerShape(100),
                    border = null,
                    colors = SuggestionChipDefaults.suggestionChipColors().copy(
                        containerColor = RedDetails
                    )
                )
            }
        }
        SpaceHeight(h = 16.dp)
        HorizontalDivider(
            color = RedDetails,
            modifier = Modifier.fillMaxWidth()
        )
        SpaceHeight(h = 16.dp)
    }
}

@Composable
fun PodcastDetailsEpisodeItem(
    imageUrl: String,
    title: String,
    description: String,
    duration: String,
    explicit: Boolean,
    date: String,
    onClick: () -> Unit,
    seeMore: (String,String) -> Unit
) {
    val context = LocalContext.current
    val imageRequest = remember(imageUrl) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .memoryCacheKey(imageUrl)
            .diskCacheKey(imageUrl)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                16.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Red,
                ambientColor = Color.Red
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 16.dp
        )
    ) {
        Column(
            Modifier
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = imageRequest,
                    contentDescription = "",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
                SpaceWidth(w = 16.dp)
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ExpandableText(
                            text = title,
                            fontSize = 16.sp,
                            color = Color.Black,
                            maximizedMaxLines = 2,
                            minimizedMaxLines = 2,
                            onClick = {seeMore(title,description)},
                            modifier = Modifier.weight(1f,fill = false)
                        )
                        if (explicit) {
                            SpaceWidth(w = 4.dp)
                            Icon(
                                painter = painterResource(id = R.drawable.ic_explicit),
                                contentDescription = "",
                                tint = Color.Red
                            )
                        }
                    }
                    SpaceHeight(h = 8.dp)
                    if (description.isHTML()) {
                        val spannedText = HtmlCompat.fromHtml(description, 0)
                        AndroidView(
                            factory = {
                                MaterialTextView(it).apply {
                                    // links
                                    autoLinkMask = Linkify.WEB_URLS
                                    linksClickable = true
                                    // setting the color to use forr highlihting the links
                                    setLinkTextColor(RedDetails.toArgb())
                                }
                            },
                            update = {
                                it.maxLines = 3
                                it.text = spannedText
                            }
                        )
                    } else {
                        ExpandableText(
                            text = description,
                            fontSize = 14.sp,
                            color = Color.Black,
                            maximizedMaxLines = 3,
                            minimizedMaxLines = 3,
                            onClick = {seeMore(title,description)}
                        )
                    }

                    SpaceHeight(h = 16.dp)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = ""
                        )
                        SpaceWidth(w = 4.dp)
                        Text(
                            text = date,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }
            }
            if (duration.isNotEmpty()) {
                SpaceHeight(h = 8.dp)
                AssistChip(
                    onClick = onClick,
                    label = {
                        Text(
                            text = duration,
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.PlayArrow,
                            contentDescription = "",
                            tint = Color.Black
                        )
                    },
                    border = null,
                    shape = RoundedCornerShape(100),
                    colors = SuggestionChipDefaults.suggestionChipColors().copy(
                        containerColor = RedDetails
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun PodcastDetailsEpisodeItemPreview() {
    PodcastDetailsEpisodeItem(
        imageUrl = "",
        title = "Title do  podtest",
        description = "Melhor descr do podtest\nmuito bom\n",
        duration = "1h 05 min 10s",
        explicit = true,
        date = "23 de setembro de 2024",
        onClick = {},
        seeMore = {_,_ ->}
    )
}

@Preview
@Composable
fun PodcastDetailsHeaderPreview() {
    PodcastDetailsHeader(
        imageUrl = "",
        name = "PodTeste",
        description = "Podcast legal\nShow\nlegal",
        author = "PodAuthor",
        categories = listOf(
            "Android",
            "Compose"
        ),
        seeMore = {_,_ ->}
    )
}