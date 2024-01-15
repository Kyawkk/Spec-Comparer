package com.kyawzinlinn.speccomparer.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyawzinlinn.speccomparer.domain.model.Product

@Composable
fun AutoCompleteSearchField(
    modifier: Modifier = Modifier,
    isSearching: Boolean,
    suggestions: List<Product>,
    onValueChange: (String) -> Unit
) {
    var selectedValue by remember {
        mutableStateOf("")
    }
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    var inputValue by remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    expanded = false
                }
            )
    ) {
        SearchBar(input = inputValue, onValueChange = { inputValue = it }, onSearch = onValueChange)
        if (!isSearching) {
            DropdownMenu(expanded = selectedValue.isNotEmpty(), modifier = Modifier.fillMaxWidth(), onDismissRequest = { selectedValue = "" }) {
                suggestions.forEach {
                    DropdownMenuItem(text = { Text(text = it.name) }, onClick = {
                        selectedValue = it.name
                        onValueChange(it.name)
                    })
                }
            }
        }
    }
}