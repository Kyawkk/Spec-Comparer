package com.kyawzinlinn.speccomparer.network.di

import com.kyawzinlinn.speccomparer.domain.utils.BASE_URL
import com.kyawzinlinn.speccomparer.network.api.ApiService
import com.kyawzinlinn.speccomparer.network.repository.ProductRepository
import com.kyawzinlinn.speccomparer.network.repository.ProductRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun providesApi() : ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesSearchRepository(apiService: ApiService) : ProductRepository {
        return ProductRepositoryImpl(apiService)
    }
}