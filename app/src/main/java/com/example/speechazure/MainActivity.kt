package com.example.speechazure

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.speechazure.screens.HomeScreen
import com.example.speechazure.ui.theme.SpeechAzureTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpeechAzureTheme {
                /*
                * State the list of permission that the program needs
                */
                val permissionsState = rememberMultiplePermissionsState(
                    permissions = listOf(android.Manifest.permission.INTERNET,
                    android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
                    android.Manifest.permission.RECORD_AUDIO)
                )

                /*
                * With lifecycleOwner we are able to access the lifecycle of the activity.
                * We want to ask for not granted permissions in onResume.
                */
                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(key1 = lifecycleOwner,
                    effect = {
                        val observer = LifecycleEventObserver { _, event ->
                            if(event == Lifecycle.Event.ON_RESUME) {
                                permissionsState.launchMultiplePermissionRequest()
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)

                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    })

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    permissionsState.permissions.forEach { perm ->
                        when(perm.permission) {
                            android.Manifest.permission.INTERNET -> {
                                when {
                                    perm.status.isGranted -> {
                                        Log.d("TEST", "INTERNET permission is granted")
                                    }
                                    perm.status.shouldShowRationale -> {
                                        Log.d("TEST", "Show window for INTERNET permission")
                                    }
                                    perm.isPermanentlyDenied() -> {
                                        Log.d("TEST", "INTERNET permission is NOT granted")
                                    }
                                }
                            }
                            android.Manifest.permission.MODIFY_AUDIO_SETTINGS -> {
                                when {
                                    perm.status.isGranted -> {
                                        Log.d("TEST", "MODIFY_AUDIO_SETTINGS permission is granted")
                                    }
                                    perm.status.shouldShowRationale -> {
                                        Log.d("TEST", "Show window for MODIFY_AUDIO_SETTINGS permission")
                                    }
                                    perm.isPermanentlyDenied() -> {
                                        Log.d("TEST", "MODIFY_AUDIO_SETTINGS permission is NOT granted")
                                    }
                                }
                            }
                            android.Manifest.permission.RECORD_AUDIO -> {
                                when {
                                    perm.status.isGranted -> {
                                        Log.d("TEST", "RECORD_AUDIO permission is granted")
                                        Text(text = "INTERNET permission accepted")
                                    }
                                    perm.status.shouldShowRationale -> {
                                        Log.d("TEST", "Show window for RECORD_AUDIO permission")
                                    }
                                    perm.isPermanentlyDenied() -> {
                                        Log.d("TEST", "RECORD_AUDIO permission is NOT granted")
                                    }
                                }
                            }
                        }
                    }
                    HomeScreen()
                }
            }
        }
    }
}
