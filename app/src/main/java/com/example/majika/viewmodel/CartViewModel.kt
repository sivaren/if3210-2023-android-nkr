package com.example.majika.viewmodel

import androidx.lifecycle.*
import com.example.majika.model.Fnb
import com.example.majika.repository.CartRepository
import kotlinx.coroutines.launch

private const val TAG = "CartViewModel"

class CartViewModel(private val repository: CartRepository) : ViewModel() {
    val allFnbs: LiveData<List<Fnb>> = repository.allFnb.asLiveData()

    // insert particular fnb instance to call FnbDao
    private fun insertFnb (fnb: Fnb) =
        viewModelScope.launch { repository.insertFnb(fnb) }

    // create new Fnb instance
    private fun newFnbEntry (fnbName: String, fnbPrice: String, fnbQuantity: String): Fnb {
        return Fnb (
            fnbName = fnbName,
            fnbPrice = fnbPrice.toInt(),
            fnbQuantity = fnbQuantity.toInt()
        )
    }

    fun resetFnb() {
        viewModelScope.launch { repository.resetFnb() }
    }

    // add new fnb that to be used in CartFragment
    fun addNewFnb (fnbName: String, fnbPrice: String, fnbQuantity: String = "1") = viewModelScope.launch {
        repository.addNewFnb(fnbName, fnbPrice, fnbQuantity)
    }
    // add particular fnb qty that will call FnbDao update
    // and to be used in CartFragment
    fun addFnbQuantity (fnb: Fnb) = viewModelScope.launch {
        repository.addFnbQuantity(fnb)
    }
    // delete particular fnb instance to call FnbDao
    private fun deleteFnb (fnb: Fnb) = viewModelScope.launch {
        repository.deleteFnb(fnb)
    }
    // remove fnb qty if qty > 0, otherwise delete the fnb instance
    fun removeFnbQuantity (fnb: Fnb) = viewModelScope.launch {
        repository.removeFnbQuantity(fnb)
    }

    fun removeFnbQuantityByNameAndPrice (name: String, price: String) = viewModelScope.launch {
        repository.removeFnbQuantityByNameAndPrice(name, price)
    }
}

class CartViewModelFactory(private val repository: CartRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
