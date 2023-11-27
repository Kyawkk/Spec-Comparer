@file:OptIn(ExperimentalMaterial3Api::class)

package com.kyawzinlinn.speccomparer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.kyawzinlinn.speccomparer.presentation.navigation.NavigationGraph
import com.kyawzinlinn.speccomparer.presentation.search.ProductViewModel
import com.kyawzinlinn.speccomparer.ui.theme.SpecComparerTheme
import com.kyawzinlinn.speccomparer.utils.ProductType
import com.kyawzinlinn.speccomparer.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: ProductViewModel = hiltViewModel()
            val searchResultsState by viewModel.compareResponse.collectAsState(Resource.Loading())
            val navController = rememberNavController()
            val uiState by viewModel.uiState.collectAsState()

            SpecComparerTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(text = uiState.title) },
                            navigationIcon = {
                                if (uiState.canNavigateBack) {
                                    IconButton(onClick = {navController.navigateUp()}) {
                                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                                    }
                                }
                            }
                        )
                    }
                ){
                    NavigationGraph(viewModel = viewModel, navController = navController, modifier = Modifier.padding(it))
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpecComparerTheme {
        Greeting("Android")
    }
}