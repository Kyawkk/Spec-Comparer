package com.kyawzinlinn.speccomparer.domain.utils

inline fun <T, reified R> T?.safe(transform: (T) -> R): R {
    return this?.let(transform) ?: "N/A" as R
}
