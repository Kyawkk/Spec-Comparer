package com.kyawzinlinn.speccomparer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kyawzinlinn.speccomparer.ProductViewModel
import com.kyawzinlinn.speccomparer.R
import com.kyawzinlinn.speccomparer.SharedUiViewmodel
import com.kyawzinlinn.speccomparer.compare.CompareScreen
import com.kyawzinlinn.speccomparer.details.ProductDetailScreen
import com.kyawzinlinn.speccomparer.domain.model.DisplayCard
import com.kyawzinlinn.speccomparer.domain.utils.ProductType
import com.kyawzinlinn.speccomparer.domain.utils.getProductType
import com.kyawzinlinn.speccomparer.home.HomeScreen
import com.kyawzinlinn.speccomparer.search.SearchScreen

val displayCards = listOf(
    DisplayCard(R.drawable.smartphone,"Smartphones","Make an in-depth comparison of various phones to see which is better in terms of camera quality, performance, battery life, and value for money.", ProductType.Smartphone),
    DisplayCard(R.drawable.smartphone_chip,"Smartphone Processors","Make an in-depth comparison of various phones to see which is better in terms of camera quality, performance, battery life, and value for money.", ProductType.Soc),
    DisplayCard(R.drawable.processor,"Laptop CPUs","Make an in-depth comparison of various phones to see which is better in terms of camera quality, performance, battery life, and value for money.", ProductType.Cpu),
    DisplayCard(R.drawable.laptop,"Laptops","Make an in-depth comparison of various phones to see which is better in terms of camera quality, performance, battery life, and value for money.", ProductType.Laptop),
)

@Composable
fun NavigationGraph(
    sharedUiViewmodel: SharedUiViewmodel,
    viewModel: ProductViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
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
                onProductItemClick = {
                    navController.navigate(
                        "${ScreenRoute.Details.name}/${it.name}/${getProductType(it.content_type)}"
                    )
                })
        }

        composable("${ScreenRoute.Details.name}/{product}/{productType}") {
            val product = it.arguments?.getString("product") ?: ""
            val productType = ProductType.valueOf(it.arguments?.getString("productType") ?: "")
            val isSearching by viewModel.isSearching.collectAsState()

            sharedUiViewmodel.apply {
                updateTitle(product)
                enableNavigateBack()
                showTrailingIcon()
            }
            LaunchedEffect(Unit) {
                sharedUiViewmodel.hideCompareBottomSheet()
            }

            ProductDetailScreen(
                product = product,
                productType = productType,
                uiState = uiState,
                showBottomSheet = sharedUiState.showCompareBottomSheet,
                onDismissBottomSheet = sharedUiViewmodel::hideCompareBottomSheet,
                onCompare = { firstDevice, secondDevice ->
                    viewModel.compareProducts(firstDevice, secondDevice, productType)
                    sharedUiViewmodel.hideCompareBottomSheet()
                    navController.navigate("${ScreenRoute.Compare.name}/$firstDevice/$secondDevice/$productType")
                }
            )
        }

        composable("${ScreenRoute.Compare.name}/{firstDevice}/{secondDevice}/{productType}") {
            val first = it.arguments?.getString("firstDevice") ?: ""
            val second = it.arguments?.getString("secondDevice") ?: ""
            val productType = ProductType.valueOf(it.arguments?.getString("productType") ?: "")

            sharedUiViewmodel.apply {
                updateTitle("$first Vs $second")
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