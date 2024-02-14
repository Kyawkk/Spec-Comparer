@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.kyawzinlinn.speccomparer.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kyawzinlinn.speccomparer.design_system.UiState
import com.kyawzinlinn.speccomparer.design_system.components.AutoCompleteSearchField
import com.kyawzinlinn.speccomparer.design_system.components.LoadingScreen
import com.kyawzinlinn.speccomparer.domain.model.Product
import com.kyawzinlinn.speccomparer.domain.utils.Resource

@Composable
fun SearchScreen(
    uiState: UiState,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onProductItemClick: (Product) -> Unit,
    onSuggestionItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    suggestions: List<Product>,
    isSearching: Boolean
) {
    var showSuggestions by rememberSaveable { mutableStateOf(false) }
    var value by rememberSaveable { mutableStateOf("") }
    var searchResults by rememberSaveable { mutableStateOf(listOf<Product>()) }
    var showSuggestionDropdown by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(uiState.searchResults) {
        when (uiState.searchResults) {
            is Resource.Success -> {
                searchResults = (uiState.searchResults as Resource.Success<List<Product>>).data
            }
            else -> {}
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        AutoCompleteSearchField(suggestions = suggestions, onSearch = onSearch, onValueChange = onValueChange)

        /*SearchBar(
            input = value,
            modifier = Modifier
                .padding(16.dp)
                .indication(interactionSource, LocalIndication.current),
            onValueChange = { keyword ->
                onValueChange(keyword)
                value = keyword
                showSuggestions = suggestions.isNotEmpty() && keyword.isNotEmpty()
            },
            onSearch = onSearch
        )*/

        /*if (isSearching) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        } else {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                DropdownMenu(
                    properties = PopupProperties(dismissOnClickOutside = true),
                    expanded = showSuggestionDropdown,
                    onDismissRequest = { showSuggestionDropdown = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    suggestions.forEach {
                        DropdownMenuItem(
                            text = { Text(text = it.name) },
                            onClick = {
                                onSuggestionItemClick(it.name)
                                showSuggestionDropdown = false
                                value = it.name
                            }
                        )
                    }
                }
            }
        }*/

        when (uiState.searchResults) {
            is Resource.Loading -> LoadingScreen()
            is Resource.Success -> SearchResultList(
                searchResults,
                onProductItemClick = onProductItemClick
            )

            is Resource.Error -> {}
            is Resource.Default -> {}
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
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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