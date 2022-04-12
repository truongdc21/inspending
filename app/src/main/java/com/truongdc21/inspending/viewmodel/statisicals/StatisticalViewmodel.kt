package com.truongdc21.inspending.viewmodel.statisicals

import androidx.lifecycle.ViewModel
import com.truongdc21.inspending.model.Categories
import com.truongdc21.inspending.model.Chart
import com.truongdc21.inspending.model.DateTime
import com.truongdc21.inspending.model.History
import com.truongdc21.inspending.util.Date
import com.truongdc21.inspending.util.Date.mDate
import com.truongdc21.inspending.util.KeyWord
import com.truongdc21.inspending.viewmodel.MainViewModel
import java.time.DayOfWeek
import java.time.LocalDate

class StatisticalViewmodel : ViewModel() {


    // Function get list History fillter Date
    fun getListDate (mTransaction : List<History>,
                     mDateTime : List<DateTime>,
                     MainVMD : MainViewModel ): ArrayList<History>{

        val mListTransactionDate = ArrayList<History>()

        var DateOne : String = ""
        var DateTwo : String = ""

        for (iTrans in mTransaction){
            for (iTypeDate in mDateTime){
                when(iTypeDate.TypeDateTime){
                    1, 5 -> {
                        DateOne = MainVMD.formatterDateToolbar(LocalDate.parse(iTrans.Date))
                        DateTwo = MainVMD.formatterDateToolbar(mDate)
                        CheckandAddDate(DateOne, DateTwo, iTrans, mListTransactionDate)
                    }
                    2 -> {
                        var monday = mDate.with(DayOfWeek.MONDAY)
                        val sunday = mDate.with(DayOfWeek.SUNDAY)
                        while (monday <= sunday) {
                            if (iTrans.Date == monday.toString()) {
                                mListTransactionDate.add(iTrans)
                            }
                            monday = monday.plusDays(1)
                        }
                    }
                    3 -> {
                        DateOne = MainVMD.formatterDateMonths(LocalDate.parse(iTrans.Date))
                        DateTwo = MainVMD.formatterDateMonths(mDate)
                        CheckandAddDate(DateOne, DateTwo, iTrans, mListTransactionDate)
                    }
                    4 -> {
                        DateOne = MainVMD.formatterDateYears(LocalDate.parse(iTrans.Date))
                        DateTwo = MainVMD.formatterDateYears(mDate)
                        CheckandAddDate(DateOne, DateTwo, iTrans, mListTransactionDate)
                    }

                    6 -> {
                        DateOne = iTrans.Date
                        var firstDate = Date.RangeDateFirst
                        val endDate = Date.RangeDateEnd
                        while (firstDate <= endDate) {
                            CheckandAddDate(
                                DateOne,
                                firstDate.toString(),
                                iTrans,
                                mListTransactionDate
                            )
                            firstDate = firstDate.plusDays(1)
                        }
                    }

                    7 -> {
                        mListTransactionDate.add(iTrans)
                    }
                }
            }

        }
        return mListTransactionDate
    }


    // Function get List History fillter Accounts
    fun getListAccounts (mListTransactionDate : ArrayList<History>,
                         accountsId : Int ): ArrayList<History> {
        var mListTransactionAccounts  =  ArrayList<History>()
            for (i in mListTransactionDate){
                if (i.Accounts == accountsId){
                    mListTransactionAccounts.add(i)
                }else if (accountsId == 0){
                    mListTransactionAccounts = mListTransactionDate
                }
            }
        return mListTransactionAccounts
    }
    private fun CheckandAddDate(DateOne: String, DateTwo: String, history: History, mListTransaction: ArrayList<History>){
        if (DateOne == DateTwo){
            mListTransaction.add(history)
        }
    }

    fun addIdtomListChart ( listTransactionAccounts : ArrayList<History>) : MutableList<Chart>{

        val mListChart = mutableListOf<Chart>()
        var Check = false
        for (i in listTransactionAccounts){

            Check = false
            if(mListChart.isEmpty()){
                mListChart.add(Chart(i.Categories,0L))
            }
            for (iChart in mListChart){
                if (i.Categories == iChart.IdCategory){
                    Check = true
                    break
                }
            }

            if (!Check){
                mListChart.add(Chart(i.Categories, 0L))
            }

        }
        return mListChart
    }

