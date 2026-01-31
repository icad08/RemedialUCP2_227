package com.example.perpusucp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perpusucp.data.entity.Buku
import com.example.perpusucp.data.repository.PerpusRepository
import kotlinx.coroutines.launch

class BukuViewModel(private val repository: PerpusRepository) : ViewModel() {

    var bukuUiState by mutableStateOf(BukuUiState())
        private set

    fun updateUiState(detail: BukuUiState) {
        bukuUiState = detail.copy()
    }

    fun saveBuku() {
        if (validateInput()) {
            viewModelScope.launch {
                repository.insertBuku(bukuUiState.toBuku())
                bukuUiState = BukuUiState()
            }
        }
    }

    private fun validateInput(uiState: BukuUiState = bukuUiState): Boolean {
        return uiState.judul.isNotBlank()
    }
}

data class BukuUiState(
    val id: Int = 0,
    val judul: String = "",
    val kategoriId: Int? = null
)

fun BukuUiState.toBuku(): Buku = Buku(
    id = id,
    judul = judul,
    kategoriId = kategoriId
)