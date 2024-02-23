package com.kyawzinlinn.speccomparer.design_system.components

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.kyawzinlinn.speccomparer.domain.utils.NetworkError
import com.kyawzinlinn.speccomparer.domain.utils.Resource


@Composable
fun <T> handleResponse(
    resource: Resource<T>,
    onRetry: () -> Unit,
    onError: () -> Unit = {},
    onLoading: () -> Unit = {},
    onSuccess: (T) -> Unit
) {
    var showLoading by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var attemptCount by remember { mutableIntStateOf(0) }
    var networkError by remember { mutableStateOf(NetworkError()) }
    val context = LocalContext.current

    LaunchedEffect(resource) {
        when (resource) {
            is Resource.Loading -> {
                onLoading()
                showLoading = true
            }
            is Resource.Success -> {
                showLoading = false
                showError = false

                onSuccess(resource.data)
            }

            is Resource.Error -> {
                onError()
                attemptCount++
                showLoading = false
                showError = true
                networkError = if (attemptCount == 3) NetworkError("Failed multiple times!","You tried multiple times. Enable mobile data or wifi in connection setting.")
                else resource.networkError
            }

            else -> {
                showLoading = false
                showError = false
            }
        }
    }

    if (showLoading) LoadingScreen()
    if (showError) NetworkErrorScreen(
        attemptCount = attemptCount,
        message = networkError,
        onRetry = {
            showError = false
            if (attemptCount != 3) onRetry()
            else {
                attemptCount = 0
                goToSettings(context)
            }
        }
    )
}

private fun goToSettings(context: Context) {
    val intentSettings = Intent()
    intentSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intentSettings.setAction(Settings.ACTION_DATA_ROAMING_SETTINGS)
    context.startActivity(intentSettings)
}