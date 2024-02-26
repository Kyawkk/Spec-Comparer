package com.kyawzinlinn.speccomparer.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyawzinlinn.speccomparer.domain.model.Product
import com.kyawzinlinn.speccomparer.domain.model.detail.ProductSpecificationResponse
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
class DetailViewModel @Inject constructor(
    private val repository: ProductRepository
): ViewModel() {

    private val _detailResponse = MutableStateFlow<Resource<ProductSpecificationResponse>> (Resource.Default)
    val detailResponse: StateFlow<Resource<ProductSpecificationResponse>> = _detailResponse.asStateFlow()

    private val _suggestions = MutableStateFlow(emptyList<Product>())
    val suggestions = _suggestions.asStateFlow()

    fun getProductDetailSpecification(device: String, type: ProductType) {
        viewModelScope.launch {
            _detailResponse.value = Resource.Loading
            delay(500)
            _detailResponse.value = repository.getProductSpecifications(device,type)
        }
    }

    fun getSuggestions(query: String,productType: ProductType) {
        viewModelScope.launch {
            when (val response = repository.search(query,8,productType)) {
                is Resource.Success -> {
                    _suggestions.value = response.data
                }
                else -> {}
            }
        }
    }

    fun resetSuggestions() {
        _suggestions.value = emptyList()
    }
}