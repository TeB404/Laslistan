package com.example.laslistan


import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class LaslistanUiState(
    val nyTitel: String = "",
    val bocker: List<Bok> = startbok,
)

class LaslistanViewModel : ViewModel() {
    var uiState by mutableStateOf(LaslistanUiState())
        private set

    private var nextId = 4

    fun onNewTitleChange(text: String) {
        uiState = uiState.copy(nyTitel = text)
    }

    fun addBok() {
        val titel = uiState.nyTitel.trim()
        if (titel.isEmpty()) return
        val bok = Bok(
            id = nextId,
            titel = titel,
            ar = 0,
            genre = "Okänd",
        )
        nextId = nextId + 1
        uiState = uiState.copy(
            nyTitel = "",
            bocker = uiState.bocker + bok,
        )
    }

    fun toggleSeen(id: Int) {
        uiState = uiState.copy(
            bocker = uiState.bocker.map { if (it.id == id) it.copy(sedd = !it.sedd) else it}
        )
    }

    fun removeBok(id: Int) {
        uiState = uiState.copy(bocker = uiState.bocker.filter { it.id != id})
    }
}