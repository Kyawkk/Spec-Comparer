package com.kyawzinlinn.speccomparer.domain.utils

sealed class Resource <out T> {
    object Default: Resource<Nothing>()
    object Loading: Resource<Nothing>()
    data class Success<T> (val data: T): Resource<T>()
    data class Error<T>(val message: String) : Resource<T>()
}