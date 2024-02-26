package com.kyawzinlinn.speccomparer.data

import com.kyawzinlinn.speccomparer.domain.model.compare.CompareDetailResponse
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareKeyDifferences
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareScore
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareScoreBar
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareScoreRow
import com.kyawzinlinn.speccomparer.domain.model.compare.KeyDifference
import com.kyawzinlinn.speccomparer.domain.model.detail.CompareResponse
import com.kyawzinlinn.speccomparer.domain.model.detail.HeaderData

object DataSource {
    val defaultCompareResponse = CompareResponse(
        headerData = HeaderData("","","",""),
        keyDifferences = emptyList(),
        compares = emptyList()
    )

    val compareResponse = com.kyawzinlinn.speccomparer.domain.model.compare.CompareResponse(
        compareDeviceHeaderDetails = CompareScore(
            firstScore = "83 out of 100",
            secondScore = "85 out of 100",
            firstImgUrl = "/common/images/phone/samsung-galaxy-s23-mini.jpeg",
            secondImgUrl = "/common/images/phone/huawei-p60-mini.jpeg",
            firstDeviceName = "Samsung Galaxy S23",
            secondDeviceName = "Huawei P60"
        ),
        keyDifferences = CompareKeyDifferences(
            title = "Key differences",
            firstKeyDifference = KeyDifference(
                title = "Reasons to consider the Samsung Galaxy S23",
                pros = listOf(
                    "Newer Bluetooth version (v5.3)",
                    "25% better performance in AnTuTu Benchmark (1468K versus 1170K)",
                    "More recent OS version: Android 14 versus 13",
                    "Faster storage type - UFS 4.0 versus UFS 3.1",
                    "Supports Dolby Atmos sound technology",
                    "Weighs 29 grams less",
                    "Ready for eSIM technology"
                )
            ),
            secondDifference = KeyDifference(
                title = "Reasons to consider the Huawei P60",
                pros = listOf(
                    "Has a 0.57 inch larger screen size",
                    "Comes with 915 mAh larger battery capacity: 4815 vs 3900 mAh",
                    "The rear camera has a 5x optical zoom",
                    "Supports higher wattage charging (66W versus 25W)",
                    "Has a built-in infrared port"
                )
            )
        ),
        compareSpecDetails = listOf(
            CompareDetailResponse(
                title = "Review",
                scoreBars = listOf(
                    CompareScoreBar(name = "Display", firstSpecName = "88", firstSpecValue = "88%", secondSpecName = "88", secondSpecValue = "88%"),
                    CompareScoreBar(name = "Camera", firstSpecName = "83", firstSpecValue = "83%", secondSpecName = "83", secondSpecValue = "83%"),
                    CompareScoreBar(name = "Performance", firstSpecName = "85", firstSpecValue = "85%", secondSpecName = "85", secondSpecValue = "85%"),
                    CompareScoreBar(name = "Gaming", firstSpecName = "97", firstSpecValue = "97%", secondSpecName = "97", secondSpecValue = "97%"),
                    CompareScoreBar(name = "Battery", firstSpecName = "73", firstSpecValue = "73%", secondSpecName = "73", secondSpecValue = "73%"),
                    CompareScoreBar(name = "Connectivity", firstSpecName = "90", firstSpecValue = "90%", secondSpecName = "90", secondSpecValue = "90%"),
                    CompareScoreBar(name = "NanoReview Score", firstSpecName = "83", firstSpecValue = "83%", secondSpecName = "83", secondSpecValue = "83%")
                ),
                scoreRows = emptyList()
            ),
            CompareDetailResponse(
                title = "Display",
                scoreBars = listOf(
                    CompareScoreBar(name = "Peak brightness test (auto)", firstSpecName = "1193 nits", firstSpecValue = "99%", secondSpecName = "1193 nits", secondSpecValue = "99%")
                ),
                scoreRows = listOf(
                    CompareScoreRow(name = "Type", first = "Dynamic AMOLED", second = "OLED"),
                    CompareScoreRow(name = "Size", first = "6.1 inches", second = "6.67 inches"),
                    CompareScoreRow(name = "Resolution", first = "1080 x 2340 pixels", second = "1220 x 2700 pixels"),
                    CompareScoreRow(name = "Aspect ratio", first = "19.5:9", second = "20:9"),
                    CompareScoreRow(name = "PPI", first = "425 ppi", second = "444 ppi"),
                    CompareScoreRow(name = "Refresh rate", first = "120 Hz", second = "120 Hz"),
                    CompareScoreRow(name = "Adaptive refresh rate", first = "Yes", second = "Yes"),
                    CompareScoreRow(name = "Max rated brightness", first = "800 nits", second = "550 nits"),
                    CompareScoreRow(name = "Max rated brightness in HDR", first = "1750 nits", second = "-"),
                    CompareScoreRow(name = "HDR support", first = "Yes, HDR10+", second = "Yes, HDR10+"),
                    CompareScoreRow(name = "Screen protection", first = "Gorilla Glass Victus 2", second = "Tempered glass"),
                    CompareScoreRow(name = "Screen-to-body ratio", first = "88.1%", second = "89.8%"),
                    CompareScoreRow(name = "Display features", first = "- DCI-P3 - Always-On Display", second = "- DCI-P3 - Always-On Display - DC Dimming"),
                    CompareScoreRow(name = "RGB color space", first = "98.9%", second = "-"),
                    CompareScoreRow(name = "PWM", first = "240 Hz", second = "-"),
                    CompareScoreRow(name = "Response time", first = "1 ms", second = "-"),
                    CompareScoreRow(name = "Contrast", first = "∞ Infinity", second = "-")
                )
            ),
            CompareDetailResponse(
                title = "Design and build",
                scoreBars = listOf(
                    CompareScoreBar(name = "Screen-to-body ratio", firstSpecName = "88.1%", firstSpecValue = "88%", secondSpecName = "88.1%", secondSpecValue = "88%")
                ),
                scoreRows = listOf(
                    CompareScoreRow(name = "Height", first = "146.3 mm (5.76 inches)", second = "161 mm (6.34 inches)"),
                    CompareScoreRow(name = "Width", first = "70.9 mm (2.79 inches)", second = "74.5 mm (2.93 inches)"),
                    CompareScoreRow(name = "Thickness", first = "7.6 mm (0.3 inches)", second = "8.3 mm (0.33 inches)"),
                    CompareScoreRow(name = "Weight", first = "168 g (5.93 oz)", second = "197 g (6.95 oz)"),
                    CompareScoreRow(name = "Waterproof", first = "IP68", second = "IP68"),
                    CompareScoreRow(name = "Advanced cooling", first = "Vapor chamber", second = "-"),
                    CompareScoreRow(name = "Rear material", first = "Glass", second = "Glass")
                )
            )
        )
    )
}