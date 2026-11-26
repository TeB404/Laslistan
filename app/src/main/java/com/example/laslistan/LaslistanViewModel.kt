package com.example.laslistan

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class LaslistanUiState(
    val bocker: List<Bok> = startbok,
)

class LaslistanViewModel : ViewModel() {
    var uiState by mutableStateOf(LaslistanUiState())
        private set
}