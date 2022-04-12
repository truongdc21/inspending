package com.truongdc21.inspending.database

import androidx.lifecycle.LiveData
import androidx.room.*

import com.truongdc21.inspending.model.DateTime

@Dao
interface DateTimeDao {

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDateTime (dateTime: DateTime)

    @Update
    suspend fun updateDateTime (dateTime: DateTime)

    @Query("SELECT * FROM date_time_table ORDER BY Id ASC")
    fun getDateTime (): LiveData<List<DateTime>>
}