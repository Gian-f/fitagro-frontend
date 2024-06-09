package com.br.fitagro_frontend.domain.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.fitagro_frontend.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    var errorMessage: MutableStateFlow<String?> = _errorMessage

    var isResultLoading = MutableStateFlow(false)
    private val _inputName = MutableStateFlow("")


    var isErrorDialogVisible = MutableStateFlow(false)
    val inputName: StateFlow<String> = _inputName

    private val _successMessage = MutableStateFlow<String?>(null)
    var successMessage: MutableStateFlow<String?> = _successMessage

    private val _classeToxicologica = MutableStateFlow<String?>(null)
    var classeToxicologica: MutableStateFlow<String?> = _classeToxicologica

    private val _classeAmbiental = MutableStateFlow<String?>(null)
    var classeAmbiental: MutableStateFlow<String?> = _classeAmbiental


    private val _resultName = MutableStateFlow<String?>(null)
    var resultName: MutableStateFlow<String?> = _resultName

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri

    private val _imageBitmap = MutableStateFlow<Bitmap?>(null)
    val imageBitmap: StateFlow<Bitmap?> = _imageBitmap

    private val _classificationResult = MutableStateFlow("")
    val classificationResult: StateFlow<String> = _classificationResult

    fun onImageCaptured(bitmap: Bitmap) {
        _imageBitmap.value = bitmap
    }

    fun onInputBarcodeChange(name: String) {
        _inputName.value = name
    }

    fun setIsErrorDialogVisible(value: Boolean) {
        isErrorDialogVisible.value = value
    }

    fun clearErrorMessage() {
        _errorMessage.value = ""
    }

    fun clearBarcodeInput() {
        _inputName.value = ""
    }

    fun sendName(name: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        isResultLoading.value = true
        viewModelScope.launch {
            runCatching {
                val result = repository.searchFruit(name)
                if (result.isSuccess) {
                    resultName.value = name
                    _classeToxicologica.value =
                        result.getOrNull().orEmpty()[0].classeToxicologica
                    _classeAmbiental.value = result.getOrNull()
                        .orEmpty()[0].classeAmbiental
                    clearErrorMessage()
                    clearBarcodeInput()
                    isResultLoading.value = true
                    onSuccess.invoke()
                }
            }.onFailure {
                isResultLoading.value = false
                _errorMessage.value =
                    it.message ?: "Ocorreu um erro ao tentar fazer uma requisição!"
                clearErrorMessage()
                clearBarcodeInput()
                onFailure.invoke()
            }
        }
    }
}