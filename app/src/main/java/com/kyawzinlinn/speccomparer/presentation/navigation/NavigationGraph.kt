package com.kyawzinlinn.speccomparer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kyawzinlinn.speccomparer.presentation.detail.ProductDetailScreen
import com.kyawzinlinn.speccomparer.presentation.home.HomeScreen
import com.kyawzinlinn.speccomparer.presentation.search.ProductViewModel
import com.kyawzinlinn.speccomparer.presentation.search.SearchScreen
import com.kyawzinlinn.speccomparer.utils.ProductType
import com.kyawzinlinn.speccomparer.utils.getProductType

@Composable
fun NavigationGraph(
    viewModel: ProductViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val searchResults by viewModel.searchResults.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = ScreenRoute.Home.name
    ) {

        composable(ScreenRoute.Home.name) {
            viewModel.apply {
                updateTitle("Home")
                updateNavigateBackStatus(false)
            }
            HomeScreen(onNavigateSearch = { type ->
                val data = type.name
                navController.navigate("${ScreenRoute.Search.name}/$data")
            })
        }

        composable( "${ScreenRoute.Search.name}/{data}") {

            viewModel.apply {
                updateNavigateBackStatus(false)
                updateTitle("Search")
            }
            val type = ProductType.valueOf(it?.arguments?.getString("data")!!)
            SearchScreen(
                searchResultsState = searchResults,
                onValueChange = {viewModel.search(it,8,type)},
                onSearch = {viewModel.search(it,500,type)},
                onProductItemClick = {
                    navController.navigate("${ScreenRoute.Details.name}/${it.name}/${getProductType(it.content_type)}")
                }
            )
        }

        composable("${ScreenRoute.Details.name}/{product}/{productType}") {
            val product = it?.arguments?.getString("product") ?: ""
            val productType = ProductType.valueOf(it?.arguments?.getString("productType") ?: "")

            viewModel.apply {
                updateTitle(product)
                updateNavigateBackStatus(true)
                getProductSpecification(product,productType)
            }

            ProductDetailScreen(
                productSpecificationResponseState = uiState.productDetails
            )
        }
    }
}