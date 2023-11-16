package com.kyawzinlinn.speccomparer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.kyawzinlinn.speccomparer.domain.model.Product
import com.kyawzinlinn.speccomparer.presentation.search.SearchViewModel
import com.kyawzinlinn.speccomparer.ui.theme.SpecComparerTheme
import com.kyawzinlinn.speccomparer.utils.ProductType
import com.kyawzinlinn.speccomparer.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: SearchViewModel = hiltViewModel()
            viewModel.search("xiaomi",20,ProductType.Smartphone)
            val searchResultsState by viewModel.searchResults.collectAsState(Resource.Loading())

            SpecComparerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    
                    Greeting(
                        when (searchResultsState) {
                            is Resource.Loading -> "Loading..."
                            is Resource.Success -> (searchResultsState as Resource.Success<List<Product>>).data.toString()
                            is Resource.Error -> (searchResultsState as Resource.Error<List<Product>>).message
                        }
                    )
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