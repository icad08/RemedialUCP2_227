package com.example.perpusucp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perpusucp.data.entity.Buku
import com.example.perpusucp.data.repository.PerpusRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BukuViewModel(private val repository: PerpusRepository) : ViewModel() {

    val allBuku: StateFlow<List<Buku>> = repository.getAllBuku()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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

    fun pinjamBuku(bukuId: Int) {
        viewModelScope.launch {
            repository.simulasiPinjamBuku(bukuId)
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