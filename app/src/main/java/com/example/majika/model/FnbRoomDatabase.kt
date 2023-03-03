package com.example.majika.model

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Fnb::class], version = 3, exportSchema = false)
abstract class FnbRoomDatabase : RoomDatabase() {
    abstract fun fnbDao(): FnbDao

    companion object {
        @Volatile
        private var INSTANCE: FnbRoomDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): FnbRoomDatabase {
            Log.d("FnbRoomDatabase", "getDatabase is called")
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder (
                    context.applicationContext,
                    FnbRoomDatabase::class.java,
                    "fnb_database"
                ).fallbackToDestructiveMigration().build()
                Log.d("FnbRoomDatabase", "database build success")
                INSTANCE = instance
                return instance
            }
        }
    }
}
