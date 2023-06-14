package com.example.speechazure.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class PermViewModel: ViewModel() {
    // []
    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeLast()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if(!isGranted) {
            visiblePermissionDialogQueue.add(0, permission)
        }
    }
}