package com.kyawzinlinn.speccomparer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyawzinlinn.speccomparer.data.repository.SearchRepository
import com.kyawzinlinn.speccomparer.domain.model.Product
import com.kyawzinlinn.speccomparer.domain.model.smartphone.ProductSpecificationResponse
import com.kyawzinlinn.speccomparer.utils.ProductType
import com.kyawzinlinn.speccomparer.utils.Resource
import com.kyawzinlinn.speccomparer.utils.Resource.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun updateNavigateBackStatus(canNavigateBack: Boolean){
        _uiState.update {
            it.copy(canNavigateBack = canNavigateBack)
        }
    }

    fun updateTitle(title: String) {
        _uiState.update {
            it.copy(title = title)
        }
    }

    fun resetProductDetails() {
        _uiState.update {
            it.copy(
                productDetails = Loading()
            )
        }
    }

    fun resetSearchResults() {
        _uiState.update {
            it.copy(
                searchResults = Resource.Default()
            )
        }
    }

    fun search(query: String, limit: Int, productType: ProductType) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(searchResults = searchRepository.search(query, limit, productType))
            }
        }
    }

    fun getSuggestions(query: String, limit: Int, productType: ProductType) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(suggestions = searchRepository.search(query, limit, productType))
            }
        }
    }

    fun getProductSpecification(device: String, type: ProductType) {
        viewModelScope.launch(Dispatchers.IO) {
            val productDetails = searchRepository.getProductSpecifications(device, type)
            _uiState.update {
                it.copy(
                    productDetails = productDetails
                )
            }
        }
    }
}

data class UiState(
    val title: String = "",
    val canNavigateBack: Boolean = false,
    val productDetails : Resource<ProductSpecificationResponse> = Loading(),
    val searchResults : Resource<List<Product>> = Resource.Default(),
    val suggestions : Resource<List<Product>> = Resource.Default(),
)