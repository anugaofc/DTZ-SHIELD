package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface QuarantineDao {
    @Query("SELECT * FROM quarantine_items ORDER BY detectionDate DESC")
    fun getAllQuarantined(): Flow<List<QuarantineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: QuarantineEntity): Long

    @Update
    suspend fun update(item: QuarantineEntity)

    @Delete
    suspend fun delete(item: QuarantineEntity)

    @Query("DELETE FROM quarantine_items WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM quarantine_items")
    suspend fun clearAll()
}
