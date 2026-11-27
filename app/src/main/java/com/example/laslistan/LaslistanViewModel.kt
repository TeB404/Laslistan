package com.example.laslistan


import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class LaslistanUiState(
    val nyTitel: String = "",
    val editingId: Int? = null,
    val nyKategori: String = "Fantasyroman",
    val bocker: List<Bok> = listOf(
        Bok(1, "Harry Potter: Den vises sten", 2001, "Fantasyroman"),
        Bok(2, "Tunnlar", 2007, "Äventyr"),
        Bok(3, "Matilda", 1988, "Fantasy")
    )
)

val kategorier: List<String> = listOf(
    "Fantasyroman",
    "Äventyr",
    "Fantasy"
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
            kategori = uiState.nyKategori,
        )
        nextId += 1
        uiState = uiState.copy(
            nyTitel = "",
            bocker = uiState.bocker + Bok(
                id = nextId,
                titel = titel,
                ar = 0,
                kategori = uiState.nyKategori
            )
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

    fun startEdit(bok: Bok) {
        uiState = uiState.copy(
            nyTitel = bok.titel,
            editingId = bok.id,
        )
    }

    fun saveEdit() {
        val titel = uiState.nyTitel.trim()
        if (titel.isEmpty()) return
        val id = uiState.editingId
        uiState = uiState.copy(
            nyTitel = "",
            editingId = null,
            bocker = uiState.bocker.map {
                if (it.id == id) it.copy(titel = titel) else it
            },
        )
    }

    fun onKategoriChange(kategori: String) {
        uiState = uiState.copy(nyKategori = kategori)
    }
}