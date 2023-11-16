package com.kyawzinlinn.speccomparer.data.repository

import com.kyawzinlinn.speccomparer.domain.model.Product
import com.kyawzinlinn.speccomparer.utils.ProductType

interface SearchRepository {
    suspend fun search(query: String, limit: Int, productType: ProductType) : List<Product>
}