package com.example.podcastrss.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.test.platform.tracing.Tracer.Span
import com.example.podcastrss.R
import com.example.podcastrss.ui.navigation.NavRoutes.configScreenRoute
import com.example.podcastrss.ui.navigation.NavRoutes.podcastDetailsRoute
import com.example.podcastrss.ui.utils.IconClickable
import com.example.podcastrss.ui.utils.SpaceGrow
import com.example.podcastrss.ui.utils.SpaceHeight
import com.example.podcastrss.ui.utils.SpaceWidth
import com.example.podcastrss.viewModel.PodcastRssSearchViewModel
import org.koin.androidx.compose.getViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun SearchPodcastScreen(
    navController: NavController
) {
    val viewModel = getViewModel<PodcastRssSearchViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.searchSuccess) {
        if (state.searchSuccess) {
            val encodedUrl =
                URLEncoder.encode(state.currentSearch, StandardCharsets.UTF_8.toString())
            navController.navigate("$podcastDetailsRoute/$encodedUrl")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onDispose()
        }
    }

    if (state.hasError) {
        ErrorSearchScreen(
            msg = state.errorMsg,
            retry = viewModel::retry
        )
    } else {
        DefaultSearchScreen(
            currentSearch = state.currentSearch,
            historySearch = state.history,
            isLoading = state.isLoading,
            updateSearch = viewModel::updateSearch,
            searchPodcastRss = viewModel::searchPodcastRss,
            clearSearch = viewModel::clearSearch,
            deleteHistorySearch = viewModel::deleteSearch,
            searchFromHistory = viewModel::searchFromHistory,
            navigateToConfig = {
                navController.navigate(configScreenRoute)
            }
        )
    }
}

@Composable
fun DefaultSearchScreen(
    currentSearch: String,
    historySearch: List<String>,
    isLoading: Boolean,
    updateSearch: (String) -> Unit,
    searchPodcastRss: () -> Unit,
    clearSearch: () -> Unit,
    deleteHistorySearch: (String) -> Unit,
    searchFromHistory: (String) -> Unit,
    navigateToConfig: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var menuIsExpanded by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(historySearch) {
        menuIsExpanded = historySearch.isNotEmpty()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp)
                .clickable(onClick = navigateToConfig)
        )

        Column(
            modifier = Modifier
                .fillMaxHeight(.20f)
                .align(Alignment.TopStart),
            verticalArrangement = Arrangement.Bottom
        ) {
            OutlinedTextField(
                value = currentSearch,
                onValueChange = { search ->
                    updateSearch(search)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(
                        16.dp,
                        shape = RoundedCornerShape(100),
                        spotColor = Color.Red,
                        ambientColor = Color.Red
                    ),
                shape = RoundedCornerShape(100),
                singleLine = true,
                colors = TextFieldDefaults.colors().copy(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedLabelColor = Color.Black.copy(0.75f),
                    unfocusedLabelColor = Color.Black.copy(0.75f)
                ),
                leadingIcon = if (currentSearch.isEmpty()) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = ""
                        )
                    }
                } else null,
                trailingIcon = {
                    if (currentSearch.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "",
                            modifier = Modifier
                                .clickable(onClick = clearSearch)
                        )
                    }
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search_podcast_placeholder)
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        searchPodcastRss()
                        focusManager.clearFocus()
                    }
                )
            )
            DropdownMenu(
                expanded = menuIsExpanded,
                onDismissRequest = {
                    menuIsExpanded = false
                },
                properties = PopupProperties(focusable = false),
                modifier = Modifier.fillMaxWidth()
            ) {
                historySearch.forEach { history ->
                    DropdownMenuItem(text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = history, modifier = Modifier.weight(1f))
                            SpaceWidth(w = 4.dp)
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "",
                                modifier = Modifier
                                    .clickable {
                                        deleteHistorySearch(history)
                                    }
                            )
                        }
                    }, onClick = {
                        searchFromHistory(history)
                        menuIsExpanded = false
                    })
                }
            }
        }

        Button(
            onClick = {
                searchPodcastRss()
            },
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = Color.Red
            ),
            modifier = Modifier
                .fillMaxWidth(.5f)
                .align(Alignment.Center)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.search_label_action),
                    fontSize = 22.sp
                )
                SpaceWidth(w = 8.dp)
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "",
                    modifier = Modifier
                        .clickable {
                            searchPodcastRss()
                            focusManager.clearFocus()
                        }
                )
            }
        }

        if (isLoading) {
            Box(
                Modifier
                    .fillMaxHeight(0.7f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.Red,
                    strokeWidth = 4.dp
                )
            }

        }

    }
}

@Composable
fun ErrorSearchScreen(
    msg: String,
    retry: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = msg,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            SpaceHeight(h = 16.dp)
            Button(
                onClick = retry,
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = Color.Red
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.retry),
                        fontSize = 16.sp
                    )
                    SpaceWidth(w = 8.dp)
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "",
                        modifier = Modifier
                            .clickable(onClick = retry)
                    )
                }
            }
        }
    }
}