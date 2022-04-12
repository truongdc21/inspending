package com.truongdc21.inspending.fragment.statisticals

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.truongdc21.inspending.R
import com.truongdc21.inspending.adapter.statistical.AdapterChartSatisticals
import com.truongdc21.inspending.databinding.FragmentStatisticalsBinding
import com.truongdc21.inspending.model.Chart
import com.truongdc21.inspending.model.History
import com.truongdc21.inspending.util.Date
import com.truongdc21.inspending.util.KeyWord
import com.truongdc21.inspending.util.NumberTextWatcherForThousand
import com.truongdc21.inspending.viewmodel.MainViewModel
import com.truongdc21.inspending.viewmodel.ViewModelFactory
import com.truongdc21.inspending.viewmodel.categories.CategoriesViewmodel
import com.truongdc21.inspending.viewmodel.history.TransactionViewmodel
import com.truongdc21.inspending.viewmodel.statisicals.StatisticalViewmodel
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.stream.Collectors
import java.util.stream.IntStream
import kotlin.collections.ArrayList


class StatisticalsFragment : Fragment() {
    private lateinit var binding : FragmentStatisticalsBinding
    private lateinit var MainVMD : MainViewModel
    private lateinit var CategoriesVMD : CategoriesViewmodel
    private lateinit var TransactionVMD : TransactionViewmodel
    private lateinit var StatisticalVMD : StatisticalViewmodel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticalsBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup viewmodel for fragment
        setUpViewmodel()

        binding.rvSatisticals.layoutManager = LinearLayoutManager(this.context , LinearLayoutManager.HORIZONTAL , false)
        MainVMD.getDateTime.observe(viewLifecycleOwner , androidx.lifecycle.Observer { mDateTime ->
            MainVMD.CheckDateLiveData.observe(viewLifecycleOwner , androidx.lifecycle.Observer { mDate ->
                MainVMD.CheckIdAccounts.observe(viewLifecycleOwner , androidx.lifecycle.Observer { accountsId ->
                    TransactionVMD.getListHistory.observe(viewLifecycleOwner , androidx.lifecycle.Observer { mTransaction ->
                        CategoriesVMD.getListCategories.observe(viewLifecycleOwner, androidx.lifecycle.Observer { mCategories ->

                            val listTransactionDate = StatisticalVMD.getListDate(mTransaction, mDateTime , MainVMD)
                            val listTransactionAccounts = StatisticalVMD.getListAccounts(listTransactionDate , accountsId)

                            // Add Id Category to mListChart
                            val mListChartIdCategory = StatisticalVMD.addIdtomListChart(listTransactionAccounts)
                            // Add monney to ListChart
                            val mListChartMonney = StatisticalVMD.addMonneyToListChart(mListChartIdCategory , listTransactionAccounts)
                            // Max from List Char
                            val MaxinListChart = StatisticalVMD.FillterMaxInListChart(mListChartMonney)

                            binding.rvSatisticals.adapter = AdapterChartSatisticals(requireContext(), mListChartMonney, MaxinListChart, mCategories)

                            val Max = MaxinListChart / 100
                            val MaxOne = Max * 100
                            val MaxTwo = Max * 80
                            val MaxThree = Max * 60
                            val MaxFour = Max * 40
                            val MaxFive = Max * 20

                            SetTvMonneyChart(MaxOne.toLong(), MaxTwo.toLong(), MaxThree.toLong(), MaxFour.toLong(), MaxFive.toLong(), 0L)

                            lifecycleScope.launch(Dispatchers.Default){
                                val monneyExpenses = StatisticalVMD.getMonneyExpenses(listTransactionAccounts , mCategories)
                                val monneyIncome = StatisticalVMD.getMonneyIncome(listTransactionAccounts , mCategories)
                                val monneyIncomeANDExpenses = StatisticalVMD.getMonneyIncomeANDExpenses(monneyIncome , monneyExpenses)
                                val moneyDIVAY = StatisticalVMD.getMonneyDIVAY(listTransactionAccounts , mCategories)
                                val monneyCHOVAY = StatisticalVMD.getMonneyCHOVAY(listTransactionAccounts , mCategories)
                                val monneyBalanceVAYNO = StatisticalVMD.getMonneyBALANCEVAYNO(monneyCHOVAY , moneyDIVAY)

                                withContext(Dispatchers.Main){
                                    binding.apply {
                                        tvMonneyIncome.text = "+ ${NumberTextWatcherForThousand.NumbertoThousand(monneyIncome)}"
                                        tvMonneyExpenses.text = "- ${NumberTextWatcherForThousand.NumbertoThousand(monneyExpenses)}"
                                        tvMonneyIncomeANDExpenses.text = NumberTextWatcherForThousand.NumbertoThousand(monneyIncomeANDExpenses)
                                        tvMonneyDIVAY.text = "+ ${NumberTextWatcherForThousand.NumbertoThousand(moneyDIVAY)}"
                                        tvMonneyCHOVAY.text = "- ${NumberTextWatcherForThousand.NumbertoThousand(monneyCHOVAY)}"
                                        tvMonneyBalanceVAYNO.text = NumberTextWatcherForThousand.NumbertoThousand(monneyBalanceVAYNO)
                                    }
                                }
                            }

                        })
                    })
                })
            })
        })

    }

    private fun SetTvMonneyChart( One: Long, Two: Long, Three: Long, Four: Long, Five: Long , Six : Long ){
        CoroutineScope(Dispatchers.Main).launch {
            binding.apply {
                tvChartMonney1.text = "${NumberTextWatcherForThousand.NumbertoThousand(One)}"
                tvChartMonney2.text = "${NumberTextWatcherForThousand.NumbertoThousand(Two)}"
                tvChartMonney3.text = "${NumberTextWatcherForThousand.NumbertoThousand(Three)}"
                tvChartMonney4.text = "${ NumberTextWatcherForThousand.NumbertoThousand(Four)}"
                tvChartMonney5.text = "${NumberTextWatcherForThousand.NumbertoThousand(Five)}"
                tvChartMonney6.text = "${NumberTextWatcherForThousand.NumbertoThousand(Six)}"

                tvMaxChart.text = "Xếp theo mức danh mục cao nhất: ${NumberTextWatcherForThousand.NumbertoThousand(One)}"
            }
        }
    }

    private fun setUpViewmodel (){
        MainVMD = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        TransactionVMD = ViewModelProvider(this).get(TransactionViewmodel::class.java)
        CategoriesVMD = ViewModelProvider(this).get(CategoriesViewmodel::class.java)
        StatisticalVMD = ViewModelProvider(this).get(StatisticalViewmodel::class.java)
    }



}
