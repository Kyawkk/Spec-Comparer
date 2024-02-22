package com.kyawzinlinn.speccomparer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareScoreRow

@Composable
fun CompareScoreRow(
    compareScoreRow: CompareScoreRow,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = compareScoreRow.name, modifier = Modifier.weight(0.3f))
            Text(text = compareScoreRow.first, modifier = Modifier.weight(0.35f))
            VerticalDivider()
            Text(text = compareScoreRow.second, modifier = Modifier.weight(0.35f))
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
    }
}