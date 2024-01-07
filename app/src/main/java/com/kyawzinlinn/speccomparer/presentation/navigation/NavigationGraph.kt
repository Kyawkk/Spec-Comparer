package com.kyawzinlinn.speccomparer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kyawzinlinn.speccomparer.presentation.ProductViewModel
import com.kyawzinlinn.speccomparer.presentation.compare.CompareScreen
import com.kyawzinlinn.speccomparer.presentation.detail.ProductDetailScreen
import com.kyawzinlinn.speccomparer.presentation.home.HomeScreen
import com.kyawzinlinn.speccomparer.presentation.search.SearchScreen
import com.kyawzinlinn.speccomparer.utils.ProductType
import com.kyawzinlinn.speccomparer.utils.getProductType

@Composable
fun NavigationGraph(
    viewModel: ProductViewModel, navController: NavHostController, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    NavHost(
        navController = navController, modifier = modifier, startDestination = ScreenRoute.Home.name
    ) {

        composable(ScreenRoute.Home.name) {
            viewModel.apply {
                updateTitle("Home")
                updateTrailingIconStatus(false)
                updateNavigateBackStatus(false)
            }
            HomeScreen(
                onNavigateSearch = { type, title ->
                    val data = type.name
                    navController.navigate("${ScreenRoute.Search.name}/$data/$title")
                })
        }

        composable("${ScreenRoute.Search.name}/{data}/{title}") {
            val title = it?.arguments?.getString("title")

            LaunchedEffect (Unit) {
                viewModel.resetSearchResults()
            }

            viewModel.apply {
                updateTitle(title ?: "Search")
                updateTrailingIconStatus(false)
                updateNavigateBackStatus(true)
            }
            val type = ProductType.valueOf(it.arguments?.getString("data")!!)

            SearchScreen(
                uiState = uiState,
                onValueChange = { viewModel.getSuggestions(it, 8, type) },
                onSearch = { viewModel.search(it, 500, type) },
                onProductItemClick = {
                    navController.navigate(
                        "${ScreenRoute.Details.name}/${it.name}/${
                            getProductType(
                                it.content_type
                            )
                        }"
                    )
                    viewModel.apply {
                        getFirstProductSpecifications(it.name, getProductType(it.content_type))
                    }
                })
        }

        composable("${ScreenRoute.Details.name}/{product}/{productType}") {
            val product = it.arguments?.getString("product") ?: ""
            val productType = ProductType.valueOf(it.arguments?.getString("productType") ?: "")

            viewModel.apply {
                updateTitle(product)
                updateNavigateBackStatus(true)
                updateTrailingIconStatus(true)
            }
            LaunchedEffect(Unit) {
                viewModel.showBottomSheet(false)
            }

            ProductDetailScreen(
                uiState = uiState,
                onDismissBottomSheet = { viewModel.showBottomSheet(false) },
                onCompare = { firstDevice, secondDevice ->
                    viewModel.compareProducts(firstDevice, secondDevice, productType)
                    viewModel.showBottomSheet(false)
                    navController.navigate("${ScreenRoute.Compare.name}/$firstDevice/$secondDevice")
                }
            )
        }

        composable("${ScreenRoute.Compare.name}/{firstDevice}/{secondDevice}"){
            val first = it.arguments?.getString("firstDevice")
            val second = it.arguments?.getString("secondDevice")

            viewModel.apply {
                updateTitle("$first Vs $second")
                updateTrailingIconStatus(false)
            }

            CompareScreen(compareResponse = uiState.compareDetails)
        }
    }
}