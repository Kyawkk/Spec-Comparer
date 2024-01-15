package com.kyawzinlinn.speccomparer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyawzinlinn.speccomparer.data.repository.SearchRepository
import com.kyawzinlinn.speccomparer.domain.model.Product
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareResponse
import com.kyawzinlinn.speccomparer.domain.model.smartphone.ProductSpecificationResponse
import com.kyawzinlinn.speccomparer.utils.ProductType
import com.kyawzinlinn.speccomparer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _suggestions = MutableStateFlow(listOf<Product>())
    val suggestions: StateFlow<List<Product>> = searchText
        .combine(_suggestions) { query, suggestions ->
            if (query.isBlank()) suggestions
            else suggestions.filter { it.doesMatchSearchQuery(query) }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _suggestions.value
        )


    fun updateNavigateBackStatus(canNavigateBack: Boolean) {
        _uiState.update {
            it.copy(canNavigateBack = canNavigateBack)
        }
    }

    fun updateTrailingIconStatus(showTrailingIcon: Boolean) {
        _uiState.update {
            it.copy(showTrailingIcon = showTrailingIcon)
        }
    }

    fun showBottomSheet(showBottomSheet: Boolean) {
        _uiState.update {
            it.copy(showBottomSheet = showBottomSheet)
        }
    }

    fun updateTitle(title: String) {
        _uiState.update {
            it.copy(title = title)
        }
    }

    fun resetSearchResults() {
        _uiState.update {
            it.copy(
                searchResults = Resource.Default
            )
        }
    }

    fun search(query: String, limit: Int, productType: ProductType) {
        viewModelScope.launch {
            _searchText.value = ""
            _uiState.update {
                it.copy(searchResults = searchRepository.search(query, limit, productType))
            }
        }
    }

    fun getSuggestions(query: String, limit: Int, productType: ProductType) {
        viewModelScope.launch {
            _isSearching.update { true }
            val searchResponse = searchRepository.search(query, limit, productType)
            when (searchResponse) {
                is Resource.Success -> {
                    _suggestions.value = searchResponse.data
                    delay(300)
                    _isSearching.update { false }
                }

                else -> {}
            }
        }
    }

    fun getFirstProductSpecifications(device: String, type: ProductType) {
        viewModelScope.launch(Dispatchers.IO) {
            val productDetails = searchRepository.getProductSpecifications(device, type)
            _uiState.update {
                it.copy(
                    isCompareState = false,
                    firstProductDetails = productDetails
                )
            }
        }
    }

    fun getSecondProductSpecifications(device: String, type: ProductType) {
        viewModelScope.launch(Dispatchers.IO) {
            val productDetails = searchRepository.getProductSpecifications(device, type)
            _uiState.update {
                it.copy(
                    isCompareState = true,
                    secondProductDetails = productDetails
                )
            }
        }
    }

    fun compareProducts(firstDevice: String, secondDevice: String, type: ProductType) {
        viewModelScope.launch(Dispatchers.IO) {
            val compareDetails = searchRepository.compareProducts(firstDevice, secondDevice, type)
            _uiState.update {
                it.copy(compareDetails = compareDetails)
            }
        }
    }
}

data class UiState(
    val title: String = "",
    val canNavigateBack: Boolean = false,
    val showTrailingIcon: Boolean = false,
    val showBottomSheet: Boolean = false,
    val isCompareState: Boolean = false,
    val firstProductDetails: Resource<ProductSpecificationResponse> = Resource.Loading,
    val secondProductDetails: Resource<ProductSpecificationResponse> = Resource.Loading,
    val compareDetails: Resource<CompareResponse> = Resource.Loading,
    val searchResults: Resource<List<Product>> = Resource.Default,
)