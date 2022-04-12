package com.truongdc21.inspending.fragment.accounts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.truongdc21.inspending.MainActivity
import com.truongdc21.inspending.R
import com.truongdc21.inspending.adapter.AdapterViewPageAccounts
import com.truongdc21.inspending.databinding.FragmentAccountsBinding
import org.w3c.dom.Text


class AccountsFragment : Fragment() {

    private lateinit var binding : FragmentAccountsBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPageAccounts.adapter = AdapterViewPageAccounts(childFragmentManager, lifecycle)

        val tab =  activity?.findViewById<TabLayout>(R.id.tabLayoutToolbar)

        TabLayoutMediator(tab!!, binding.viewPageAccounts, object : TabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    when (position) {
                        0 -> tab.text = "Tài Khoản"
                        1 -> tab.text = "Tiết Kiệm"
                        2 -> tab.text = "Vay Nợ"
                    }
                }
        }).attach()
    }

}