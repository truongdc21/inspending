package com.truongdc21.inspending.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.truongdc21.inspending.model.History

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addHistory (history: History)

    @Update
    suspend fun updateTransaction (history: History)

    @Delete
    suspend fun deleteTransaction ( history: History)

    @Query("SELECT * FROM history_table ORDER BY Id ASC")
    fun getListHistory () : LiveData<List<History>>
}