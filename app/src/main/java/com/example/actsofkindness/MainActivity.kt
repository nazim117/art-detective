package com.example.actsofkindness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.actsofkindness.ui.theme.ActsOfKindnessTheme
import com.example.actsofkindness.ui.theme.MainScreen
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            ActsOfKindnessTheme {
                MainScreen()
            }
        }
    }
}