@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.kyawzinlinn.speccomparer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.kyawzinlinn.speccomparer.presentation.ProductViewModel
import com.kyawzinlinn.speccomparer.presentation.navigation.NavigationGraph
import com.kyawzinlinn.speccomparer.ui.components.TopBar
import com.kyawzinlinn.speccomparer.ui.theme.SpecComparerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val viewModel: ProductViewModel = hiltViewModel()
            val navController = rememberNavController()
            val uiState by viewModel.uiState.collectAsState()

            SpecComparerTheme {
                Scaffold(
                    topBar = {
                        TopBar(
                            title = uiState.title,
                            canNavigateBack = uiState.canNavigateBack,
                            showTrailingIcon = uiState.showTrailingIcon,
                            onTrailingIconClick = { viewModel.showBottomSheet(true) },
                            navigateUp = { navController.navigateUp() },
                        )
                    }
                ) {
                    NavigationGraph(
                        viewModel = viewModel,
                        navController = navController,
                        modifier = Modifier.padding(it)
                    )
                }
            }
        }
    }
}
