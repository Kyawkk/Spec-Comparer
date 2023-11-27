package com.kyawzinlinn.speccomparer.data.repository

import com.kyawzinlinn.speccomparer.domain.model.Product
import com.kyawzinlinn.speccomparer.domain.model.smartphone.CompareResponse
import com.kyawzinlinn.speccomparer.domain.model.smartphone.ProductSpecificationResponse
import com.kyawzinlinn.speccomparer.utils.ProductType
import com.kyawzinlinn.speccomparer.utils.Resource

interface SearchRepository {
    suspend fun search(query: String, limit: Int, productType: ProductType) : List<Product>

    suspend fun getProductSpecifications(device: String, type: ProductType) : Resource<ProductSpecificationResponse>
}