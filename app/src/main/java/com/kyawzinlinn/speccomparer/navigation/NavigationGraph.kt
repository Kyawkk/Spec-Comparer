package com.kyawzinlinn.speccomparer.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kyawzinlinn.speccomparer.R
import com.kyawzinlinn.speccomparer.SharedUiViewmodel
import com.kyawzinlinn.speccomparer.compare.CompareScreen
import com.kyawzinlinn.speccomparer.details.ProductDetailScreen
import com.kyawzinlinn.speccomparer.domain.model.DisplayCard
import com.kyawzinlinn.speccomparer.domain.utils.ProductType
import com.kyawzinlinn.speccomparer.domain.utils.getProductType
import com.kyawzinlinn.speccomparer.domain.utils.safe
import com.kyawzinlinn.speccomparer.domain.utils.toPath
import com.kyawzinlinn.speccomparer.home.HomeScreen
import com.kyawzinlinn.speccomparer.search.SearchScreen

val displayCards = listOf(
    DisplayCard(
        R.drawable.smartphone,
        "Smartphones",
        "Make an in-depth comparison of various phones to see which is better in terms of camera quality, performance, battery life, and value for money.",
        ProductType.Smartphone
    ),
    DisplayCard(
        R.drawable.smartphone_chip,
        "Smartphone Processors",
        "Make an in-depth comparison of various phones to see which is better in terms of camera quality, performance, battery life, and value for money.",
        ProductType.Soc
    ),
    DisplayCard(
        R.drawable.processor,
        "Laptop CPUs",
        "Make an in-depth comparison of various phones to see which is better in terms of camera quality, performance, battery life, and value for money.",
        ProductType.Cpu
    ),
    DisplayCard(
        R.drawable.laptop,
        "Laptops",
        "Make an in-depth comparison of various phones to see which is better in terms of camera quality, performance, battery life, and value for money.",
        ProductType.Laptop
    ),
)

@Composable
fun NavigationGraph(
    sharedUiViewmodel: SharedUiViewmodel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val TAG = "NavigationGraph"
    val sharedUiState by sharedUiViewmodel.uiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController, modifier = modifier, startDestination = ScreenRoute.Home.name
    ) {

        composable(ScreenRoute.Home.name) {
            sharedUiViewmodel.apply {
                updateTitle("Home")
                hideTrailingIcon()
                disableNavigateBack()
            }
            HomeScreen(
                displayCards = displayCards,
                onNavigateSearch = { type, title ->
                    val data = type.name
                    navController.navigate("${ScreenRoute.Search.name}/$data/$title")
                })
        }

        composable("${ScreenRoute.Search.name}/{data}/{title}") {
            val title = it.arguments?.getString("title")
            val type = ProductType.valueOf(it.arguments?.getString("data")!!)

            sharedUiViewmodel.apply {
                updateTitle(title ?: "Search")
                hideTrailingIcon()
                enableNavigateBack()
            }

            SearchScreen(
                productType = type,
                onProductItemClick = { product, isExynos ->
                    Log.d(TAG, "NavigationGraph: $product")
                    navController.navigate(
                        "${ScreenRoute.Details.name}/${product.name}/${getProductType(product.content_type)}/${product.path}"
                    )
                })
        }

        composable("${ScreenRoute.Details.name}/{product}/{productType}/{path}") {
            val product = it.arguments?.getString("product") ?: ""
            val path = it.arguments?.getString("path") ?: ""
            val productType = ProductType.valueOf(it.arguments?.getString("productType") ?: "")

            //val route = if (path) "${product.toPath()}-exynos" else product.toPath()

            Log.d(TAG, "NavigationGraph: $path")

            sharedUiViewmodel.apply {
                updateTitle(product)
                enableNavigateBack()
                showTrailingIcon()
            }
            LaunchedEffect(Unit) {
                sharedUiViewmodel.hideCompareBottomSheet()
            }

            ProductDetailScreen(
                product = path,
                productType = productType,
                showBottomSheet = sharedUiState.showCompareBottomSheet,
                onDismissBottomSheet = sharedUiViewmodel::hideCompareBottomSheet,
                onCompare = { firstDevice, secondDevice ->
                    sharedUiViewmodel.hideCompareBottomSheet()
                    navController.navigate("${ScreenRoute.Compare.name}/$route/$secondDevice/$productType")
                }
            )
        }

        composable("${ScreenRoute.Compare.name}/{firstDevice}/{secondDevice}/{productType}") {
            val first = it.arguments?.getString("firstDevice").safe { it }
            val second = it.arguments?.getString("secondDevice").safe { it }
            val productType = ProductType.valueOf(it.arguments?.getString("productType") ?: "")

            sharedUiViewmodel.apply {
                updateTitle("Comparison")
                hideTrailingIcon()
            }

            CompareScreen(
                firstDevice = first,
                secondDevice = second,
                productType = productType
            )
        }
    }
}