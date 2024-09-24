package com.example.podcastrss.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp

@Composable
fun IconClickable(
    id: Int,
    color: Color = Color.Unspecified,
    size: Dp = Dp.Unspecified,
    onClick: () -> Unit
) {
    Icon(
        painter = painterResource(id = id),
        contentDescription = "",
        tint = color,
        modifier = Modifier
            .then(
                if (size == Dp.Unspecified) {
                    Modifier
                } else {
                    Modifier.size(size)
                }
            )
            .clickable(onClick = onClick)
    )
}