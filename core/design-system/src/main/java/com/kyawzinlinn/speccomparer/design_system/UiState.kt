package com.kyawzinlinn.speccomparer.design_system

import com.kyawzinlinn.speccomparer.domain.model.Product
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareResponse
import com.kyawzinlinn.speccomparer.domain.model.smartphone.ProductSpecificationResponse
import com.kyawzinlinn.speccomparer.domain.utils.Resource

data class UiState(
    val title: String = "",
    val canNavigateBack: Boolean = false,
    val showTrailingIcon: Boolean = false,
    val showBottomSheet: Boolean = false,
    val isCompareState: Boolean = false,
    val productDetails: Resource<ProductSpecificationResponse> = Resource.Loading,
    val secondProductDetails: Resource<ProductSpecificationResponse> = Resource.Loading,
    val suggestions: List<Product> = emptyList(),
    val compareDetails: Resource<CompareResponse> = Resource.Loading,
    val searchResults: Resource<List<Product>> = Resource.Default,
)
