package com.kyawzinlinn.speccomparer.domain.utils

inline fun <T, reified R> T?.safe(transform: (T) -> R): R {
    return this?.let(transform) ?: "N/A" as R
}

inline fun <reified T> getDefaultValueWhenNull(): T {
    return when (T::class) {
        Int::class -> 0 as T
        String::class -> "Something" as T
        Float::class -> 0.0f as T
        Double::class -> 0.0 as T
        else -> null as T
    }
}