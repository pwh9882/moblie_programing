package com.example.mobile_programing.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(private val application: Application): ViewModel() {
    init {
        viewModelScope.launch {  }
    }
}