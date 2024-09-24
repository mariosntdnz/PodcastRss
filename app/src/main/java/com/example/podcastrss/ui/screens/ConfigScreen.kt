package com.example.podcastrss.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.example.podcastrss.R
import com.example.podcastrss.ui.utils.SpaceWidth

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ConfigScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                context.imageLoader.diskCache?.clear()
                context.imageLoader.memoryCache?.clear()
                Toast.makeText(context,"Cache foi limpa", Toast.LENGTH_LONG).show()
            },
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = Color.Red
            ),
            modifier = Modifier
                .fillMaxWidth(.5f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.clean_cache),
                    fontSize = 22.sp
                )
                SpaceWidth(w = 8.dp)
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "",
                    tint = Color.White
                )
            }
        }
    }
}