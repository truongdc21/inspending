package com.truongdc21.inspending.viewmodel.accounts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.truongdc21.inspending.R
import com.truongdc21.inspending.model.Icon

class AddAccountsViewmodel(application: Application) : AndroidViewModel(application) {

    var mListIcon = mutableListOf<Int>(
        R.drawable.ic_accounts_1,
        R.drawable.ic_accounts_2,
        R.drawable.ic_accounts_3,
        R.drawable.ic_accounts_4,
        R.drawable.ic_accounts_5,
        R.drawable.ic_accounts_6,
        R.drawable.ic_accounts_7,
        R.drawable.ic_accounts_8,
        R.drawable.ic_accounts_9,
        R.drawable.ic_accounts_10,
        R.drawable.ic_accounts_11,
        R.drawable.ic_accounts_12,
        R.drawable.ic_accounts_13 )


    var ImgAccounts = R.drawable.ic_accounts_6
    val liveDataImgAccounts = MutableLiveData<Int>()

    var TypeAccountss = 0
    val liveDataTypeAccounts = MutableLiveData<Int>()

    var Currency = "VND"
    val liveDataCurrency = MutableLiveData<String>()

    var Destription = ""
    val liveDataDestription = MutableLiveData<String>()



    init {
        liveDataImgAccounts.value = ImgAccounts
        liveDataCurrency.value = Currency
        liveDataTypeAccounts.value = TypeAccountss
        liveDataDestription.value = Destription
    }

    // -- Accounts Icon
    val currenIcon: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }



}