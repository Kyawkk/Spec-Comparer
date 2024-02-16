package com.kyawzinlinn.speccomparer.domain.utils

object ImageUrlBuilder {
    fun build(path: String) : String {
        return IMG_PREFIX + path
    }

    fun buildSingleImage(productType: ProductType, path: String) : String{
        println("productType: $productType")
        return when (productType) {
            ProductType.Soc -> checkPath(path)
            else -> "$IMAGE_URL${productType.title}/$path-mini.jpeg"
        }
    }

    private fun checkPath(path: String): String {
        return when (true) {
            path.lowercase().contains("qualcomm") -> "${IMAGE_URL}soc/qualcomm-mini.jpeg"
            path.lowercase().contains("mediatek") -> "${IMAGE_URL}soc/mediatek-mini.jpeg"
            path.lowercase().contains("exynos") -> "${IMAGE_URL}soc/samsung-mini.jpeg"
            path.lowercase().contains("apple") -> "${IMAGE_URL}soc/apple-mini.jpeg"
            path.lowercase().contains("google") -> "${IMAGE_URL}soc/google-mini.jpeg"
            path.lowercase().contains("hisilicon") -> "${IMAGE_URL}soc/hisilicon-mini.jpeg"
            path.lowercase().contains("unisoc") -> "${IMAGE_URL}soc/unisoc-mini.jpeg"
            else -> ""
        }
    }
}