package com.kyawzinlinn.speccomparer.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kyawzinlinn.speccomparer.utils.ProductType

@Composable
fun HomeScreen(
    onNavigateSearch: (ProductType) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {onNavigateSearch(ProductType.Laptop)}) {
            Text("Compare")
        }
    }
}