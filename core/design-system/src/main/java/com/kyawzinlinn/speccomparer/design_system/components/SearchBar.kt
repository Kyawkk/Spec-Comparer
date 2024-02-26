package com.kyawzinlinn.speccomparer.design_system.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    input : String,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    readOnly: Boolean,
    onFocusChanged: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var value by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(input) {
        value = input
    }

    TextField(
        value = TextFieldValue(
            text = value,
            selection = TextRange(value.length)
        ),
        maxLines = 1,
        readOnly = readOnly,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { onFocusChanged(it.isFocused) }
            .border(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(0.3f)),
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(6.dp)),
        placeholder = { Text("Search device...") },
        shape = RoundedCornerShape(10.dp),
        leadingIcon = { Icon(imageVector = Icons.Rounded.Search, tint = Color.Gray, contentDescription = null) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
                onSearch(value)
            }
        ),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        ),
        onValueChange = {
            value = it.text
            onValueChange(value)
        }
    )
}