package com.br.fitagro_frontend.domain.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    var errorMessage: MutableStateFlow<String?> = _errorMessage
    var isResultLoading = MutableStateFlow(false)


}