package com.kyawzinlinn.speccomparer.network.repository

import com.kyawzinlinn.speccomparer.domain.model.Product
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareResponse
import com.kyawzinlinn.speccomparer.domain.model.detail.ProductSpecificationResponse
import com.kyawzinlinn.speccomparer.domain.utils.ProductType
import com.kyawzinlinn.speccomparer.domain.utils.Resource

interface ProductRepository {
    suspend fun search(query: String, limit: Int, productType: ProductType) : Resource<List<Product>>
    suspend fun getProductSpecifications(device: String, type: ProductType) : Resource<ProductSpecificationResponse>
    suspend fun compareProducts (firstDevice: String, secondDevice: String, type: ProductType): Resource<CompareResponse>

}