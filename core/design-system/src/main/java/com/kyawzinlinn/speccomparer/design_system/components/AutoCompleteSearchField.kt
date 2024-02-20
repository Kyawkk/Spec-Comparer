@file:OptIn(ExperimentalMaterial3Api::class)

package com.kyawzinlinn.speccomparer.design_system.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.kyawzinlinn.speccomparer.domain.model.Product

@Composable
fun AutoCompleteSearchField(
    defaultValue: String = "",
    modifier: Modifier = Modifier,
    suggestions: List<Product>,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    var selectedValue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var searchFieldSize by remember { mutableStateOf(Size.Zero) }
    var inputValue by remember { mutableStateOf(defaultValue) }

    LaunchedEffect(defaultValue) {
        inputValue = defaultValue
    }

    LaunchedEffect (suggestions) {
        if (suggestions.isEmpty()) expanded = false
    }

    LaunchedEffect(inputValue) {
        expanded = suggestions.isNotEmpty()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        SearchBar(
            input = inputValue,
            onValueChange = {
                inputValue = it
                onValueChange(it)
            },
            onSearch = {
                expanded = false
                onSearch(it)
            },
            modifier = Modifier.onGloballyPositioned { coordinates ->
                    searchFieldSize = coordinates.size.toSize()
                },
        )
        Spacer(modifier = Modifier.height(8.dp))
        AnimatedVisibility(visible = expanded) {
            Card {
                LazyColumn(modifier = Modifier.height(180.dp)) {
                    items(suggestions) {
                        Text(text = it.name, modifier = Modifier
                            .clickable {
                                expanded = false
                                inputValue = it.name
                                onSearch(it.name)
                                selectedValue = it.name
                            }
                            .padding(16.dp)
                            .fillMaxWidth())
                    }
                }
            }
        }

        /*ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            SearchBar(
                input = inputValue,
                onValueChange = {
                    inputValue = it
                    onValueChange(it)
                },
                onSearch = onSearch,
                modifier = Modifier
                    .menuAnchor()
                    .onGloballyPositioned { coordinates ->
                        searchFieldSize = coordinates.size.toSize()
                    },
            )
            ExposedDropdownMenu(
                expanded = expanded,
                modifier = Modifier.width(with(LocalDensity.current) { searchFieldSize.width.toDp() }),
                onDismissRequest = { expanded = false }) {
                suggestions.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.name, modifier = Modifier.fillMaxWidth()) },
                        onClick = {
                            inputValue = it.name
                            onSearch(it.name)
                            selectedValue = it.name
                            expanded = false
                        }
                    )
                }
            }
        }*/
    }
}

@Composable
fun ExposedDropdownMenuSample() {
    val options = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
    var expanded by remember { mutableStateOf(true) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    LaunchedEffect(textFieldSize) {
        Log.d("TAG", "ExposedDropdownMenuSample: ${textFieldSize.width}")
    }

    // We want to react on tap/press on TextField to show menu
    Row {
        ExposedDropdownMenuBox(
            expanded = expanded,
            modifier = Modifier.weight(1f),
            onExpandedChange = { expanded = it },
        ) {
            TextField(
                // The `menuAnchor` modifier must be passed to the text field for correctness.
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .background(Color.Green)
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    },
                readOnly = true,
                value = selectedOptionText,
                onValueChange = {},
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Green, focusedContainerColor = Color.Green
                ),
                label = { Text("Label") },
            )
            ExposedDropdownMenu(
                expanded = expanded,
                modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() }),
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        modifier = Modifier.background(Color.Green),
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ExposedDropDownPreview() {
    ExposedDropdownMenuSample()
}