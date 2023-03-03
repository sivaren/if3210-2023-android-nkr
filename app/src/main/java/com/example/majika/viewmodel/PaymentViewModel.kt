package com.example.majika.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.majika.network.MajikaApi
import kotlinx.coroutines.launch

private const val TAG = "PaymentViewModel"

class PaymentViewModel : ViewModel() {
    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    fun getStatus(code: String) {
        viewModelScope.launch {
            try {
                _status.value = MajikaApi.retrofitService.getPaymentStatus(code).status
                Log.d(TAG, "code: $code")
                Log.d(TAG, "status: ${status.value}")
            } catch (e: Exception) {
                _status.value = "Failure: ${e.message}"
                Log.d(TAG, "Failure: ${e.message}")
            }
        }
    }
}