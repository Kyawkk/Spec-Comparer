@file:OptIn(ExperimentalMaterial3Api::class)

package com.kyawzinlinn.speccomparer.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyawzinlinn.speccomparer.design_system.displayCards
import com.kyawzinlinn.speccomparer.domain.model.DisplayCard
import com.kyawzinlinn.speccomparer.domain.utils.ProductType

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateSearch: (ProductType, String) -> Unit,
) {
    val density = LocalDensity.current
    var desiredItemMinHeight by remember {
        mutableStateOf(0.dp)
    }

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(displayCards) {
                ProductItemCard(
                    displayCard = it,
                    onCardClick = { type, title ->
                        onNavigateSearch(type, title)
                    },
                    modifier = Modifier
                        .onPlaced {
                            with(density) {
                                if (desiredItemMinHeight < it.size.height.toDp()) {
                                    desiredItemMinHeight = it.size.height.toDp()
                                }
                            }
                        }
                        .defaultMinSize(minHeight = desiredItemMinHeight)
                )
            }
        }
    }
}

@Composable
private fun ProductItemCard(
    displayCard: DisplayCard,
    onCardClick: (ProductType, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var desiredItemMinHeight by remember {
        mutableStateOf(0.dp)
    }

    val context = LocalContext.current
    val title = context.resources.getString(displayCard.title)
    Card(
        modifier = modifier,
        onClick = { onCardClick(displayCard.type,title) }
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.Start) {
            Icon(painter = painterResource(displayCard.icon), contentDescription = null)
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(id = displayCard.title),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(id = displayCard.description),
                fontSize = 13.sp,
                letterSpacing = 0.1.sp,
                lineHeight = 16.sp
            )
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick = { onCardClick(displayCard.type, title) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
            }
        }
    }
}