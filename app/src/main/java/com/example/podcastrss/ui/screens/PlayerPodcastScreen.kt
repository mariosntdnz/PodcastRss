package com.example.podcastrss.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.example.podcastrss.R
import com.example.podcastrss.ui.theme.RedDetails
import com.example.podcastrss.ui.utils.IconClickable
import com.example.podcastrss.ui.utils.SpaceGrow
import com.example.podcastrss.ui.utils.SpaceHeight
import com.example.podcastrss.ui.utils.SpaceWidth
import com.example.podcastrss.viewModel.PodcastPlayerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerPodcastScreen(
    url: String,
    guid: String
) {

    val viewModel = getViewModel<PodcastPlayerViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SideEffect {
        viewModel.initPlayer(url,guid)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scroll by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = -1f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    var hasOverFlow by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer
            .Builder(context)
            .build()
    }

    val mediaItem by remember(state.currentEp) {
        mutableStateOf(
            MediaItem.Builder()
                .setUri(state.currentEp?.episodeUrl ?: "")
                .setMediaId(state.currentEp?.id ?: "")
                .setMediaMetadata(MediaMetadata.Builder().setDisplayTitle(state.currentEp?.title?:"").build())
                .build()
        )
    }

    LaunchedEffect(Unit) {
        this.launch {
            while (true) {
                delay(1000)
                viewModel.setCurrentDuration(exoPlayer.currentPosition)
            }
        }
    }

    LaunchedEffect(mediaItem) {
        viewModel.setCurrentProgress(0f)
        exoPlayer.seekTo(0L)
        exoPlayer.playWhenReady = true
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == ExoPlayer.STATE_ENDED) {
                    viewModel.nextEp(url)
                }
            }
        })
    }

    LaunchedEffect(state.isPlay) {
        if (!exoPlayer.isPlaying && state.isPlay) {
            exoPlayer.play()
        } else {
            exoPlayer.pause()
        }
    }


    LaunchedEffect(state.currentProgressUserSeek) {
        exoPlayer.seekTo((exoPlayer.duration * state.currentProgressUserSeek).toLong())
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            SpaceHeight(h = 48.dp)
            Text(
                state.currentEp?.title ?: "",
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (hasOverFlow) {
                            Modifier
                                .offset(x = Dp(500 * scroll))
                        } else {
                            Modifier
                                .padding(start = 32.dp)
                        }
                    ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = {
                    hasOverFlow = it.hasVisualOverflow
                },
                fontSize = 24.sp
            )

            AsyncImage(
                model = state.currentEp?.imageUrl ?: "",
                contentDescription = "",
                Modifier
                    .aspectRatio(1f)
                    .padding(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(16.dp)
            )

            Column {
                Slider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    value = state.currentProgress,
                    valueRange = 0f..1f,
                    onValueChange = {
                        viewModel.setCurrentProgressUserSeek(it)
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Red,
                        activeTrackColor = Color.Red,
                        inactiveTickColor = Color.Red,
                        activeTickColor = Color.Red,
                        inactiveTrackColor = RedDetails
                    ),
                    thumb = {
                        Box(
                            Modifier
                                .width(16.dp)
                                .height(16.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                        )
                    },
                    track = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(state.currentProgress)
                                    .height(4.dp)
                                    .background(Color.Red, RoundedCornerShape(100))
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .background(RedDetails, RoundedCornerShape(100))
                            )
                        }
                    }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    Text(
                        text = state.durationLabel,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                    SpaceGrow()
                    Text(
                        text = state.currentEp?.durationLabel ?: "",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )

                }

                SpaceHeight(h = 32.dp)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    SpaceWidth(w = 24.dp)
                    IconClickable(
                        id = R.drawable.ic_replay_10,
                        onClick = {
                            viewModel.replayTenSec()
                        },
                        color = Color.White,
                        size = 48.dp
                    )
                    IconClickable(
                        id = R.drawable.ic_prev,
                        onClick = {
                            viewModel.prevEp(url)
                        },
                        color = Color.White,
                        size = 48.dp
                    )
                    Crossfade(targetState = state.isPlay, label = "") { isPlay ->
                        if (isPlay) {
                            IconClickable(
                                id = R.drawable.ic_pause,
                                onClick = viewModel::playOrPause,
                                color = Color.White,
                                size = 64.dp
                            )
                        } else {
                            IconClickable(
                                id = R.drawable.ic_play,
                                onClick = viewModel::playOrPause,
                                color = Color.White,
                                size = 64.dp
                            )
                        }
                    }
                    IconClickable(
                        id = R.drawable.ic_next,
                        onClick = {
                            viewModel.nextEp(url)
                        },
                        color = Color.White,
                        size = 48.dp
                    )
                    IconClickable(
                        id = R.drawable.ic_next_time_10,
                        onClick = {
                            viewModel.forwardTenSec()
                        },
                        color = Color.White,
                        size = 48.dp
                    )
                    SpaceWidth(w = 24.dp)
                }
            }
            SpaceHeight(h = 48.dp)
        }
    }

}
