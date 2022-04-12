package com.truongdc21.inspending.fragment.accounts.viewpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.truongdc21.inspending.adapter.AdapterAccounts
import com.truongdc21.inspending.databinding.FragmentAccountsVPGBinding
import com.truongdc21.inspending.util.NumberTextWatcherForThousand
import com.truongdc21.inspending.viewmodel.accounts.AccountsViewmodel


class AccountsVPGFragment : Fragment() {

    private lateinit var binding : FragmentAccountsVPGBinding
    private lateinit var mViewModelAccounts : AccountsViewmodel
    private  var adapter : AdapterAccounts? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountsVPGBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mViewModelAccounts = ViewModelProvider(this).get(AccountsViewmodel::class.java)
        super.onViewCreated(view, savedInstanceState)
        binding.rvAcounts.layoutManager = LinearLayoutManager(this.context)

        mViewModelAccounts.getListAccounts.observe(viewLifecycleOwner, Observer { accounts ->
            binding.rvAcounts.adapter = AdapterAccounts( this.requireContext(),accounts)

            var monney: Long = 0
            for (i in accounts) {
                monney = monney + i.monney
            }
            binding.tvMonneyAllAccounts.text = "${NumberTextWatcherForThousand.NumbertoThousand(monney)}"
        })



    }

}