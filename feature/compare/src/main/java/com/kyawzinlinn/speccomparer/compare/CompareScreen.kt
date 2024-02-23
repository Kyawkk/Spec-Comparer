@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.kyawzinlinn.speccomparer.compare

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kyawzinlinn.speccomparer.design_system.components.CompareScoreBar
import com.kyawzinlinn.speccomparer.design_system.components.CompareScoreRow
import com.kyawzinlinn.speccomparer.design_system.components.ExpandableCard
import com.kyawzinlinn.speccomparer.design_system.components.NetworkImage
import com.kyawzinlinn.speccomparer.design_system.components.handleResponse
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareDetailResponse
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareKeyDifferences
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareResponse
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareScore
import com.kyawzinlinn.speccomparer.domain.model.compare.KeyDifference
import com.kyawzinlinn.speccomparer.domain.utils.IMG_PREFIX
import com.kyawzinlinn.speccomparer.domain.utils.ProductType
import com.kyawzinlinn.speccomparer.domain.utils.safe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun CompareScreen(
    firstDevice: String,
    secondDevice: String,
    productType: ProductType,
    compareViewModel: CompareViewModel = hiltViewModel()
) {
    val compareResponse by compareViewModel.compareResponse.collectAsStateWithLifecycle()
    var compareDetail by remember { mutableStateOf<CompareResponse?>(null) }

    LaunchedEffect(Unit) {
        compareViewModel.compare(firstDevice, secondDevice, productType)
    }

    handleResponse(
        resource = compareResponse,
        onSuccess = {
            compareDetail = it
        },
        onRetry = {
            compareViewModel.compare(firstDevice, secondDevice, productType)
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (compareDetail != null) CompareDetailContent(
            compareResponse = compareDetail!!,
        )
    }
}

@Composable
private fun CompareDetailContent(
    compareResponse: CompareResponse,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()
    var showStickyTitle by remember { mutableStateOf(false) }
    LaunchedEffect(scrollState.firstVisibleItemIndex) {
        withContext(Dispatchers.IO) {
            showStickyTitle = scrollState.firstVisibleItemIndex != 0
        }
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier,
        state = scrollState,
    ) {
        stickyHeader {
            AnimatedVisibility(
                visible = showStickyTitle,
                enter = fadeIn() + slideInVertically { -it },
                exit = slideOutVertically { -it } + fadeOut() + shrinkOut(),
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                StickyTitleRow(
                    firstDevice = compareResponse.compareDeviceHeaderDetails?.firstDeviceName ?: "",
                    secondDevice = compareResponse.compareDeviceHeaderDetails?.secondDeviceName
                        ?: ""
                )
            }
        }
        item {
            key(compareResponse.compareDeviceHeaderDetails?.firstDeviceName) {
                HeaderSection(compareHeaderDetails = compareResponse.compareDeviceHeaderDetails)
            }
        }
        item { KeyDifferences(keyDifferences = compareResponse.keyDifferences) }
        items(compareResponse.compareSpecDetails) {
            key(it?.title) {
                CompareSpecItem(
                    firstDevice = compareResponse.compareDeviceHeaderDetails.safe { headerDetails -> headerDetails.firstDeviceName },
                    secondDevice = compareResponse.compareDeviceHeaderDetails.safe { headerDetails -> headerDetails.secondDeviceName },
                    compareDetailResponse = it,
                    hasExpanded = false
                )
            }
        }
    }
}

@Composable
fun StickyTitleRow(
    firstDevice: String,
    secondDevice: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = firstDevice,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                modifier = Modifier.weight(0.45f),
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp
            )
            Text(
                text = "Vs",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(0.1f),
                fontSize = 20.sp
            )
            Text(
                text = secondDevice,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                modifier = Modifier.weight(0.45f),
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider()
    }
}

@Composable
private fun CompareSpecItem(
    firstDevice: String,
    secondDevice: String,
    hasExpanded: Boolean,
    compareDetailResponse: CompareDetailResponse?,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(hasExpanded) }

    LaunchedEffect(hasExpanded) {
        expanded = hasExpanded
    }

    ExpandableCard(
        title = compareDetailResponse?.title ?: "",
        modifier = modifier,
        onExpandedChanged = { expanded = it }) {
        compareDetailResponse?.scoreBars?.forEach { compareScoreBar ->
            CompareScoreBar(
                expanded = expanded,
                firstDevice = firstDevice,
                secondDevice = secondDevice,
                compareScoreBar = compareScoreBar!!
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        compareDetailResponse?.scoreRows?.forEach { scoreRow ->
            CompareScoreRow(compareScoreRow = scoreRow!!)
        }
    }
}

@Composable
private fun KeyDifferences(
    keyDifferences: CompareKeyDifferences?,
    modifier: Modifier = Modifier
) {
    ExpandableCard(title = keyDifferences?.title ?: "", modifier = modifier) {
        KeyDifferenceItem(keyDifference = keyDifferences?.firstKeyDifference)
        Spacer(modifier = Modifier.height(16.dp))
        if (keyDifferences?.secondDifference?.title!!.isNotEmpty()) KeyDifferenceItem(keyDifference = keyDifferences?.secondDifference)
    }
}

@Composable
fun KeyDifferenceItem(
    keyDifference: KeyDifference?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = keyDifference?.title ?: "", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        keyDifference?.pros?.forEach {
            Row {
                Icon(
                    imageVector = Icons.Default.Check,
                    tint = Color(0xFF198754),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = it, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
private fun HeaderSection(
    compareHeaderDetails: CompareScore?,
    modifier: Modifier = Modifier,
) {
    val firstDevice = compareHeaderDetails.safe { it.firstDeviceName }
    val secondDevice = compareHeaderDetails.safe { it.secondDeviceName }
    ExpandableCard(
        title = "$firstDevice vs $secondDevice",
        expandable = false,
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HeaderDeviceItem(
                    modifier = Modifier.weight(0.5f),
                    score = compareHeaderDetails.safe { it.firstScore },
                    device = compareHeaderDetails.safe { it.firstDeviceName },
                    imageUrl = compareHeaderDetails.safe { it.firstImgUrl },
                    onRetry = { /*TODO*/ })

                HeaderDeviceItem(
                    modifier = Modifier.weight(0.5f),
                    score = compareHeaderDetails.safe { it.secondScore },
                    device = compareHeaderDetails.safe { it.secondDeviceName },
                    imageUrl = compareHeaderDetails.safe { it.secondImgUrl },
                    onRetry = { /*TODO*/ })
            }
        }
    }
}

@Composable
private fun HeaderDeviceItem(
    score: String,
    device: String,
    imageUrl: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (score.trim().isNotEmpty()) Text(text = score)
        NetworkImage(
            imageUrl = "$IMG_PREFIX$imageUrl",
            modifier = Modifier.size(100.dp),
            onRetrySuccess = {},
            onRetry = onRetry,
            onErrorItemRemove = {}
        )
        Text(text = device, textAlign = TextAlign.Center)
    }
}