package com.truongdc21.inspending.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.truongdc21.inspending.model.Accounts

@Dao
interface AccountsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAccounts (accounts: Accounts)

    @Update
    suspend fun updateAccounts(accounts: Accounts)

    @Delete
    suspend fun deleteAccounts(accounts: Accounts)

    @Query("SELECT * FROM accounts_table ORDER BY Id ASC")
    fun getListAccounts () : LiveData<List<Accounts>>



}