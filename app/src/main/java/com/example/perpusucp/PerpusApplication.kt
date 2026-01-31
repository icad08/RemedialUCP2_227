package com.example.perpusucp

import android.app.Application
import com.example.perpusucp.data.AppContainer
import com.example.perpusucp.data.AppDataContainer

class PerpusApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}