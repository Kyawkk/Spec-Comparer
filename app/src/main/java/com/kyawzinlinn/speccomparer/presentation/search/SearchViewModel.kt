package com.kyawzinlinn.speccomparer.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyawzinlinn.speccomparer.data.repository.SearchRepository
import com.kyawzinlinn.speccomparer.domain.model.Product
import com.kyawzinlinn.speccomparer.utils.ProductType
import com.kyawzinlinn.speccomparer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {
    private val _searchResults = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val searchResults = _searchResults.asStateFlow()

    fun search(query: String, limit: Int, productType: ProductType) {
        viewModelScope.launch {
            try {
                val result = searchRepository.search(query, limit, productType)
                _searchResults.value = Resource.Success(result)
            } catch (e: Exception) {
                _searchResults.value = Resource.Error(e.message.toString())
            }
        }
    }
}