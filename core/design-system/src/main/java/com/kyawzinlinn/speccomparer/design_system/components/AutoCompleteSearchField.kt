@file:OptIn(ExperimentalMaterial3Api::class)

package com.kyawzinlinn.speccomparer.design_system.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyawzinlinn.speccomparer.domain.model.Product

@Composable
fun AutoCompleteSearchField(
    defaultValue: String = "",
    modifier: Modifier = Modifier,
    showSuggestions: Boolean = true,
    suggestions: List<Product>,
    readOnly: Boolean = false,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {

    var selectedValue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var inputValue by remember { mutableStateOf(defaultValue) }
    var hasFocused by remember { mutableStateOf(false) }
    var hasSearched by remember { mutableStateOf(false) }

    LaunchedEffect(defaultValue) {
        inputValue = defaultValue
    }

    LaunchedEffect(suggestions) {
        expanded = suggestions.isNotEmpty() && hasFocused && !hasSearched
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        SearchBar(
            input = inputValue,
            readOnly = readOnly,
            onFocusChanged = { hasFocused = it },
            onValueChange = {
                inputValue = it
                hasSearched = false
                onValueChange(it)
            },
            onSearch = {
                expanded = false
                hasSearched = true
                if (inputValue.trim().isNotEmpty()) onSearch(it)
            },
        )
        AnimatedVisibility(visible = inputValue.trim().isEmpty() && hasSearched) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Device name must not be empty!",
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        SuggestionDropdown(
            visible = expanded && showSuggestions,
            suggestions = suggestions,
            onItemClick = {
                selectedValue = it
                expanded = false
                hasSearched = true
                onSearch(it)
                inputValue = it
            }
        )
    }
}

@Composable
private fun SuggestionDropdown(
    visible: Boolean,
    suggestions: List<Product>,
    onItemClick: (String) -> Unit
) {
    AnimatedVisibility(visible = visible) {
        Card {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(0.dp, 180.dp)
            ) {
                items(suggestions) {
                    key(it.name) {
                        Text(
                            text = it.name,
                            modifier = Modifier
                                .clickable {
                                    onItemClick(it.name)
                                }
                                .padding(16.dp)
                                .fillMaxWidth())
                    }
                }
            }
        }
    }
}
