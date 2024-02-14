package com.kyawzinlinn.speccomparer.network.di

import com.kyawzinlinn.speccomparer.domain.utils.BASE_URL
import com.kyawzinlinn.speccomparer.network.api.ApiService
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
    fun providesSearchRepository(apiService: ApiService) : com.kyawzinlinn.speccomparer.data.repository.SearchRepository {
        return com.kyawzinlinn.speccomparer.data.repository.SearchRepositoryImpl(apiService)
    }
}