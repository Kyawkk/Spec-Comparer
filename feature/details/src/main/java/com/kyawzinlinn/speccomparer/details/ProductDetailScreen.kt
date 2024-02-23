@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.kyawzinlinn.speccomparer.details

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kyawzinlinn.speccomparer.design_system.components.ExpandableCard
import com.kyawzinlinn.speccomparer.design_system.components.handleResponse
import com.kyawzinlinn.speccomparer.design_system.components.CompareBottomSheet
import com.kyawzinlinn.speccomparer.design_system.extensions.dividerColor
import com.kyawzinlinn.speccomparer.domain.model.smartphone.ProductSpecificationResponse
import com.kyawzinlinn.speccomparer.domain.model.smartphone.SpecificationColumn
import com.kyawzinlinn.speccomparer.domain.model.smartphone.SpecificationItem
import com.kyawzinlinn.speccomparer.domain.model.smartphone.SpecificationTable
import com.kyawzinlinn.speccomparer.domain.utils.ImageUrlBuilder
import com.kyawzinlinn.speccomparer.domain.utils.ProductType

@Composable
fun ProductDetailScreen(
    product: String,
    productType: ProductType,
    showBottomSheet: Boolean,
    onCompare: (String, String) -> Unit,
    onDismissBottomSheet: () -> Unit,
    modifier: Modifier = Modifier,
    detailViewModel: DetailViewModel = hiltViewModel()
) {
    val detailResponse by detailViewModel.detailResponse.collectAsStateWithLifecycle()
    val suggestions by detailViewModel.suggestions.collectAsStateWithLifecycle()
    var productSpecification by remember { mutableStateOf<ProductSpecificationResponse?>(null) }

    LaunchedEffect(Unit) { detailViewModel.resetSuggestions() }

    LaunchedEffect(Unit) { if(productSpecification == null) detailViewModel.getProductDetailSpecification(product, productType) }

    if (productSpecification == null) {
        handleResponse(resource = detailResponse,
            onRetry = { detailViewModel.getProductDetailSpecification(product, productType) }) {
            productSpecification = it
        }
    }

    CompareBottomSheet(
        suggestions = suggestions,
        onValueChange = { detailViewModel.getSuggestions(it, productType) },
        firstDevice = productSpecification?.productSpecification?.productName ?: "",
        showBottomSheet = showBottomSheet,
        onCompare = onCompare,
        onDismissBottomSheet = onDismissBottomSheet
    )

    Column(modifier = modifier.fillMaxSize()) {
        if (productSpecification != null) ProductDetailContent(productSpecificationResponse = productSpecification!!)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecItemList(specificationItem: SpecificationItem, modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    ExpandableCard(
        title = specificationItem.title,
        modifier = modifier,
        onExpandedChanged = {
            expanded = it
        }
    ) {
        specificationItem.specificationsColumn.forEach {
            SpecColumnItem(expanded = expanded, specificationColumn = it)
        }
        Spacer(modifier = Modifier.height(8.dp))
        specificationItem.specificationsTable.forEach {
            SpecTableItem(it)
        }
    }
}

@Composable
fun SpecTableItem(specificationTable: SpecificationTable, modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = specificationTable.title, style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = specificationTable.value
            )
        }
        Spacer(Modifier.height(8.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.dividerColor)
    }
}

@Composable
fun SpecColumnItem(
    expanded: Boolean,
    specificationColumn: SpecificationColumn,
    modifier: Modifier = Modifier
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(expanded) {
        isExpanded = expanded
    }

    val animatedProgress by animateFloatAsState(
        targetValue = if (isExpanded) specificationColumn.progress.toFloat() / 100f else 0f,
        animationSpec = tween(1000),
        label = ""
    )
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = specificationColumn.name
            )
            Text(
                text = specificationColumn.value
            )
        }
        Spacer(Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.onSecondary,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProductDetailContent(
    productSpecificationResponse: ProductSpecificationResponse, modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {

        item {
            Card(
                modifier = Modifier
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(ImageUrlBuilder.build(productSpecificationResponse.productSpecification.productImageUrl))
                            .crossfade(true).build(),
                        modifier = Modifier.weight(0.2f),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = null
                    )
                    Column(
                        modifier = Modifier.weight(0.8f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        productSpecificationResponse.productSpecification.productDetails.forEach {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = it.name, style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.weight(0.3f)
                                )
                                Text(
                                    text = "${it.value.replaceFirstChar { "" }}",
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.weight(0.7f)
                                )
                            }
                        }
                    }
                }
            }
        }

        items(productSpecificationResponse.productSpecifications) { specification ->
            key (specification.title) {
                SpecItemList(specification, modifier = Modifier.animateItemPlacement())
            }
        }
    }
}