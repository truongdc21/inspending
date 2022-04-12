package com.truongdc21.inspending.fragment.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.truongdc21.inspending.adapter.transaction.AdapterTransactionAllDate
import com.truongdc21.inspending.databinding.FragmentTransactionBinding
import com.truongdc21.inspending.model.History
import com.truongdc21.inspending.util.Date
import com.truongdc21.inspending.util.NumberTextWatcherForThousand
import com.truongdc21.inspending.viewmodel.MainViewModel
import com.truongdc21.inspending.viewmodel.accounts.AccountsViewmodel
import com.truongdc21.inspending.viewmodel.categories.CategoriesViewmodel
import com.truongdc21.inspending.viewmodel.history.TransactionViewmodel
import com.truongdc21.inspending.viewmodel.statisicals.StatisticalViewmodel
import kotlinx.coroutines.*
import java.time.DayOfWeek
import java.time.LocalDate


class TransactionFragment : Fragment() {

    private lateinit var binding: FragmentTransactionBinding

    private lateinit var TransactionVMD: TransactionViewmodel
    private lateinit var AccountsVMD: AccountsViewmodel
    private lateinit var CategoriesVMD: CategoriesViewmodel
    private lateinit var MainVMD: MainViewModel
    private lateinit var StatisticalVMD : StatisticalViewmodel

    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTransactionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModelAndDataStore()
        setViewTranSaction()
    }

    private fun setViewTranSaction() {

            binding.rvTransaction.layoutManager = LinearLayoutManager(context)

            MainVMD.getDateTime.observe(viewLifecycleOwner, Observer { mListTypeDate ->
            MainVMD.CheckIdAccounts.observe(viewLifecycleOwner, Observer { accountId ->
            MainVMD.CheckDateLiveData.observe(viewLifecycleOwner, Observer { date ->
            TransactionVMD.getListHistory.observe(viewLifecycleOwner, Observer { mListTransaction ->
            AccountsVMD.getListAccounts.observe(viewLifecycleOwner, Observer { mListAccounts ->
            CategoriesVMD.getListCategories.observe(viewLifecycleOwner, Observer { mListCategories ->



                lifecycleScope.launch(Dispatchers.Default){

                    val listTransactionDate = async { StatisticalVMD.getListDate(mListTransaction ,mListTypeDate , MainVMD) }
                    val listTransactionAccounts = async { StatisticalVMD.getListAccounts(listTransactionDate.await(), accountId) }
                    val listStringDate = async { TransactionVMD.getMlistStringDate(listTransactionAccounts.await(), mListAccounts) }
                    val monneyIncome = async { StatisticalVMD.getMonneyIncome(listTransactionAccounts.await() ,mListCategories)}
                    val monneyExpenses = async {StatisticalVMD.getMonneyExpenses(listTransactionAccounts.await() , mListCategories) }
                    val moneyBalance =  async { StatisticalVMD.getMonneyIncomeANDExpenses(monneyIncome.await() , monneyExpenses.await()) }

                    withContext(Dispatchers.Main){

                        val adapterAllTransaction =
                            AdapterTransactionAllDate(
                                requireContext(),
                                listStringDate.await(),
                                listTransactionAccounts.await(),
                                mListAccounts,
                                mListCategories,
                                StatisticalVMD
                            )

                        binding.apply {
                            rvTransaction.adapter = adapterAllTransaction
                            tvMonneyIncome.text = "+ ${NumberTextWatcherForThousand.NumbertoThousand(monneyIncome.await())}"
                            tvMonneyExpenses.text = "- ${NumberTextWatcherForThousand.NumbertoThousand(monneyExpenses.await())}"
                            tvMonneyBalance.text = NumberTextWatcherForThousand.NumbertoThousand(moneyBalance.await())

                        }
                    }
                }

            })
            })
            })
            })
            })
            })

    }

    private fun setUpViewModelAndDataStore() {
        dataStore = requireContext().createDataStore(name = "FirstInstalledApp")
        TransactionVMD = ViewModelProvider(this).get(TransactionViewmodel::class.java)
        AccountsVMD = ViewModelProvider(this).get(AccountsViewmodel::class.java)
        CategoriesVMD = ViewModelProvider(this).get(CategoriesViewmodel::class.java)
        MainVMD = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        StatisticalVMD = ViewModelProvider(this).get(StatisticalViewmodel::class.java)
    }


}