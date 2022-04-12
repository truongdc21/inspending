package com.truongdc21.inspending.viewmodel.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.truongdc21.inspending.database.MyDatabase
import com.truongdc21.inspending.model.Accounts
import com.truongdc21.inspending.model.History
import com.truongdc21.inspending.repository.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewmodel(application: Application) : AndroidViewModel(application){

    val getListHistory : LiveData<List<History>>
    private val repository : HistoryRepository


    init {
        val history = MyDatabase.getMyDatabase(application).mHistoryDao()
        repository = HistoryRepository(history)
        getListHistory = repository.getListHistory

    }

    fun addHistory(history: History){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addHistory(history)
        }
    }

    suspend fun getMlistStringDate (mListTransactionAccounts : ArrayList<History> , mListAccounts : List<Accounts>): MutableList<String>{

        val mListStringDate = mutableListOf<String>()
            var Check = false
            for (i in mListTransactionAccounts) {
                if (mListAccounts.isEmpty()) {
                    mListStringDate.add(i.Date)
                }
                for (iDate in mListStringDate) {
                    if (i.Date == iDate) {
                        Check = true
                        break
                    }
                }
                if (!Check) {
                    mListStringDate.add(i.Date)
                }
                Check = false
            }
            mListStringDate.sort()

        return mListStringDate
    }












}