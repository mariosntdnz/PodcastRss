package com.example.podcastrss.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.podcastrss.ui.navigation.NavRoutes.podcastDetailsRoute
import com.example.podcastrss.ui.navigation.NavRoutes.searchScreenRoute
import com.example.podcastrss.ui.screens.PodcastDetailsScreen
import com.example.podcastrss.ui.screens.SearchPodcastScreen

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = searchScreenRoute
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(searchScreenRoute) {
            SearchPodcastScreen(navController)
        }

        composable(podcastDetailsRoute) {
            PodcastDetailsScreen(navController)
        }
    }
}