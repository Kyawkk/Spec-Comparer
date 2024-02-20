@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class
)

package com.kyawzinlinn.speccomparer.search

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kyawzinlinn.speccomparer.components.handleResponse
import com.kyawzinlinn.speccomparer.design_system.components.AutoCompleteSearchField
import com.kyawzinlinn.speccomparer.design_system.components.LoadingScreen
import com.kyawzinlinn.speccomparer.domain.model.Product
import com.kyawzinlinn.speccomparer.domain.utils.ProductType

@Composable
fun SearchScreen(
    productType: ProductType,
    onProductItemClick: (Product) -> Unit,
    searchViewModel: SearchViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val TAG = "SearchScreen"
    var searchResults by rememberSaveable { mutableStateOf(listOf<Product>()) }
    val searchResponse by searchViewModel.searchResultsResponse.collectAsStateWithLifecycle()
    val suggestions by searchViewModel.suggestions.collectAsStateWithLifecycle()
    var showLoading by remember { mutableStateOf(false) }
    val selectedQuery by searchViewModel.selectedQuery.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        searchViewModel.clearSuggestions()
    }

    handleResponse(
        resource = searchResponse,
        onRetry = { searchViewModel.search(searchQuery, productType) }
    ) {
        searchResults = it
    }

    /* LaunchedEffect(searchResponse) {
         withContext(Dispatchers.Main) {

         }
         when (searchResponse) {
             is Resource.Loading -> {
                 showLoading = true
             }

             is Resource.Success -> {
                 showLoading = false
                 searchResults = (searchResponse as Resource.Success<List<Product>>).data
             }

             else -> {
                 showLoading = false
             }
         }
     }*/

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        AutoCompleteSearchField(
            suggestions = suggestions,
            defaultValue = selectedQuery,
            onSearch = {
                searchQuery = it
                searchViewModel.search(it, productType)
            },
            onValueChange = { searchViewModel.getSuggestions(it, productType) })

        if (showLoading) LoadingScreen()
        else {
            SearchResultList(
                searchResults = searchResults,
                onRemoveErrorItem = {
                    val temp = searchResults.toMutableList()
                    temp.remove(it)
                    searchResults = temp
                }, onProductItemClick = onProductItemClick
            )
        }
    }
}

@Composable
fun SearchResultList(
    searchResults: List<Product>,
    onRemoveErrorItem: (Product) -> Unit,
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
            Card(
                onClick = { onProductItemClick(product) },
                modifier = Modifier.animateItemPlacement()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                        onError = { onRemoveErrorItem(product) },
                        model = ImageRequest.Builder(context).data(product.imageUrl).crossfade(true)
                            .build(),
                        modifier = Modifier.weight(0.2f),
                        contentDescription = null
                    )
                    Text(
                        text = product.name,
                        modifier = Modifier.weight(0.8f),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}