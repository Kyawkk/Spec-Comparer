package com.kyawzinlinn.speccomparer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SharedUiViewmodel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(SharedUiState())
    val uiState: StateFlow<SharedUiState> = _uiState.asStateFlow()

    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun enableNavigateBack() {
        _uiState.update { it.copy(canNavigateBack = true) }
    }

    fun disableNavigateBack() {
        _uiState.update { it.copy(canNavigateBack = false) }
    }

    fun showTrailingIcon(){
        _uiState.update { it.copy(showTrailingIcon = true) }
    }

    fun hideTrailingIcon(){
        _uiState.update { it.copy(showTrailingIcon = false) }
    }

    fun showCompareBottomSheet() {
        _uiState.update { it.copy(showCompareBottomSheet = true) }
    }

    fun hideCompareBottomSheet() {
        _uiState.update { it.copy(showCompareBottomSheet = false) }
    }
}

data class SharedUiState(
    val title: String = "",
    val canNavigateBack: Boolean = false,
    val showTrailingIcon: Boolean = false,
    val showCompareBottomSheet: Boolean = false
)