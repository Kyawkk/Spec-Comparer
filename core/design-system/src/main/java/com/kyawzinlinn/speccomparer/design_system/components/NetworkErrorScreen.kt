package com.kyawzinlinn.speccomparer.design_system.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyawzinlinn.speccomparer.design_system.R
import com.kyawzinlinn.speccomparer.domain.utils.NetworkError

@Composable
fun NetworkErrorScreen(
    attemptCount: Int,
    message: NetworkError,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    Box (modifier = modifier
        .fillMaxSize()
        .padding(24.dp), contentAlignment = Alignment.Center) {
        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.connection_error),
                modifier = Modifier.size(100.dp),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = message.title,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message.message,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.Black.copy(0.5f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onRetry) {
                Text(text = if (attemptCount == 3) "Open Setting" else "Retry")
            }
        }
    }
}

@Preview (showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NetworkErrorScreenPreview() {
    NetworkErrorScreen(onRetry = {}, attemptCount = 0, message = NetworkError("Network error!","No internet connection. Please check your mobile data or  wifi."))
}