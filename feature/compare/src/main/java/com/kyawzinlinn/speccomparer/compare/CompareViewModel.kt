package com.kyawzinlinn.speccomparer.compare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyawzinlinn.speccomparer.domain.model.compare.CompareResponse
import com.kyawzinlinn.speccomparer.domain.utils.ProductType
import com.kyawzinlinn.speccomparer.domain.utils.Resource
import com.kyawzinlinn.speccomparer.network.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompareViewModel @Inject constructor(
   private val repository: ProductRepository
) : ViewModel() {
    private val _compareResponse = MutableStateFlow<Resource<CompareResponse>>(Resource.Default)
    val compareResponse: StateFlow<Resource<CompareResponse>> = _compareResponse.asStateFlow()

    fun compare(firstDevice: String, secondDevice: String, type: ProductType) {
        viewModelScope.launch (Dispatchers.IO) {
            _compareResponse.value = Resource.Loading
            delay(500)
            _compareResponse.value = repository.compareProducts(firstDevice, secondDevice, type)
        }
    }
}