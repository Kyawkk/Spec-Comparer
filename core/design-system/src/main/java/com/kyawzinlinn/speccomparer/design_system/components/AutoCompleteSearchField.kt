@file:OptIn(ExperimentalMaterial3Api::class)

package com.kyawzinlinn.speccomparer.design_system.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
    val TAG = "AutoCompleteSearchField"

    var selectedValue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var inputValue by remember { mutableStateOf(defaultValue) }
    var hasFocused by remember { mutableStateOf(false) }
    var hasSearched by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

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
                onSearch(it)
            },
        )
        Spacer(modifier = Modifier.height(8.dp))
        AnimatedVisibility(visible = expanded && showSuggestions) {
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
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        expanded = false
                                        hasSearched = true
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
        }
    }
}
