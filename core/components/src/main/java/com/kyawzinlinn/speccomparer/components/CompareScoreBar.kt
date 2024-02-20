package com.kyawzinlinn.speccomparer.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareScoreBar

@Composable
fun CompareScoreBar(
    expanded: Boolean,
    firstDevice: String,
    secondDevice: String,
    compareScoreBar: CompareScoreBar,
    modifier: Modifier = Modifier
) {
    val firstAnimatedProgress by animateFloatAsState(
        targetValue = if(expanded) compareScoreBar.firstSpecValue.replace("%", "").toFloat() / 100f else 0f,
        animationSpec = tween(1000), label = ""
    )

    val secondAnimatedProgress by animateFloatAsState(
        targetValue = if(expanded) compareScoreBar.secondSpecValue.replace("%", "").toFloat() / 100f else 0f,
        animationSpec = tween(1000), label = ""
    )

    Column (modifier = modifier) {
        Text(text = compareScoreBar.name, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Column {
            Row {
                Text(text = firstDevice)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = compareScoreBar.firstSpecName)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { firstAnimatedProgress },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSecondary,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column {
            Row {
                Text(text = secondDevice)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = compareScoreBar.secondSpecName)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { secondAnimatedProgress },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}