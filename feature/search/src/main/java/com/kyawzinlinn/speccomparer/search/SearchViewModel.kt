package com.kyawzinlinn.speccomparer.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyawzinlinn.speccomparer.design_system.states.SearchResultState
import com.kyawzinlinn.speccomparer.domain.model.Product
import com.kyawzinlinn.speccomparer.domain.utils.ProductType
import com.kyawzinlinn.speccomparer.domain.utils.Resource
import com.kyawzinlinn.speccomparer.network.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel() {
    
    companion object {
        const val TAG = "SearchViewModel"
    }
    
    private val _searchResultsResponse = MutableStateFlow<Resource<List<Product>>>(Resource.Default)
    val searchResultsResponse = _searchResultsResponse.asStateFlow()

    private val _suggestions = MutableStateFlow(emptyList<Product>())
    val suggestions = _suggestions.asStateFlow()

    private val _selectedQuery = MutableStateFlow("")
    val selectedQuery = _selectedQuery.asStateFlow()

    private val _searchResults = MutableStateFlow(emptyList<Product>())
    val searchResults: StateFlow<List<Product>> = _searchResults.asStateFlow()

    private val _searchResultState = MutableStateFlow<SearchResultState>(SearchResultState.Default)
    val searchResultState: StateFlow<SearchResultState> = _searchResultState.asStateFlow()

    init {
        _suggestions.value = emptyList()
        _searchResultsResponse.value = Resource.Default
    }

    fun updateSearchResultState(state: SearchResultState) {
        _searchResultState.value = state
    }

    private fun updateSelectedQuery(selectedQuery: String) {
        _selectedQuery.value = selectedQuery
    }

    fun removeErrorItem(product: Product) {
        Log.d(TAG, "removeErrorItem: ${_searchResults.value.map { it.name }}")
        _searchResults.update { it - product }
        Log.d(TAG, "removeErrorItem: ${_searchResults.value.map { it.name }}")
    }

    fun search (productName: String, productType: ProductType) {
        viewModelScope.launch {
            _searchResults.update { emptyList() }
            _suggestions.value = emptyList()
            updateSelectedQuery(productName)
            _searchResultsResponse.value = Resource.Loading
            delay(500)
            _searchResultsResponse.value = productRepository.search(productName,500,productType)
            when (searchResultsResponse.value) {
                is Resource.Loading -> updateSearchResultState(SearchResultState.Loading)
                is Resource.Success -> {
                    val data = (searchResultsResponse.value as Resource.Success<List<Product>>).data
                    _searchResults.value = data
                    Log.d(TAG, "search: $data")
                    if (data.isEmpty()) updateSearchResultState(SearchResultState.Empty) else updateSearchResultState(SearchResultState.Success)
                }
                is Resource.Error -> {
                    Log.d(TAG, "search: error")
                    updateSearchResultState(SearchResultState.Error)
                }
                else -> {updateSearchResultState(SearchResultState.Default)}
            }
        }
    }

    fun getSuggestions(query: String,productType: ProductType) {
        viewModelScope.launch {
            when (val response = productRepository.search(query,8,productType)) {
                is Resource.Success -> {
                    _suggestions.value = response.data
                }
                else -> {}
            }
        }
    }

    fun clearSuggestions() {
        _suggestions.value = emptyList()
    }
}