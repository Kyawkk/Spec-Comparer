package com.kyawzinlinn.speccomparer.data.repository

import com.kyawzinlinn.speccomparer.domain.model.Product
import com.kyawzinlinn.speccomparer.domain.model.addImageLinks
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareResponse
import com.kyawzinlinn.speccomparer.domain.model.smartphone.ProductSpecificationResponse
import com.kyawzinlinn.speccomparer.domain.utils.ProductType
import com.kyawzinlinn.speccomparer.domain.utils.Resource
import com.kyawzinlinn.speccomparer.network.api.ApiService
import com.kyawzinlinn.speccomparer.network.api.CompareApi
import com.kyawzinlinn.speccomparer.network.api.DetailApi

class SearchRepositoryImpl(private val apiService: ApiService) : SearchRepository {
    override suspend fun search(
        query: String,
        limit: Int,
        productType: ProductType
    ): Resource<List<Product>> {
        try {
            return Resource.Success(apiService.search(query, limit, productType.title).addImageLinks())
        }catch (e: Exception) {
            return Resource.Error(e.message.toString())
        }
    }

    override suspend fun getProductSpecifications(
        device: String,
        type: ProductType
    ): Resource<ProductSpecificationResponse> = DetailApi.getProductSpecification(device ,type)

    override suspend fun compareProducts(
        firstDevice: String,
        secondDevice: String,
        type: ProductType
    ): Resource<CompareResponse> = CompareApi.compareDevices(firstDevice,secondDevice,type)
}