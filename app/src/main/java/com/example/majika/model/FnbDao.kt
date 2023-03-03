package com.example.majika.model

import android.content.ClipData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FnbDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(fnb: Fnb)
    @Update
    suspend fun update(fnb: Fnb)
    @Delete
    suspend fun delete(fnb: Fnb)
    @Query("SELECT * FROM fnb LIMIT 1") // still dummy for first row
    fun getFnb(): Flow<Fnb>
    @Query("SELECT * FROM fnb ORDER BY name ASC")
    fun getFnbs(): Flow<List<Fnb>>
    @Query("SELECT COUNT(*) FROM fnb") // to check row length in the table
    fun getRowLength(): Flow<Int>
    @Query("SELECT * FROM fnb WHERE name = :name AND price = :price")
    suspend fun getFnbByNameAndPrice(name: String, price: Int): Fnb
    @Query("DELETE FROM fnb")
    suspend fun clearFnb()
}
