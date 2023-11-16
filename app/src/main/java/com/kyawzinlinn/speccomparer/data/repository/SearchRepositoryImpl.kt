package com.kyawzinlinn.speccomparer.data.repository

import com.kyawzinlinn.speccomparer.data.remote.ApiService
import com.kyawzinlinn.speccomparer.domain.model.Product
import com.kyawzinlinn.speccomparer.utils.ProductType

class SearchRepositoryImpl(private val apiService: ApiService) : SearchRepository {
    override suspend fun search(
        query: String,
        limit: Int,
        productType: ProductType
    ): List<Product> = apiService.search(query, limit, productType.title)
}