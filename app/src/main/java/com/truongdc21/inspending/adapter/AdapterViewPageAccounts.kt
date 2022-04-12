package com.truongdc21.inspending.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.truongdc21.inspending.fragment.accounts.viewpage.AccountsVPGFragment
import com.truongdc21.inspending.fragment.accounts.viewpage.DebtsVPGFragment
import com.truongdc21.inspending.fragment.accounts.viewpage.MyFinancesVPGFragment

class AdapterViewPageAccounts (fragment : FragmentManager , lifecycle: Lifecycle) : FragmentStateAdapter ( fragment , lifecycle)
{
    override fun getItemCount(): Int {
       return 3
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return AccountsVPGFragment()
            1 -> return DebtsVPGFragment()
        }
        return MyFinancesVPGFragment()  // 2
    }

}