package com.example.podcastrss.application

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.podcastrss.dao.HistoryDao
import com.example.podcastrss.models.entities.HistoryEntity

@Database(version = 1, entities = [HistoryEntity::class])
abstract class DataBase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}