package com.truongdc21.inspending.repository

import androidx.lifecycle.LiveData
import com.truongdc21.inspending.database.DateTimeDao
import com.truongdc21.inspending.model.DateTime

class DateTimeRepository (private val dateTimeDao: DateTimeDao) {

    val getDatetime  : LiveData<List<DateTime>> = dateTimeDao.getDateTime()

    suspend fun addDateTime (dateTime: DateTime){
        dateTimeDao.addDateTime(dateTime)
    }

    suspend fun updateDateTime (dateTime: DateTime){
        dateTimeDao.updateDateTime(dateTime)
    }
}