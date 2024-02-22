@file:OptIn(ExperimentalMaterial3Api::class)

package com.kyawzinlinn.speccomparer.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyawzinlinn.speccomparer.design_system.theme.Inter
import com.kyawzinlinn.speccomparer.domain.model.DisplayCard
import com.kyawzinlinn.speccomparer.domain.utils.ProductType

@Composable
fun HomeScreen(
    displayCards: List<DisplayCard>,
    modifier: Modifier = Modifier,
    onNavigateSearch: (ProductType, String) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(displayCards) {
                ProductItemCard(
                    displayCard = it,
                    onCardClick = { type, title ->
                        onNavigateSearch(type, title)
                    }
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

    Card(modifier = modifier, onClick = {onCardClick(displayCard.type, displayCard.title)}) {
        Column (modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.Start) {
            Icon(painter = painterResource(displayCard.icon), contentDescription = null)
            Spacer(Modifier.height(8.dp))
            Text(
                text = displayCard.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = displayCard.description,
                fontSize = 12.sp,
                letterSpacing = 0.1.sp,
                lineHeight = 16.sp
            )
            Spacer(Modifier.height(8.dp))
            IconButton(onClick = {onCardClick(displayCard.type, displayCard.title)}, modifier = Modifier.align(Alignment.End)) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
            }
        }
    }
}