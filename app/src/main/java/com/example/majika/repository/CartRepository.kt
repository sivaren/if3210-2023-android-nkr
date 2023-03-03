package com.example.majika.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.majika.model.Fnb
import com.example.majika.model.FnbDao
import kotlinx.coroutines.flow.Flow

class CartRepository(private val fnbDao: FnbDao) {
    val allFnb: Flow<List<Fnb>> = fnbDao.getFnbs()

    // get table row length that will call FnbDao
    suspend fun getTableRowLength(): LiveData<Int> {
        return fnbDao.getRowLength().asLiveData()
    }
    // retrieve first fnb data that will call FnbDao
    suspend fun retrieveFirstFnb(): LiveData<Fnb> {
        return fnbDao.getFnb().asLiveData()
    }
    // insert particular fnb instance to call FnbDao
    suspend fun insertFnb (fnb: Fnb) {
        fnbDao.insert(fnb)
    }
    // create new Fnb instance
    private fun newFnbEntry (fnbName: String, fnbPrice: String, fnbQuantity: String): Fnb {
        return Fnb (
            fnbName = fnbName,
            fnbPrice = fnbPrice.toInt(),
            fnbQuantity = fnbQuantity.toInt()
        )
    }

    suspend fun resetFnb() {
        Log.d("CartRepo", "reset fnb")
        fnbDao.clearFnb()
    }

    // add new fnb that to be used in CartFragment
    suspend fun addNewFnb (fnbName: String, fnbPrice: String, fnbQuantity: String) {
        val newFnb: Fnb = newFnbEntry(fnbName, fnbPrice, fnbQuantity)
            val fnb: Fnb = fnbDao.getFnbByNameAndPrice(fnbName, fnbPrice.toInt())
            if (fnb == null) {
//                Log.d(TAG, "fnb is not exist, insert to db")
                insertFnb(newFnb)
            } else {
//                Log.d(TAG, "fnb is exist, increase its quantity")
                addFnbQuantity(fnb)
            }

    }
    // add particular fnb qty that will call FnbDao update
    // and to be used in CartFragment
    suspend fun addFnbQuantity (fnb: Fnb) {
        val updatedFnb: Fnb = fnb.copy(fnbQuantity = fnb.fnbQuantity + 1)
        fnbDao.update(updatedFnb)
    }
    // delete particular fnb instance to call FnbDao
    suspend fun deleteFnb (fnb: Fnb) {
        fnbDao.delete(fnb)
    }
    // remove fnb qty if qty > 0, otherwise delete the fnb instance
    suspend fun removeFnbQuantity (fnb: Fnb) {
        if(fnb.fnbQuantity - 1 > 0) {
            val updatedFnb: Fnb = fnb.copy(fnbQuantity = fnb.fnbQuantity - 1)
            fnbDao.update(updatedFnb)
        } else {
            deleteFnb(fnb)
        }
    }

    suspend fun removeFnbQuantityByNameAndPrice (name: String, price: String) {
            val fnb: Fnb = fnbDao.getFnbByNameAndPrice(name, price.toInt())
            if (fnb != null) {
                if(fnb.fnbQuantity > 1) {
                    val updatedFnb: Fnb = fnb.copy(fnbQuantity = fnb.fnbQuantity - 1)
                    fnbDao.update(updatedFnb)
                } else {
                    deleteFnb(fnb)
                }
            }
    }
}