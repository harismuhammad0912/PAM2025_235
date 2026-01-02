package com.example.freshcycle.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.freshcycle.FreshCycleApplication

fun CreationExtras.freshCycleApplication(): FreshCycleApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FreshCycleApplication)

object PenyediaViewModel {
    val Factory = viewModelFactory {
        // Nanti kita akan mendaftarkan LoginViewModel, HomeViewModel, dll di sini
    }
}
