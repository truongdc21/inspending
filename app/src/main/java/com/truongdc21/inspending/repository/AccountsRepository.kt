package com.truongdc21.inspending.repository

import androidx.lifecycle.LiveData
import com.truongdc21.inspending.database.AccountsDao
import com.truongdc21.inspending.model.Accounts

class AccountsRepository(private val accountsDao: AccountsDao ){

    val getListAcounts : LiveData<List<Accounts>> = accountsDao.getListAccounts()

    suspend fun addAccounts (accounts: Accounts){
        accountsDao.addAccounts(accounts)
    }

    suspend fun updateAccounts (accounts: Accounts){
        accountsDao.updateAccounts(accounts)
    }

}