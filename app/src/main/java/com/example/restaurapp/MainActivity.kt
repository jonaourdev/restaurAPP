package com.example.restaurapp

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.restaurapp.ui.theme.RestaurAppTheme
import com.example.restaurapp.ui.HomeScreen
import com.example.restaurapp.ui.RegisterScreen
import com.example.restaurapp.viewmodel.RegisterViewModel
import com.example.restaurapp.viewmodel.RegisterViewModelFactory


class MainActivity : ComponentActivity() {

    private val vmRegister: RegisterViewModel by viewModels {
        RegisterViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestaurAppTheme {
                RegisterScreen(
                    vm = vmRegister,
                    onBack = {  }, // Cierra la activity o navega atrás finish()
                    onSaved = {
                        vmRegister.guardar() // Llama la lógica de guardado
                    }
                )
            }
        }
    }
}


