package com.kronos.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.kronos.core.presentation.navigation.Navegacion
import com.kronos.core.presentation.theme.BackgroundUltraDark
import com.kronos.core.presentation.theme.KronosTheme
import com.kronos.core.presentation.viewmodel.KronosViewModel
import com.kronos.core.presentation.viewmodel.KronosViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: KronosViewModel by viewModels {
        KronosViewModelFactory((application as KronosApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KronosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundUltraDark
                ) {
                    val navController = rememberNavController()
                    val uiState by viewModel.uiState.collectAsState()

                    Navegacion(
                        navController = navController,
                        viewModel = viewModel,
                        uiState = uiState
                    )
                }
            }
        }
    }
}
