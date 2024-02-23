package com.kyawzinlinn.speccomparer.design_system.extensions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


fun <T> LazyListScope.products(
    query: String = "",
    hasSearched: Boolean = false,
    isSearching: Boolean,
    items: List<T>,
    key: ((item: T) -> Any)? = null,
    contentType: (item: T) -> Any? = { null },
    itemContent: @Composable LazyItemScope.(item: T) -> Unit
) = run {
    if (!hasSearched && items.isEmpty()) item {
        Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Search anything...")
        }
    }
    if (hasSearched && items.isEmpty() && !isSearching) item {
        Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No results for \"$query\"!")
        }
    }
    else {
        items(count = items.size,
            key = if (key != null) { index: Int -> key(items[index]) } else null,
            contentType = { index: Int -> contentType(items[index]) }) {
            itemContent(items[it])
        }
    }
}