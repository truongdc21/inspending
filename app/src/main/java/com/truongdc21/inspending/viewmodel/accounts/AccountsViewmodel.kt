package com.truongdc21.inspending.viewmodel.accounts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.truongdc21.inspending.R
import com.truongdc21.inspending.database.MyDatabase
import com.truongdc21.inspending.model.Accounts
import com.truongdc21.inspending.repository.AccountsRepository
import com.truongdc21.inspending.util.NumberTextWatcherForThousand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountsViewmodel(application: Application) : AndroidViewModel(application) {

    val getListAccounts : LiveData<List<Accounts>>
    val repository : AccountsRepository

    init {
        val mAccounts = MyDatabase.getMyDatabase(application).mAccountsDao()
        repository  = AccountsRepository(mAccounts)
        getListAccounts = repository.getListAcounts
    }

    fun addAccounts (accounts: Accounts){
        viewModelScope.launch (Dispatchers.IO){
            repository.addAccounts(accounts)
        }
    }

    fun updateAccounts (accounts: Accounts){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateAccounts(accounts)
        }
    }

    fun addFirstAccounts (){
        val accountsOne = Accounts(0, R.drawable.ic_bottomnav_card,"Thẻ ATM" , 0L , 0 , 0,"VND" ,"")
        val accountsTwo = Accounts(0, R.drawable.ic_monney,"Tiền Mặt" , 0L , 0,0,"VND" ,"")
        addAccounts(accountsOne)
        addAccounts(accountsTwo)
    }

    fun MonneyAccounts (accounts : List<Accounts>):String{
        var monney : Long = 0
        for (i in accounts){
            monney = monney + i.monney
        }
        return "${NumberTextWatcherForThousand.NumbertoThousand(monney)}"
    }

}