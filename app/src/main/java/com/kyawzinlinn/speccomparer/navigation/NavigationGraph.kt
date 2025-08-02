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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kyawzinlinn.speccomparer.SharedUiViewmodel
import com.kyawzinlinn.speccomparer.compare.CompareScreen
import com.kyawzinlinn.speccomparer.details.ProductDetailScreen
import com.kyawzinlinn.speccomparer.domain.utils.ProductType
import com.kyawzinlinn.speccomparer.domain.utils.safe
import com.kyawzinlinn.speccomparer.domain.utils.toProductType
import com.kyawzinlinn.speccomparer.home.HomeScreen
import com.kyawzinlinn.speccomparer.search.SearchScreen

@Composable
fun NavigationGraph(
    sharedUiViewmodel: SharedUiViewmodel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val sharedUiState by sharedUiViewmodel.uiState.collectAsStateWithLifecycle()
    val currentDestination = navController.currentBackStackEntryAsState()

    LaunchedEffect (currentDestination.value?.destination?.route) {
        Log.d("TAG", "NavigationGraph: ${currentDestination.value?.destination?.route} ${ScreenRoute.Home::class.qualifiedName}")
        when (currentDestination.value?.destination?.route) {
            ScreenRoute.Home::class.qualifiedName -> {
                sharedUiViewmodel.apply {
                    updateTitle("Home")
                    hideTrailingIcon()
                    disableNavigateBack()
                }
            }
            ScreenRoute.Search::class.qualifiedName -> {
                sharedUiViewmodel.apply {
                    hideTrailingIcon()
                    enableNavigateBack()
                }
            }
            ScreenRoute.Compare::class.qualifiedName -> {
                sharedUiViewmodel.apply {
                    enableNavigateBack()
                    showTrailingIcon()
                }
            }
            ScreenRoute.Details::class.qualifiedName -> {
                sharedUiViewmodel.apply {
                    enableNavigateBack()
                    showTrailingIcon()
                }
            }
        }
    }

    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = ScreenRoute.Home.name
    ) {

        composable(ScreenRoute.Home.name) {
//            sharedUiViewmodel.apply {
//                updateTitle("Home")
//                hideTrailingIcon()
//                disableNavigateBack()
//            }
            HomeScreen(
                onNavigateSearch = { type, title ->
                    val data = type.name
                    navController.navigate("${ScreenRoute.Search.name}/$data/$title")
                }
            )
        }

        composable("${ScreenRoute.Search.name}/{data}/{title}") {
            val title = it.arguments?.getString("title")
            val type = ProductType.valueOf(it.arguments?.getString("data")!!)

            sharedUiViewmodel.apply {
                updateTitle(title ?: "Search")
            }

            SearchScreen(
                productType = type,
                onProductItemClick = { product ->
                    navController.navigate(
                        "${ScreenRoute.Details.name}/${product.name}/${product.content_type.toProductType()}/${product.path}"
                    )
                })
        }

        composable("${ScreenRoute.Details.name}/{product}/{productType}/{path}") {
            val product = it.arguments?.getString("product") ?: ""
            val path = it.arguments?.getString("path") ?: ""
            val productType = ProductType.valueOf(it.arguments?.getString("productType") ?: "")

            sharedUiViewmodel.apply {
                updateTitle(product)
            }

            LaunchedEffect(Unit) {
                sharedUiViewmodel.hideCompareBottomSheet()
            }

            ProductDetailScreen(
                product = path,
                productType = productType,
                showBottomSheet = sharedUiState.showCompareBottomSheet,
                onDismissBottomSheet = sharedUiViewmodel::hideCompareBottomSheet,
                onCompare = { secondDevice ->
                    sharedUiViewmodel.hideCompareBottomSheet()
                    navController.navigate("${ScreenRoute.Compare.name}/$path/$secondDevice/$productType")
                }
            )
        }

        composable("${ScreenRoute.Compare.name}/{firstDevice}/{secondDevice}/{productType}") {

            val first = it.arguments?.getString("firstDevice").safe { it }
            val second = it.arguments?.getString("secondDevice").safe { it }
            val productType = ProductType.valueOf(it.arguments?.getString("productType") ?: "")

            sharedUiViewmodel.apply {
                updateTitle("Comparison")
            }

            CompareScreen(
                firstDevice = first,
                secondDevice = second,
                productType = productType
            )
        }
    }
}