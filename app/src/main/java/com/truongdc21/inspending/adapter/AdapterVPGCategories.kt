package com.truongdc21.inspending.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.truongdc21.inspending.fragment.categories.CategoriesFragment
import com.truongdc21.inspending.fragment.statisticals.StatisticalsFragment
import com.truongdc21.inspending.fragment.transaction.TransactionFragment

class AdapterVPGCategories (fragment : FragmentManager , lifecycle: Lifecycle) : FragmentStateAdapter(fragment , lifecycle) {
    override fun getItemCount(): Int {
        return 10000
    }

    override fun createFragment(position: Int): Fragment {
        return CategoriesFragment()
    }
}