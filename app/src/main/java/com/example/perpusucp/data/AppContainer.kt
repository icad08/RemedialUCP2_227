package com.example.perpusucp.data

import android.content.Context
import com.example.perpusucp.data.repository.OfflinePerpusRepository
import com.example.perpusucp.data.repository.PerpusRepository

interface AppContainer {
    val perpusRepository: PerpusRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val perpusRepository: PerpusRepository by lazy {
        OfflinePerpusRepository(PerpusDatabase.getDatabase(context))
    }
}