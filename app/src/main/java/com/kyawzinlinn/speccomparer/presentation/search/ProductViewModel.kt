package com.kyawzinlinn.speccomparer.presentation.search

import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyawzinlinn.speccomparer.R
import com.kyawzinlinn.speccomparer.data.repository.SearchRepository
import com.kyawzinlinn.speccomparer.domain.model.Product
import com.kyawzinlinn.speccomparer.domain.model.smartphone.CompareResponse
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
    private val _searchResults = MutableStateFlow<Resource<List<Product>>>(Loading())
    val searchResults: StateFlow<Resource<List<Product>>> = _searchResults.asStateFlow()

    private val _compareResponse = MutableStateFlow<Resource<CompareResponse>>(Loading())
    val compareResponse : StateFlow<Resource<CompareResponse>> = _compareResponse.asStateFlow()

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

    fun search(query: String, limit: Int, productType: ProductType) {
        viewModelScope.launch {
            _searchResults.value = Loading()
            try {
                val result = searchRepository.search(query, limit, productType)
                _searchResults.value = Resource.Success(result)
            } catch (e: Exception) {
                _searchResults.value = Resource.Error(e.message.toString())
            }
        }
    }

    fun getProductSpecification(device: String, type: ProductType) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    productDetails = searchRepository.getProductSpecifications(device, type)
                )
            }
        }
    }
}

data class UiState(
    val title: String = "",
    val canNavigateBack: Boolean = false,
    val productDetails : Resource<ProductSpecificationResponse> = Loading(),
    val searchResults : Resource<List<Product>> = Loading()
)