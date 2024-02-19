package com.kyawzinlinn.speccomparer.network.repository

import android.util.Log
import com.kyawzinlinn.speccomparer.domain.model.Product
import com.kyawzinlinn.speccomparer.domain.model.addImageLinks
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareResponse
import com.kyawzinlinn.speccomparer.domain.model.smartphone.ProductSpecificationResponse
import com.kyawzinlinn.speccomparer.domain.utils.ProductType
import com.kyawzinlinn.speccomparer.domain.utils.Resource
import com.kyawzinlinn.speccomparer.network.api.ApiService
import com.kyawzinlinn.speccomparer.network.api.CompareApi
import com.kyawzinlinn.speccomparer.network.api.DetailApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(private val apiService: ApiService) : ProductRepository {
    override suspend fun search(
        query: String, limit: Int, productType: ProductType
    ): Resource<List<Product>> {
        return try {
            val response = apiService.search(query, limit, productType.title).addImageLinks()
            Log.d("TAG", "search: $response")
            Resource.Success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun getProductSpecifications(
        device: String, type: ProductType
    ): Resource<ProductSpecificationResponse> = try {
        withContext(Dispatchers.IO) {
            Resource.Success(DetailApi.getProductSpecification(device, type))
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Resource.Error(e.message.toString())
    }

    override suspend fun compareProducts(
        firstDevice: String, secondDevice: String, type: ProductType
    ): Resource<CompareResponse> = CompareApi.compareDevices(firstDevice, secondDevice, type)
}