package com.example.perpusucp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perpusucp.data.entity.Kategori
import com.example.perpusucp.data.repository.PerpusRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class KategoriViewModel(private val repository: PerpusRepository) : ViewModel() {

    val allKategori: StateFlow<List<Kategori>> = repository.getAllKategori()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var kategoriUiState by mutableStateOf(KategoriUiState())
        private set

    fun updateUiState(kategori: KategoriUiState) {
        kategoriUiState = kategori.copy()
    }

    fun saveKategori() {
        if (validateInput()) {
            viewModelScope.launch {
                try {
                    repository.insertKategori(kategoriUiState.toKategori())
                    kategoriUiState = KategoriUiState()
                } catch (e: Exception) {

                }
            }
        }
    }

    fun deleteKategori(kategori: Kategori, moveToUncategorized: Boolean) {
        viewModelScope.launch {
            try {
                repository.deleteKategoriSafe(kategori, moveToUncategorized)
            } catch (e: Exception) {

            }
        }
    }

    private fun validateInput(uiState: KategoriUiState = kategoriUiState): Boolean {
        return uiState.nama.isNotBlank()
    }
}

data class KategoriUiState(
    val id: Int = 0,
    val nama: String = "",
    val parentId: Int? = null
)

fun KategoriUiState.toKategori(): Kategori = Kategori(
    id = id,
    nama = nama,
    parentId = parentId
)