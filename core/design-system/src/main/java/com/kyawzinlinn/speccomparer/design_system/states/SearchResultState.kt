package com.kyawzinlinn.speccomparer.design_system.states

import androidx.compose.runtime.State
import com.kyawzinlinn.speccomparer.domain.utils.Resource

sealed class SearchResultState{
    data object Loading: SearchResultState()
    data object Error: SearchResultState()
    data object Success: SearchResultState()
    data object Empty: SearchResultState()
    data object Default: SearchResultState()
    data object Nothing: SearchResultState()
}