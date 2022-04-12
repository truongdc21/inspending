package com.truongdc21.inspending.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.truongdc21.inspending.database.HistoryDao
import com.truongdc21.inspending.model.History
import com.truongdc21.inspending.util.Date


class HistoryRepository(private val historyDao: HistoryDao) {


    val getListHistory : LiveData<List<History>> = historyDao.getListHistory()


    suspend fun addHistory(history: History){
        historyDao.addHistory(history)
    }
}