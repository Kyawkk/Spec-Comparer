package com.kyawzinlinn.speccomparer.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyawzinlinn.speccomparer.domain.model.Product
import com.kyawzinlinn.speccomparer.domain.utils.ProductType
import com.kyawzinlinn.speccomparer.domain.utils.Resource
import com.kyawzinlinn.speccomparer.network.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    init {
        /*_suggestions.value = emptyList()
        _searchResultsResponse.value = Resource.Default*/

        viewModelScope.launch {
            searchResultsResponse.collect {
                Log.d(TAG, "response: $it")
            }
        }
    }

    private fun updateSelectedQuery(selectedQuery: String) {
        _selectedQuery.value = selectedQuery
    }

    fun search (productName: String, productType: ProductType) {
        viewModelScope.launch {
            _suggestions.value = emptyList()
            updateSelectedQuery(productName)
            Log.d(TAG, "search: $productName $productType")
            _searchResultsResponse.value = Resource.Loading
            delay(500)
            _searchResultsResponse.value = productRepository.search(productName,500,productType)
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
}