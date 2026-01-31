package com.example.perpusucp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.perpusucp.ui.screens.HomeScreen
import com.example.perpusucp.ui.screens.ManagementScreen
import com.example.perpusucp.ui.theme.PerpusucpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PerpusucpTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf("home") }

                    when (currentScreen) {
                        "home" -> HomeScreen(
                            onNavigateToManagement = { currentScreen = "management" }
                        )
                        "management" -> ManagementScreen(
                            onNavigateBack = { currentScreen = "home" }
                        )
                    }
                }
            }
        }
    }
}