package com.truongdc21.inspending.interfa

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.truongdc21.inspending.adapter.AdapterSelectIcon
import com.truongdc21.inspending.model.Accounts
import com.truongdc21.inspending.model.Icon

interface OnClickICon {
    fun Iclick (position: Int , linkIcon : Int)

    fun setAdapter(rv: RecyclerView, context: Context, list: MutableList<Int>){
        rv.layoutManager = GridLayoutManager(context , 4)
        rv.adapter = AdapterSelectIcon(list , this)
    }
    fun IclickSelectAccounts (accounts: Accounts)


}