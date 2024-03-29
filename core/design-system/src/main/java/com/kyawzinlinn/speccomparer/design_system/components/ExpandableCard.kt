package com.kyawzinlinn.speccomparer.design_system.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyawzinlinn.speccomparer.design_system.extensions.dividerColor

@Composable
fun ExpandableCard(
    title: String,
    modifier: Modifier = Modifier,
    expandable: Boolean = true,
    onExpandedChanged: (Boolean) -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 180f else 360f,
        animationSpec = tween(durationMillis = 400,easing = FastOutLinearInEasing), label = ""
    )

    LaunchedEffect (expandable) {
        if (!expandable) expanded = true
    }

    LaunchedEffect(expanded) {
        onExpandedChanged(expanded)
    }

    Card(modifier = modifier.fillMaxWidth(), onClick = { expanded = if (expandable) !expanded else true }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp
                )
                if (expandable) Icon(
                    modifier = Modifier.rotate(rotationAngle),
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }
            AnimatedVisibility(visible = expanded) {
                Column {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.dividerColor)
                    content()
                }
            }
        }
    }
}