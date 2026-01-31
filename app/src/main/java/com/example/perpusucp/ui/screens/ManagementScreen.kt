package com.example.perpusucp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.perpusucp.ui.viewmodel.AppViewModelProvider
import com.example.perpusucp.ui.viewmodel.BukuViewModel
import com.example.perpusucp.ui.viewmodel.KategoriViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagementScreen(
    onNavigateBack: () -> Unit,
    kategoriViewModel: KategoriViewModel = viewModel(factory = AppViewModelProvider.Factory),
    bukuViewModel: BukuViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Manajemen Data") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Tambah Kategori Baru", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = kategoriViewModel.kategoriUiState.nama,
                onValueChange = { kategoriViewModel.updateUiState(kategoriViewModel.kategoriUiState.copy(nama = it)) },
                label = { Text("Nama Kategori") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = kategoriViewModel.kategoriUiState.parentId?.toString() ?: "",
                onValueChange = {
                    val parentId = if (it.isBlank()) null else it.toIntOrNull()
                    kategoriViewModel.updateUiState(kategoriViewModel.kategoriUiState.copy(parentId = parentId))
                },
                label = { Text("Parent ID (Opsional)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    kategoriViewModel.saveKategori()
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Simpan Kategori")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Tambah Buku Baru", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = bukuViewModel.bukuUiState.judul,
                onValueChange = { bukuViewModel.updateUiState(bukuViewModel.bukuUiState.copy(judul = it)) },
                label = { Text("Judul Buku") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = bukuViewModel.bukuUiState.kategoriId?.toString() ?: "",
                onValueChange = {
                    val catId = if (it.isBlank()) null else it.toIntOrNull()
                    bukuViewModel.updateUiState(bukuViewModel.bukuUiState.copy(kategoriId = catId))
                },
                label = { Text("Kategori ID") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    bukuViewModel.saveBuku()
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Simpan Buku")
            }
        }
    }
}