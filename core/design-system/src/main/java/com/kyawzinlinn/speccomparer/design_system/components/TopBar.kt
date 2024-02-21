@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.kyawzinlinn.speccomparer.design_system.components

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
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TopBar(
    title: String,
    canNavigateBack: Boolean,
    showTrailingIcon: Boolean,
    onTrailingIconClick: () -> Unit = {},
    navigateUp: () -> Unit
) {
    var canNavigateBackValue by remember { mutableStateOf(false) }
    var showTrailingIconValue by remember { mutableStateOf(false) }

    LaunchedEffect(canNavigateBackValue, showTrailingIconValue) {
        canNavigateBackValue = canNavigateBack
        showTrailingIconValue = showTrailingIcon
    }

    CenterAlignedTopAppBar(title = {
        AnimatedContent(
            targetState = title,
            transitionSpec = {
                fadeIn() + slideInVertically(
                    animationSpec = tween(400),
                    initialOffsetY = { it }) togetherWith fadeOut(
                    animationSpec = tween(
                        200
                    )
                ) + slideOutVertically {
                    -it
                }
            }, label = ""
        ) { newTitle ->
            Text(
                text = newTitle,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                if (canNavigateBack) Icon(
                    imageVector = Icons.Default.ArrowBack, contentDescription = null
                )
                else null
            }
        },
        actions = {
            IconButton(onClick = onTrailingIconClick) {
                if (showTrailingIcon) {
                    Icon(
                        imageVector = Icons.Default.Compare, contentDescription = null
                    )
                } else null
            }
        }
    )
}