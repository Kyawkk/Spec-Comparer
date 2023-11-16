package com.kyawzinlinn.speccomparer.data.remote

import com.kyawzinlinn.speccomparer.domain.model.Product
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    suspend fun search(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("type") type: String
    ) : List<Product>
}