@file:OptIn(ExperimentalMaterial3Api::class)

package com.kyawzinlinn.speccomparer.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TopBar(
    title: String, canNavigateBack: Boolean, navigateUp: () -> Unit
) {
    CenterAlignedTopAppBar(title = {
        AnimatedContent(targetState = title, transitionSpec = {
            fadeIn() + slideInVertically(
                animationSpec = tween(400),
                initialOffsetY = { it }) togetherWith fadeOut(
                animationSpec = tween(
                    200
                )
            ) + slideOutVertically {
                -it
            }
        }) { newTitle ->
            Text(
                text = newTitle, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
            )
        }
    }, navigationIcon = {
        if (canNavigateBack) {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Default.ArrowBack, contentDescription = null
                )
            }
        }
    })
}