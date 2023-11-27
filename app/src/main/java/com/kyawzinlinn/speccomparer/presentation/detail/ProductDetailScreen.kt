package com.kyawzinlinn.speccomparer.presentation.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kyawzinlinn.speccomparer.domain.model.smartphone.ProductDetail
import com.kyawzinlinn.speccomparer.domain.model.smartphone.ProductSpecification
import com.kyawzinlinn.speccomparer.domain.model.smartphone.ProductSpecificationResponse
import com.kyawzinlinn.speccomparer.domain.model.smartphone.SpecificationColumn
import com.kyawzinlinn.speccomparer.domain.model.smartphone.SpecificationItem
import com.kyawzinlinn.speccomparer.domain.model.smartphone.SpecificationTable
import com.kyawzinlinn.speccomparer.ui.components.LoadingScreen
import com.kyawzinlinn.speccomparer.utils.ImageUrlBuilder
import com.kyawzinlinn.speccomparer.utils.Resource

@Composable
fun ProductDetailScreen(
    productSpecificationResponseState: Resource<ProductSpecificationResponse>,
    modifier: Modifier = Modifier.padding(16.dp)
) {
    Column(modifier = modifier.fillMaxSize()) {
        when (productSpecificationResponseState) {
            is Resource.Loading -> LoadingScreen()
            is Resource.Success -> ProductDetailContent(productSpecificationResponseState.data!!)
            is Resource.Error -> {}
        }
    }
}

@Composable
fun SpecItemList(specificationItem: SpecificationItem) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column (modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = specificationItem.title,
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                IconButton(onClick = {expanded = !expanded}) {
                    Icon(
                        imageVector = if (!expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = null
                    )
                }
            }


            AnimatedVisibility(
                visible = expanded,
            ) {
                Column {
                    if (specificationItem.specificationsColumn.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(bottom = 12.dp)) {
                            specificationItem.specificationsColumn.forEach {
                                SpecColumnItem(it)
                            }
                        }
                    }
                    if (specificationItem.specificationsTable.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 8.dp)) {
                            specificationItem.specificationsTable.forEach {
                                SpecTableItem(it)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SpecTableItem(specificationTable: SpecificationTable, modifier: Modifier = Modifier) {
    Column (modifier = Modifier.fillMaxWidth()) {
        Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = specificationTable.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = specificationTable.value
            )
        }
        Spacer(Modifier.height(8.dp))
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray))
    }
}

@Composable
fun SpecColumnItem(specificationColumn: SpecificationColumn, modifier: Modifier = Modifier) {
    val animatedProgress by animateFloatAsState(
        targetValue = 0.8f,
        animationSpec = tween(4000)/*spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )*/
    )
    Column (modifier = modifier) {
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
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.onSecondary,
            progress = animatedProgress,
        )
    }
}

@Composable
private fun ProductDetailContent(
    productSpecificationResponse: ProductSpecificationResponse,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            modifier = Modifier
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(ImageUrlBuilder.build(productSpecificationResponse.productSpecification.productImageUrl))
                        .crossfade(true)
                        .build(),
                    modifier = Modifier,
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null
                )
                Spacer(Modifier.width(16.dp))
                Column(
                    modifier = Modifier
                        .weight(0.8f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    productSpecificationResponse.productSpecification.productDetails.forEach {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = " - ${it.value}",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
            }
        }

        productSpecificationResponse.productSpecifications.map { specification ->
            SpecItemList(specification)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductDetailScreenPreview() {
    ProductDetailScreen(
        Resource.Success(
            ProductSpecificationResponse(
                productSpecification = ProductSpecification(
                    "Xiaomi", "", listOf(
                        ProductDetail("Screen", "6.38\" AMOLED - 1080 x 2400"),
                        ProductDetail("SoC", "MediaTek Dimensity 920"),
                        ProductDetail("Camera", "2 (64 MP + 2 MP)"),
                        ProductDetail("Battery", "4500 mAh"),
                        ProductDetail("OS", "Android 13"),
                        ProductDetail("Weight", "173 grams (6.1 oz)"),
                    )
                ),
                productSpecifications = emptyList()
            )
        )
    )
}