package com.kyawzinlinn.speccomparer.di

import com.kyawzinlinn.speccomparer.data.remote.ApiService
import com.kyawzinlinn.speccomparer.data.repository.SearchRepository
import com.kyawzinlinn.speccomparer.data.repository.SearchRepositoryImpl
import com.kyawzinlinn.speccomparer.utils.BASE_URL
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
    fun providesSearchRepository(apiService: ApiService) : SearchRepository {
        return SearchRepositoryImpl(apiService)
    }
}