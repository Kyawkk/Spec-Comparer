@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class
)

package com.kyawzinlinn.speccomparer.search

import android.util.Log
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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kyawzinlinn.speccomparer.design_system.components.AutoCompleteSearchField
import com.kyawzinlinn.speccomparer.design_system.components.HandleResponse
import com.kyawzinlinn.speccomparer.design_system.components.NetworkImage
import com.kyawzinlinn.speccomparer.design_system.extensions.products
import com.kyawzinlinn.speccomparer.design_system.states.SearchResultState
import com.kyawzinlinn.speccomparer.domain.model.Product
import com.kyawzinlinn.speccomparer.domain.utils.ProductType

@Composable
fun SearchScreen(
    productType: ProductType,
    onProductItemClick: (Product) -> Unit,
    searchViewModel: SearchViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {

    val searchResponse by searchViewModel.searchResultsResponse.collectAsStateWithLifecycle()
    val suggestions by searchViewModel.suggestions.collectAsStateWithLifecycle()
    val selectedQuery by searchViewModel.selectedQuery.collectAsStateWithLifecycle()
    val searchResults by searchViewModel.searchResults.collectAsStateWithLifecycle()
    val searchResultState by searchViewModel.searchResultState.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }
    var hasSearched by remember { mutableStateOf(false) }
    var isSearching by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        searchViewModel.clearSuggestions()
    }

    HandleResponse(
        resource = searchResponse,
        onRetry = {
            searchViewModel.apply {
                search(searchQuery, productType)
                updateSearchResultState(SearchResultState.Nothing)
            }
        },
        onError = {},
        onLoading = {},
        onSuccess = {}
    )

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        AutoCompleteSearchField(
            suggestions = suggestions,
            defaultValue = selectedQuery,
            onSearch = {
                searchQuery = it
                hasSearched = true
                searchViewModel.search(it, productType)
            },
            onValueChange = {
                hasSearched = it.isNotEmpty()
                isSearching = true
                searchViewModel.getSuggestions(it, productType)
            }
        )

        SearchResultList(
            searchResults = searchResults,
            onRemoveErrorItem = searchViewModel::removeErrorItem,
            searchQuery = searchQuery,
            searchResultState = searchResultState,
            onProductItemClick = onProductItemClick,
        )
    }
}

@Composable
fun SearchResultList(
    searchQuery: String,
    searchResultState: SearchResultState,
    searchResults: List<Product>,
    onRemoveErrorItem: (Product) -> Unit,
    onProductItemClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        products(
            items = searchResults,
            searchResultState = searchResultState,
            query = searchQuery,
        ) { product ->
            var updatedProduct by remember { mutableStateOf(product) }

            Card(
                onClick = { onProductItemClick(updatedProduct) },
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
                    NetworkImage(
                        imageUrl = updatedProduct.imageUrl,
                        product = updatedProduct,
                        onRetrySuccess = {
                            updatedProduct = it
                        },
                        onErrorItemRemove = {
                            Log.d("TAG", "SearchResultList: onErrorItemRemove")
                            onRemoveErrorItem(product)
                        }
                    )
                    Text(
                        text = updatedProduct.name,
                        modifier = Modifier.weight(0.8f),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}