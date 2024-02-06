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
    val suggestions by viewModel.suggestions.collectAsState()

    NavHost(
        navController = navController, modifier = modifier, startDestination = ScreenRoute.Home.name
    ) {

        composable(ScreenRoute.Home.name) {
            viewModel.apply {
                updateTitle("Home")
                updateTrailingIconVisibility(false)
                updateNavigateBackButtonVisibility(false)
            }
            HomeScreen(
                onNavigateSearch = { type, title ->
                    val data = type.name
                    navController.navigate("${ScreenRoute.Search.name}/$data/$title")
                })
        }

        composable("${ScreenRoute.Search.name}/{data}/{title}") {
            val title = it.arguments?.getString("title")
            val isSearching by viewModel.isSearching.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.apply {
                    resetSearchResults()
                    resetProductSpecifications()
                }
            }

            viewModel.apply {
                updateTitle(title ?: "Search")
                updateTrailingIconVisibility(false)
                updateNavigateBackButtonVisibility(true)
            }
            val type = ProductType.valueOf(it.arguments?.getString("data")!!)

            SearchScreen(
                suggestions = uiState.suggestions,
                isSearching = isSearching,
                uiState = uiState,
                onValueChange = { if (it.isNotEmpty()) viewModel.getSuggestions(it, 8, type) },
                onSearch = { viewModel.search(it, 500, type) },
                onSuggestionItemClick = {
                    viewModel.search(it, 500, type)
                },
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
            val isSearching by viewModel.isSearching.collectAsState()

            viewModel.apply {
                updateTitle(product)
                updateNavigateBackButtonVisibility(true)
                updateTrailingIconVisibility(true)
            }
            LaunchedEffect(Unit) {
                viewModel.showBottomSheet(false)
            }

            ProductDetailScreen(
                uiState = uiState,
                isSearching = isSearching,
                suggestions = uiState.suggestions,
                onValueChange = {
                    viewModel.getSuggestions(it, 8, productType)
                },
                onDismissBottomSheet = { viewModel.showBottomSheet(false) },
                onCompare = { firstDevice, secondDevice ->
                    viewModel.compareProducts(firstDevice, secondDevice, productType)
                    viewModel.showBottomSheet(false)
                    navController.navigate("${ScreenRoute.Compare.name}/$firstDevice/$secondDevice")
                }
            )
        }

        composable("${ScreenRoute.Compare.name}/{firstDevice}/{secondDevice}") {
            val first = it.arguments?.getString("firstDevice")
            val second = it.arguments?.getString("secondDevice")

            viewModel.apply {
                updateTitle("$first Vs $second")
                updateTrailingIconVisibility(false)
            }

            CompareScreen(compareResponse = uiState.compareDetails)
        }
    }
}