package com.kyawzinlinn.speccomparer.presentation.compare

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareResponse
import com.kyawzinlinn.speccomparer.utils.Resource

@Composable
fun CompareScreen(
    compareResponse: Resource<CompareResponse>
) {
    var data by remember { mutableStateOf("") }
    
    LaunchedEffect (compareResponse) {
        data = when (compareResponse){
            is Resource.Loading -> "Loading"
            is Resource.Success -> compareResponse.data.toString()
            is Resource.Error -> compareResponse.message
            else -> ""
        }
    }
    
    Column (modifier = Modifier.fillMaxSize()) {
        Text(text = data)
    }
}