@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.kyawzinlinn.speccomparer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.kyawzinlinn.speccomparer.design_system.components.TopBar
import com.kyawzinlinn.speccomparer.design_system.theme.SpecComparerTheme
import com.kyawzinlinn.speccomparer.navigation.NavigationGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val sharedUiViewmodel: SharedUiViewmodel = hiltViewModel()
            val navController = rememberNavController()
            val uiState by sharedUiViewmodel.uiState.collectAsStateWithLifecycle()
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

            SpecComparerTheme {
                Scaffold(
                    modifier = Modifier,
                    topBar = {
                        TopBar(
                            title = uiState.title,
                            canNavigateBack = uiState.canNavigateBack,
                            showTrailingIcon = uiState.showTrailingIcon,
                            onTrailingIconClick = sharedUiViewmodel::showCompareBottomSheet,
                            navigateUp = navController::navigateUp,
                        )
                    }
                ) {
                    NavigationGraph(
                        sharedUiViewmodel = sharedUiViewmodel,
                        navController = navController,
                        modifier = Modifier.padding(it)
                    )
                }
            }
        }
    }
}
