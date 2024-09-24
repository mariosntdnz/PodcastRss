package com.example.podcastrss.repository

import com.example.podcastrss.dao.HistoryDao
import com.example.podcastrss.models.entities.HistoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SearchHistoryRepository {
    fun insertSearch(search: String)
    fun deleteSearch(search: String)
    fun getHistorySearch(search: String): Flow<List<String>>
}

class SearchHistoryRepositoryImpl(
    private val historyDao: HistoryDao
): SearchHistoryRepository {
    override fun insertSearch(search: String) {
        historyDao.insertHistory(HistoryEntity(search))
    }

    override fun deleteSearch(search: String) {
        historyDao.deleteHistory(HistoryEntity(search))
    }

    override fun getHistorySearch(search: String): Flow<List<String>> {
        return historyDao.getHistory(search).map {
            it.map {
                it.historyValue
            }
        }
    }

}