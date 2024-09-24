package com.example.podcastrss.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.podcastrss.models.entities.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history where historyValue like '%' || :search || '%'") fun getHistory(search: String): Flow<List<HistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertHistory(history: HistoryEntity)

    @Delete fun deleteHistory(history: HistoryEntity)

}