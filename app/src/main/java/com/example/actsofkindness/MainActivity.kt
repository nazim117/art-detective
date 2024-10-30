package com.example.actsofkindness

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.example.actsofkindness.ui.theme.ActsOfKindnessTheme

class MainActivity : ComponentActivity() {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var showPermissionDialog by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the permission launcher before setContent
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                // Show the permission dialog if permission is denied
                showPermissionDialog = true
            }
        }

        // Check camera permission on launch
        checkCameraPermission()

        setContent {
            ActsOfKindnessTheme {
                MainScreen()

                // Show permission request dialog if needed
                if (showPermissionDialog) {
                    AlertDialog(
                        onDismissRequest = { showPermissionDialog = false },
                        title = { Text("Camera Permission Needed") },
                        text = { Text("This app requires camera access to take photos.") },
                        confirmButton = {
                            Button(onClick = {
                                showPermissionDialog = false
                                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }) {
                                Text("Allow")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showPermissionDialog = false }) {
                                Text("Deny")
                            }
                        }
                    )
                }
            }
        }
    }

    // Function to check camera permission
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            showPermissionDialog = true
        }
    }
}
