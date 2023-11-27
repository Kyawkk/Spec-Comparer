package com.kyawzinlinn.speccomparer.data.repository

import com.kyawzinlinn.speccomparer.data.remote.ApiService
import com.kyawzinlinn.speccomparer.data.remote.api.smartphone.CompareApi
import com.kyawzinlinn.speccomparer.domain.model.Product
import com.kyawzinlinn.speccomparer.domain.model.addImageLinks
import com.kyawzinlinn.speccomparer.domain.model.smartphone.CompareResponse
import com.kyawzinlinn.speccomparer.domain.model.smartphone.ProductSpecificationResponse
import com.kyawzinlinn.speccomparer.utils.ProductType
import com.kyawzinlinn.speccomparer.utils.Resource

class SearchRepositoryImpl(private val apiService: ApiService) : SearchRepository {
    override suspend fun search(
        query: String,
        limit: Int,
        productType: ProductType
    ): List<Product> = apiService.search(query, limit, productType.title).addImageLinks()

    override suspend fun getProductSpecifications(
        device: String,
        type: ProductType
    ): Resource<ProductSpecificationResponse> = CompareApi.getProductSpecification(device ,type)
}