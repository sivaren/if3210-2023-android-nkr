package com.example.majika.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.majika.model.BranchItem
import com.example.majika.network.MajikaApi
import com.example.majika.network.MajikaApiService
import kotlinx.coroutines.launch

class BranchViewModel : ViewModel() {
    private val _branchItem = MutableLiveData<List<BranchItem>>()
    val branchItem: LiveData<List<BranchItem>> = _branchItem

    init {
        getAllBranches()
    }

    private fun getAllBranches() {
        viewModelScope.launch {
            try {
                _branchItem.value = MajikaApi.retrofitService.getAllBranch().data
                Log.d("BranchViewModel","Branch loaded count: ${branchItem.value?.size}")
            } catch (e: Exception) {
                Log.d("BranchViewModel", "Failure: ${e.message}")
            }
        }
    }
}
