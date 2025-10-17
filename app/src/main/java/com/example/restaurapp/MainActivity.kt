package com.example.restaurapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.restaurapp.ui.theme.RestaurAppTheme
import com.example.restaurapp.ui.screens.HomeScreen


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RestaurAppTheme{
                HomeScreen();
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview(){
    RestaurAppTheme{
        HomeScreen();
    }
}