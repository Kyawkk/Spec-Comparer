@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.kyawzinlinn.speccomparer.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kyawzinlinn.speccomparer.domain.model.Product

@Composable
fun CompareBottomSheet(
    firstDevice: String,
    showBottomSheet: Boolean,
    onValueChange: (String) -> Unit,
    onCompare: (String, String) -> Unit,
    onDismissBottomSheet: () -> Unit,
    modifier: Modifier = Modifier,
    suggestions: List<Product>,
    isSearching: Boolean
) {
    var showBottomSheetValue by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(showBottomSheet) {
        showBottomSheetValue = showBottomSheet
    }

    if (showBottomSheetValue) {
        ModalBottomSheet(
            onDismissRequest = onDismissBottomSheet,
            sheetState = sheetState
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
            ) {
                BottomSheetContent(firstDevice = firstDevice, onSearch = onCompare, isSearching = isSearching, suggestions = suggestions, onValueChange = onValueChange)
            }
        }
    }
}

@Composable
private fun BottomSheetContent(
    isSearching: Boolean,
    suggestions: List<Product>,
    firstDevice: String,
    onValueChange: (String) -> Unit,
    onSearch: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var firstDeviceInput by remember { mutableStateOf(firstDevice) }
    var secondDeviceInput by remember { mutableStateOf("") }

    Column (modifier = modifier) {
        SearchDeviceItem(
            title = "First Device",
            inputString = firstDevice,
            onValueChange = { firstDeviceInput = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Second Device", style = MaterialTheme.typography.titleSmall)
        AutoCompleteSearchField(suggestions = suggestions, onSearch = {}, onValueChange = onValueChange)

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = { onSearch(firstDeviceInput, secondDeviceInput) }) {
            Text(text = "compare".uppercase())
        }
    }

}

@Composable
fun SearchDeviceItem(
    title: String,
    inputString: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(8.dp))
        SearchBar(input = inputString, onValueChange = onValueChange, onSearch = {})
    }
}