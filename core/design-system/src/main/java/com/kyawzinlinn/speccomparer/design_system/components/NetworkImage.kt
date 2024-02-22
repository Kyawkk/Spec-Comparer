package com.kyawzinlinn.speccomparer.design_system.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kyawzinlinn.speccomparer.domain.utils.BASE_URL
import com.kyawzinlinn.speccomparer.domain.utils.IMG_PREFIX
import com.kyawzinlinn.speccomparer.domain.utils.ImageUrlBuilder

@Composable
fun NetworkImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    onErrorItemRemove: () -> Unit = {},
    onRetrySuccess: () -> Unit,
    onRetry: () -> Unit = {},
) {
    val TAG = "NetworkImage"
    var url by remember { mutableStateOf(imageUrl) }
    var errorCount by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    LaunchedEffect(imageUrl) {
        url = if (imageUrl.contains("https")) imageUrl else "$IMG_PREFIX/$imageUrl"
    }

    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(context)
            .data(url)
            .crossfade(true)
            .build(),
        onError = {
            if (errorCount == 2) onErrorItemRemove()
            else {
                onRetry()
                url = ImageUrlBuilder.buildFailedImageUrl(imageUrl)
                errorCount++
            }
        },
        onSuccess = { if (errorCount != 0) onRetrySuccess() },
        contentDescription = null,
    )
}