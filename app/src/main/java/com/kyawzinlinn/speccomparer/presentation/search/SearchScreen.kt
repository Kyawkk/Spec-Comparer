@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.kyawzinlinn.speccomparer.presentation.search

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kyawzinlinn.speccomparer.domain.model.Product
import com.kyawzinlinn.speccomparer.presentation.UiState
import com.kyawzinlinn.speccomparer.ui.components.LoadingScreen
import com.kyawzinlinn.speccomparer.ui.components.SearchBar
import com.kyawzinlinn.speccomparer.utils.Resource

@Composable
fun SearchScreen(
    uiState: UiState,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onProductItemClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSuggestions by rememberSaveable { mutableStateOf(false) }
    var value by rememberSaveable { mutableStateOf("") }
    var searchResults by rememberSaveable { mutableStateOf(listOf<Product>()) }
    var suggestions by rememberSaveable { mutableStateOf(listOf<Product>()) }

    LaunchedEffect(uiState.searchResults, uiState.suggestions) {

        when (uiState.searchResults) {
            is Resource.Success -> {searchResults = uiState.searchResults.data}
            else -> {}
        }

        when (uiState.suggestions) {
            is Resource.Success -> {suggestions = uiState.suggestions.data}
            else -> {}
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SearchBar(
            input = value,
            modifier = Modifier.padding(16.dp),
            onValueChange = { keyword ->
                onValueChange(keyword)
                showSuggestions = suggestions.isNotEmpty() && keyword.isNotEmpty()
            },
            onSearch = onSearch
        )

        when (uiState.searchResults) {
            is Resource.Loading -> LoadingScreen()
            is Resource.Success -> SearchResultList(
                searchResults, onProductItemClick = onProductItemClick
            )

            is Resource.Error -> {}
            is Resource.Default -> { }
        }

        AnimatedVisibility(
            visible = showSuggestions, enter = fadeIn(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMedium
                )
            ), exit = fadeOut(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMedium
                )
            )
        ) {
            SuggestionList(
                suggestions = suggestions,
                onSuggestionItemClick = {
                    value = it
                    onSearch(it)
                    showSuggestions = false
                }
            )
        }
    }
}

@Composable
fun SearchResultList(
    searchResults: List<Product>,
    onProductItemClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(searchResults) { product ->
            Card(onClick = { onProductItemClick(product) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                        .padding(16.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context).data(product.imageUrl).crossfade(true)
                            .build(), modifier = Modifier, contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = product.name, style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun SuggestionList(
    suggestions: List<Product>,
    onSuggestionItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(suggestions) {
            Card(modifier = Modifier, onClick = { onSuggestionItemClick(it.name) }) {
                Text(
                    text = it.name, modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}