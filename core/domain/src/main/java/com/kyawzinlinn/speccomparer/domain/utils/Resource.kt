package com.kyawzinlinn.speccomparer.domain.utils

sealed class Resource <out T> {
    data object Default: Resource<Nothing>()
    data object Loading: Resource<Nothing>()
    data class Success<T> (val data: T): Resource<T>()
    data class Error<T>(val networkError: NetworkError) : Resource<T>()
}