    fun addMonneyToListChart ( mListChart : MutableList<Chart> , listTransactionAccounts : ArrayList<History>): MutableList<Chart>{
        for (i in mListChart){
            val index = mListChart.indexOf(i)
            var monney = 0L
            for (acc in listTransactionAccounts){
                if (i.IdCategory == acc.Categories){
                    monney = monney + acc.monney
                }
            }
            mListChart.set(index , Chart(i.IdCategory ,monney ))
        }
        return mListChart
    }

    fun FillterMaxInListChart (mListChart : MutableList<Chart>): Int{
        var MaxMlistChart = 0
        for (i in mListChart){
            if (i.monneyCategory >= MaxMlistChart ){
                MaxMlistChart = i.monneyCategory.toInt()
            }
        }
        return MaxMlistChart
    }


    suspend fun getMonneyIncome (listTransactionAccounts : ArrayList<History> , mCategories : List<Categories>): Long{
        var monneyIncome : Long = 0
        for (i in listTransactionAccounts){
            for (income in mCategories){
                if ( income.Id == i.Categories && income.typeCategories == KeyWord.Income){
                    monneyIncome = monneyIncome + i.monney
                }
            }
        }
        return monneyIncome
    }
    suspend fun getMonneyExpenses (listTransactionAccounts : ArrayList<History> , mCategories : List<Categories>): Long{
        var monneyExpenses : Long = 0
        for (i in listTransactionAccounts){
            for (income in mCategories){
                if ( income.Id == i.Categories && income.typeCategories == KeyWord.Expenses){
                    monneyExpenses = monneyExpenses + i.monney
                }
            }
        }
        return monneyExpenses
    }
    suspend fun getMonneyIncomeANDExpenses (Income : Long , Expenses : Long): Long{
        var Monney : Long = 0
        Monney = Income + (- (Expenses))
        return Monney
    }

    suspend fun getMonneyDIVAY (listTransactionAccounts : ArrayList<History> , mCategories : List<Categories>): Long{
        var monneyDIVAY : Long = 0
        for (i in listTransactionAccounts){
            for (income in mCategories){
                if ( income.Id == i.Categories && income.typeCategoriesChild == KeyWord.no_thu){
                    monneyDIVAY = monneyDIVAY + i.monney
                }
            }
        }
        return monneyDIVAY
    }

    suspend fun getMonneyCHOVAY (listTransactionAccounts : ArrayList<History> , mCategories : List<Categories>): Long{
        var monneyCHOVAY: Long = 0
        for (i in listTransactionAccounts){
            for (income in mCategories){
                if ( income.Id == i.Categories && income.typeCategoriesChild == KeyWord.no_chi){
                    monneyCHOVAY = monneyCHOVAY + i.monney
                }
            }
        }
        return monneyCHOVAY
    }

    suspend fun getMonneyBALANCEVAYNO (BI_NO : Long , NO : Long): Long {
        var BalanceVAYNO : Long = 0
        if  (BI_NO == 0L){
            return -NO
        }else{
            BalanceVAYNO = BI_NO - NO
        }
        return BalanceVAYNO
    }

    suspend fun getListMonneyCategoriesInListTransaction (mListTransactionAccounts : ArrayList<History> , itemCategories : Int) : ArrayList<History>{
        val mListTransactionCategories = ArrayList<History>()
        for (i in mListTransactionAccounts){
            if (i.Categories ==  itemCategories){
                mListTransactionCategories.add(i)
            }
        }
        return mListTransactionCategories
    }
    suspend fun getMonneyfromListTransactionForItemCategories (mListTransactionCategories : ArrayList<History>) : Long{
        var money : Long = 0
        for (i in mListTransactionCategories){
            money = money + i.monney
        }
        return money
    }


}