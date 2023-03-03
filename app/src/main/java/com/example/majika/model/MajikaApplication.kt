package com.example.majika.model

import android.app.Application
import com.example.majika.repository.CartRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MajikaApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database: FnbRoomDatabase by lazy { FnbRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy {
        CartRepository(database.fnbDao())
    }
}
