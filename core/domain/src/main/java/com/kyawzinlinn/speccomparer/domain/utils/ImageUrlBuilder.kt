package com.kyawzinlinn.speccomparer.domain.utils

import android.util.Log

object ImageUrlBuilder {
    fun build(path: String): String {
        return IMG_PREFIX + path
    }

    fun buildFailedImageUrl(path: String): String {
        return if (path.lowercase().contains("samsung")) {
            "${path.replace("mini.jpeg", "exynos-mini.jpeg")}"
        } else "$IMAGE_URL/phone/$path"
    }

    fun buildSingleImage(productType: ProductType, path: String): String {
        return when (productType) {
            ProductType.Soc -> checkPath(path, productType)
            ProductType.Cpu -> checkPath(path, productType)
            else -> "$IMAGE_URL${productType.title}/$path-mini.jpeg"
        }
    }

    private fun checkPath(path: String, productType: ProductType): String {
        return when (true) {
            path.lowercase().contains("qualcomm") -> "${IMAGE_URL}soc/qualcomm-mini.jpeg"
            path.lowercase().contains("mediatek") -> "${IMAGE_URL}soc/mediatek-mini.jpeg"
            path.lowercase().contains("exynos") -> "${IMAGE_URL}soc/samsung-mini.jpeg"
            (path.lowercase()
                .contains("apple") && productType == ProductType.Soc) -> "${IMAGE_URL}soc/apple-mini.jpeg"

            path.lowercase().contains("google") -> "${IMAGE_URL}soc/google-mini.jpeg"
            path.lowercase().contains("hisilicon") -> "${IMAGE_URL}soc/hisilicon-mini.jpeg"
            path.lowercase().contains("unisoc") -> "${IMAGE_URL}soc/unisoc-mini.jpeg"
            path.lowercase().contains("amd-ryzen-3") -> "${IMAGE_URL}cpu/amd-ryzen-3.jpeg"
            path.lowercase().contains("amd-ryzen-5") -> "${IMAGE_URL}cpu/amd-ryzen-5.jpeg"
            path.lowercase().contains("amd-ryzen-7") -> "${IMAGE_URL}cpu/amd-ryzen-7.jpeg"
            path.lowercase().contains("amd-ryzen-9") -> "${IMAGE_URL}cpu/amd-ryzen-9.jpeg"
            path.lowercase()
                .contains("amd-ryzen-threadripper") -> "${IMAGE_URL}cpu/amd-ryzen-threadripper.jpeg"

            path.lowercase().contains("i3") -> "${IMAGE_URL}cpu/intel-core-i3.jpeg"
            path.lowercase().contains("i5") -> "${IMAGE_URL}cpu/intel-core-i5.jpeg"
            path.lowercase().contains("i7") -> "${IMAGE_URL}cpu/intel-core-i7.jpeg"
            path.lowercase().contains("i9") -> "${IMAGE_URL}cpu/intel-core-i9.jpeg"
            path.lowercase()
                .contains("intel-core-ultra-3") -> "${IMAGE_URL}cpu/intel-core-ultra-3.jpeg"

            path.lowercase()
                .contains("intel-core-ultra-5") -> "${IMAGE_URL}cpu/intel-core-ultra-5.jpeg"

            path.lowercase()
                .contains("intel-core-ultra-7") -> "${IMAGE_URL}cpu/intel-core-ultra-7.jpeg"

            path.lowercase()
                .contains("intel-core-ultra-9") -> "${IMAGE_URL}cpu/intel-core-ultra-9.jpeg"

            path.lowercase().contains("intel") -> "${IMAGE_URL}cpu/intel-processor.jpeg"
            path.lowercase().contains("apple-m1") -> "${IMAGE_URL}cpu/apple-m.jpeg"
            path.lowercase().contains("apple-m2") -> "${IMAGE_URL}cpu/apple-m2.jpeg"
            path.lowercase().contains("apple-m3") -> "${IMAGE_URL}cpu/apple-m3.jpeg"
            else -> ""
        }
    }
}