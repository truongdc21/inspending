package com.truongdc21.inspending.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.truongdc21.inspending.viewmodel.history.TransactionViewmodel
import java.lang.IllegalArgumentException
import java.util.Calendar.getInstance

@Suppress("UNCHECKED_CAST")
class ViewModelFactory (val application: Application): ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(application) as T
        }else{
            throw IllegalArgumentException("unknow viewmodel class $modelClass")
        }
    }
}