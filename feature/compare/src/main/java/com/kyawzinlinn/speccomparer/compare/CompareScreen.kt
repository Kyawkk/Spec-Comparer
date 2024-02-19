package com.kyawzinlinn.speccomparer.compare

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kyawzinlinn.speccomparer.compare.components.CompareCard
import com.kyawzinlinn.speccomparer.components.CompareScoreBar
import com.kyawzinlinn.speccomparer.components.CompareScoreRow
import com.kyawzinlinn.speccomparer.components.SpecColumnItem
import com.kyawzinlinn.speccomparer.data.DataSource
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareDetailResponse
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareKeyDifferences
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareResponse
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareScore
import com.kyawzinlinn.speccomparer.domain.model.compare.KeyDifference
import com.kyawzinlinn.speccomparer.domain.utils.ImageUrlBuilder
import com.kyawzinlinn.speccomparer.domain.utils.ProductType
import com.kyawzinlinn.speccomparer.domain.utils.Resource

@Composable
fun CompareScreen(
    firstDevice: String,
    secondDevice: String,
    productType: ProductType,
    compareViewModel: CompareViewModel = hiltViewModel()
) {
    var data by remember { mutableStateOf("") }
    val compareResponse by compareViewModel.compareResponse.collectAsStateWithLifecycle()
    var compareDetail by remember { mutableStateOf<CompareResponse?>(null) }

    LaunchedEffect(Unit) {
        compareViewModel.compare(firstDevice,secondDevice,productType)
    }

    LaunchedEffect (compareResponse) {
        when (compareResponse){
            is Resource.Loading -> "Loading"
            is Resource.Success -> {
                compareDetail = (compareResponse as Resource.Success<CompareResponse>).data
            }
            is Resource.Error -> (compareResponse as Resource.Error<CompareResponse>).message
            else -> ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (compareDetail != null) CompareDetailContent(compareDetail!!)
    }
}

@Composable
private fun CompareDetailContent(
    compareResponse: CompareResponse, modifier: Modifier = Modifier
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        item { HeaderSection(compareHeaderDetails = compareResponse.compareDeviceHeaderDetails!!) }
        item { KeyDifferences(keyDifferences = compareResponse.keyDifferences!!) }
        items(compareResponse.compareSpecDetails) {
            CompareSpecItem(
                firstDevice = compareResponse.compareDeviceHeaderDetails!!.firstDeviceName,
                secondDevice = compareResponse.compareDeviceHeaderDetails!!.secondDeviceName,
                compareDetailResponse = it!!
            )
        }
    }
}

@Composable
private fun CompareSpecItem(
    firstDevice: String,
    secondDevice: String,
    compareDetailResponse: CompareDetailResponse,
    modifier: Modifier = Modifier
) {
    CompareCard(title = compareDetailResponse.title, modifier = modifier) {
        compareDetailResponse.scoreBars.forEach {compareScoreBar ->
            CompareScoreBar(
                firstDevice = firstDevice,
                secondDevice = secondDevice,
                compareScoreBar = compareScoreBar!!
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        compareDetailResponse.scoreRows.forEach { scoreRow ->
            CompareScoreRow(compareScoreRow = scoreRow!!)
        }
    }
}

@Composable
private fun KeyDifferences(
    keyDifferences: CompareKeyDifferences,
    modifier: Modifier = Modifier
) {
    CompareCard(title = keyDifferences.title, modifier = modifier) {
        KeyDifferenceItem(keyDifference = keyDifferences.firstKeyDifference)
        Spacer(modifier = Modifier.height(16.dp))
        KeyDifferenceItem(keyDifference = keyDifferences.secondDifference)
    }
}

@Composable
fun KeyDifferenceItem(
    keyDifference: KeyDifference,
    modifier: Modifier = Modifier
) {
    Column (modifier = modifier) {
        Text(text = keyDifference.title, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        keyDifference.pros.forEach {
            Row {
                Icon(
                    imageVector = Icons.Default.Check,
                    tint = Color(0xFF198754),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = it, color = Color.DarkGray)
            }
        }
    }
}

@Composable
private fun HeaderSection(
    compareHeaderDetails: CompareScore,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    CompareCard(
        title = "${compareHeaderDetails.firstDeviceName} vs ${compareHeaderDetails.secondDeviceName}",
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            Row {
                Column(
                    modifier = Modifier.weight(0.5f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = compareHeaderDetails.firstScore)
                    AsyncImage(
                        model = ImageRequest
                            .Builder(context)
                            .data(ImageUrlBuilder.build(compareHeaderDetails.firstImgUrl))
                            .error(com.google.android.material.R.drawable.ic_clock_black_24dp)
                            .build(),
                        modifier = Modifier.size(100.dp),
                        contentDescription = null
                    )
                    Text(text = compareHeaderDetails.firstDeviceName)
                }

                Column(
                    modifier = Modifier.weight(0.5f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = compareHeaderDetails.secondScore)
                    AsyncImage(
                        model = ImageRequest
                            .Builder(context)
                            .data(ImageUrlBuilder.build(compareHeaderDetails.secondImgUrl))
                            .error(com.google.android.material.R.drawable.ic_clock_black_24dp)
                            .build(),
                        modifier = Modifier.size(100.dp),
                        contentDescription = null
                    )
                    Text(text = compareHeaderDetails.secondDeviceName)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CompareScreenPreview() {
    CompareScreen(
        firstDevice = "First Device",
        secondDevice = "Second Device",
        productType = ProductType.Smartphone
    )
}