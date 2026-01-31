package com.example.perpusucp.ui.viewmodel

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.perpusucp.PerpusApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            KategoriViewModel(perpusApplication().container.perpusRepository)
        }
        initializer {
            BukuViewModel(perpusApplication().container.perpusRepository)
        }
    }
}

fun CreationExtras.perpusApplication(): PerpusApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as PerpusApplication)