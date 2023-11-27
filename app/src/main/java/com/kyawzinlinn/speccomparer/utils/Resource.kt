package com.kyawzinlinn.speccomparer.utils

sealed class Resource <T> (val data: T?) {
    class Loading<T>: Resource<T>(data = null)
    data class Success<T> (val da: T): Resource<T>(data = da)
    data class Error<T>(val message: String) : Resource<T>(data = null)
}