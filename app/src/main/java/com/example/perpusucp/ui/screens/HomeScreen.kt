package com.example.perpusucp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.perpusucp.data.entity.Buku
import com.example.perpusucp.data.entity.Kategori
import com.example.perpusucp.ui.viewmodel.AppViewModelProvider
import com.example.perpusucp.ui.viewmodel.BukuViewModel
import com.example.perpusucp.ui.viewmodel.KategoriViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToManagement: () -> Unit,
    kategoriViewModel: KategoriViewModel = viewModel(factory = AppViewModelProvider.Factory),
    bukuViewModel: BukuViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val kategoriList by kategoriViewModel.allKategori.collectAsState()
    val bukuList by bukuViewModel.allBuku.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedKategori by remember { mutableStateOf<Kategori?>(null) }
    var moveBukuToUncategorized by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Sistem Perpus UCP") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToManagement) {
                Icon(Icons.Default.Add, contentDescription = "Tambah")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text("Daftar Kategori", style = MaterialTheme.typography.titleLarge)
                }
                items(kategoriList) { kategori ->
                    KategoriItem(
                        kategori = kategori,
                        onDelete = {
                            selectedKategori = kategori
                            showDeleteDialog = true
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Daftar Buku (Simulasi)", style = MaterialTheme.typography.titleLarge)
                }
                items(bukuList) { buku ->
                    BukuItem(
                        buku = buku,
                        onPinjam = {
                            bukuViewModel.pinjamBuku(buku.id)
                            Toast.makeText(context, "Buku dipinjam! Coba hapus kategorinya.", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }

        if (showDeleteDialog && selectedKategori != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Hapus Kategori?") },
                text = {
                    Column {
                        Text("Apakah Anda yakin ingin menghapus kategori '${selectedKategori?.nama}'?")
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = moveBukuToUncategorized,
                                onCheckedChange = { moveBukuToUncategorized = it }
                            )
                            Text("Pindahkan buku ke 'Tanpa Kategori'")
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            scope.launch {
                                try {
                                    selectedKategori?.let {
                                        kategoriViewModel.deleteKategori(it, moveBukuToUncategorized)
                                    }
                                    showDeleteDialog = false
                                    moveBukuToUncategorized = false
                                } catch (e: Exception) {
                                    // Tangkap Error Rollback disini
                                    showDeleteDialog = false
                                    snackbarHostState.showSnackbar(e.message ?: "Gagal hapus")
                                }
                            }
                        }
                    ) {
                        Text("Hapus")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Batal")
                    }
                }
            )
        }
    }
}

@Composable
fun KategoriItem(kategori: Kategori, onDelete: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = kategori.nama, style = MaterialTheme.typography.titleMedium)
                if (kategori.parentId != null) {
                    Text(text = "Sub-Kategori dari ID: ${kategori.parentId}", style = MaterialTheme.typography.bodySmall)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Hapus")
            }
        }
    }
}

@Composable
fun BukuItem(buku: Buku, onPinjam: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = buku.judul, style = MaterialTheme.typography.titleMedium)
                Text(text = "Kategori ID: ${buku.kategoriId ?: "Tanpa Kategori"}", style = MaterialTheme.typography.bodySmall)
            }
            OutlinedButton(
                onClick = onPinjam,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Blue)
            ) {
                Text("Pinjam", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}