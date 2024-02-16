package com.kyawzinlinn.speccomparer.design_system.components

import android.util.Log
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import com.kyawzinlinn.speccomparer.domain.model.Product

@Composable
fun AutoCompleteSearchField(
    defaultValue: String = "",
    modifier: Modifier = Modifier,
    suggestions: List<Product>,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    val TAG = "AutoCompleteSearchField"
    var selectedValue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var searchFieldSize by remember { mutableStateOf(Size.Zero) }
    var inputValue by remember { mutableStateOf(defaultValue) }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect (defaultValue) {
        inputValue = defaultValue
    }

    LaunchedEffect(inputValue) {
        expanded = suggestions.isNotEmpty()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .onGloballyPositioned { coordinates -> searchFieldSize = coordinates.size.toSize() }
    ) {
        SearchBar(
            input = inputValue,
            onValueChange = {
                inputValue = it
                onValueChange(it)
                            },
            onSearch = onSearch,
            modifier = Modifier
                .fillMaxWidth()
                .indication(interactionSource, LocalIndication.current),
        )
        DropdownMenu(
            expanded = expanded,
            properties = PopupProperties(dismissOnClickOutside = true),
            modifier = Modifier.width(with(LocalDensity.current) { searchFieldSize.width.toDp() }),
            onDismissRequest = { expanded = false }) {
            suggestions.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.name) },
                    onClick = {
                        inputValue = it.name
                        onSearch(it.name)
                        selectedValue = it.name
                        expanded = false
                    }
                )
            }
        }
    }
}