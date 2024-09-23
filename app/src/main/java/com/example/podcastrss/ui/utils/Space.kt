package com.example.podcastrss.ui.utils

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun SpaceHeight(h: Dp) {
    Spacer(modifier = Modifier.height(h))
}

@Composable
fun SpaceWidth(w: Dp) {
    Spacer(modifier = Modifier.width(w))
}

@Composable
fun ColumnScope.SpaceGrow(w: Float = 1f) {
    Spacer(modifier = Modifier.weight(w))
}

@Composable
fun RowScope.SpaceGrow(w: Float = 1f) {
    Spacer(modifier = Modifier.weight(w))
}