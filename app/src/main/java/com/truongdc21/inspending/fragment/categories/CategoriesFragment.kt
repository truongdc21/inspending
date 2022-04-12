package com.truongdc21.inspending.fragment.categories

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.truongdc21.inspending.adapter.categories.AdapterAllCategories
import com.truongdc21.inspending.databinding.FragmentCategoriesBinding
import com.truongdc21.inspending.model.Categories
import com.truongdc21.inspending.util.Date
import com.truongdc21.inspending.util.KeyWord
import com.truongdc21.inspending.util.NumberTextWatcherForThousand
import com.truongdc21.inspending.viewmodel.MainViewModel
import com.truongdc21.inspending.viewmodel.categories.CategoriesViewmodel
import com.truongdc21.inspending.viewmodel.history.TransactionViewmodel
import com.truongdc21.inspending.viewmodel.statisicals.StatisticalViewmodel
import kotlinx.coroutines.*

class CategoriesFragment() : Fragment(){

    private var _binding : FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var MainVMD : MainViewModel
    private lateinit var CategoriesVMD : CategoriesViewmodel
    private lateinit var TransactionVMD : TransactionViewmodel
    private lateinit var StatisticalVMD : StatisticalViewmodel

    private lateinit var dataStore : DataStore<Preferences>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCategoriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpDataStoreANDViewmodel()





        val mListAllCategoriesIncome = CategoriesVMD.ListAllCategoriesIncome()
        val mListAllCategoriesExpenses = CategoriesVMD.ListAllCategoriesExpenses()


        // Setup Progressbar true or false
        binding.apply {
            if (rdExpenses.isChecked){
                getDataRvFromAdapter(KeyWord.Expenses , mListAllCategoriesExpenses)
            }
            rdBalance.setOnCheckedChangeListener { _, isChecked ->
                if (rdExpenses.isChecked){
                    //setUpCircularProgressbar()
                    getDataRvFromAdapter(KeyWord.Expenses , mListAllCategoriesExpenses)

                }else if (rdIncome.isChecked){
                    //setUpCircularProgressbar2()
                    getDataRvFromAdapter(KeyWord.Income , mListAllCategoriesIncome)
                }
            }
        }
    }

    private fun getDataRvFromAdapter (Key : String , mListAllCategories : ArrayList<String>){
        binding.apply {
            rvIncomeMonth.layoutManager = LinearLayoutManager(context)
            MainVMD.getDateTime.observe(viewLifecycleOwner, Observer { mListDateTime ->
                MainVMD.CheckDateLiveData.observe(viewLifecycleOwner, Observer { DateLive ->
                    MainVMD.CheckIdAccounts.observe(viewLifecycleOwner, Observer { accountsId ->
                        TransactionVMD.getListHistory.observe(viewLifecycleOwner, Observer { mListTransaction ->
                            CategoriesVMD.getListCategories.observe(viewLifecycleOwner, Observer { mListCategories ->

                            lifecycleScope.launch(Dispatchers.Default){

                                val ListFilterTypeDate = StatisticalVMD.getListDate(mListTransaction,mListDateTime ,MainVMD )
                                val ListFilterDatefromList = StatisticalVMD.getListAccounts(ListFilterTypeDate , accountsId )
                                val monneyExpenses = StatisticalVMD.getMonneyExpenses(ListFilterDatefromList , mListCategories)
                                val monneyIncome = StatisticalVMD.getMonneyIncome(ListFilterDatefromList , mListCategories)
                                val monneyAllBalance = StatisticalVMD.getMonneyIncomeANDExpenses(monneyIncome , monneyExpenses)
                                val perCentIncome = CategoriesVMD.getPercentInCome(monneyIncome , monneyExpenses)
                                val perCentExpenses = CategoriesVMD.getPercentExpenses(monneyIncome , monneyExpenses)
                                val mListCategoriesOnfix = CategoriesVMD.ListCategoriesOnfix(mListCategories , Key)

                                withContext(Dispatchers.Main){
                                    // Setup Propessbar
                                    if (Key == KeyWord.Income){
                                        setUpCircularProgressbarIncome(perCentIncome)
                                    }else{
                                        setUpCircularProgressbarExpenses(perCentExpenses)
                                    }
                                    binding.tvMonneyExpenses.text = NumberTextWatcherForThousand.NumbertoThousand(monneyExpenses)
                                    binding.tvMonneyIncome.text = NumberTextWatcherForThousand.NumbertoThousand(monneyIncome)
                                    binding.tvMonneyBalance.text = NumberTextWatcherForThousand.NumbertoThousand(monneyAllBalance)

                                    rvIncomeMonth.adapter = AdapterAllCategories(
                                        requireActivity(),
                                        CategoriesVMD,
                                        mListAllCategories,
                                        mListCategories,
                                        accountsId,
                                        mListTransaction,
                                        mListDateTime,
                                        MainVMD,
                                        DateLive,
                                        mListCategoriesOnfix,
                                        StatisticalVMD


                                    )

                                }
                            }
                            })
                        })
                    })
                })
            })

        }
    }
    private fun setUpCircularProgressbarIncome (f1 : Float){
        binding.apply {
            binding.tvTypeCategories.text = "Thu nhập >>"
            binding.tvPrecentCost.text = "${f1.toInt()} %"
            binding.progressCircularCost.apply {
                backgroundProgressBarColor = Color.parseColor("#f4f5f9")
                progressBarColor = Color.parseColor("#9ACD32")
                setProgressWithAnimation(f1, 1000)
                startAngle = 180f
                progressDirection = CircularProgressBar.ProgressDirection.TO_LEFT
            }
        }
    }
    private fun setUpCircularProgressbarExpenses (f1: Float){
        binding.apply {
            binding.tvTypeCategories.text = "Chi phí >>"
            tvPrecentCost.text = "${f1.toInt()} %"
            binding.progressCircularCost.apply {
                backgroundProgressBarColor = Color.parseColor("#f4f5f9")
                progressBarColor = Color.parseColor("#FA8072")
                setProgressWithAnimation(f1, 1000)
                startAngle = 180f
                progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
            }
        }
    }

    private fun setUpDataStoreANDViewmodel (){
        dataStore = requireContext().createDataStore("FirstInstalledApp")
        CategoriesVMD = ViewModelProvider(this).get(CategoriesViewmodel::class.java)
        TransactionVMD = ViewModelProvider(this).get(TransactionViewmodel::class.java)
        MainVMD = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        StatisticalVMD = ViewModelProvider(this).get(StatisticalViewmodel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}