package com.example.laslistan

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class LaslistanUiState(
    val nyTitel: String = "",
    val bocker: List<Bok> = startbok,
)

class LaslistanViewModel : ViewModel() {
    var uiState by mutableStateOf(LaslistanUiState())
        private set

    fun onNewTitleChange(text: String) {
        uiState = uiState.copy(nyTitel = text)
    }
}