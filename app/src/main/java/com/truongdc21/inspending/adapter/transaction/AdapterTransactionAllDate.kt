package com.truongdc21.inspending.adapter.transaction

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.truongdc21.inspending.R
import com.truongdc21.inspending.model.Accounts
import com.truongdc21.inspending.model.Categories
import com.truongdc21.inspending.model.History
import com.truongdc21.inspending.util.NumberTextWatcherForThousand
import com.truongdc21.inspending.viewmodel.statisicals.StatisticalViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AdapterTransactionAllDate (
    val context: Context,
    val mListStringDate : MutableList<String>,
    val mTransaction : List<History>,
    val mAccounts : List<Accounts>,
    val mCategories : List<Categories>,
    val StatisticalVMD : StatisticalViewmodel

) : RecyclerView.Adapter<AdapterTransactionAllDate.TransactionAllDateViewHolder>() {
    inner class TransactionAllDateViewHolder (ctView: View): RecyclerView.ViewHolder(ctView){
        val itemDayofMonth = ctView.findViewById<TextView>(R.id.itemTvDayofMonth)
        val itemDayofWeek = ctView.findViewById<TextView>(R.id.itemTvDayofWeek)
        val itemMonth_and_year = ctView.findViewById<TextView>(R.id.itemTvMonthandYear)
        val tvMonneyOfday = ctView.findViewById<TextView>(R.id.tvItemMonneyOfDay)
        val itemRvTransaction = ctView.findViewById<RecyclerView>(R.id.item_rvTransaction_all_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionAllDateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_transaction_all_date , parent, false)
        return TransactionAllDateViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionAllDateViewHolder, position: Int) {
        val itemTrasaction = mListStringDate[position]
        val linearLayout = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , true)

        CoroutineScope(Dispatchers.Default).launch {

            val DateParse = LocalDate.parse(itemTrasaction)
            val mListTransactionDate = ListTransactionDate(itemTrasaction)
            val monneyIncome = StatisticalVMD.getMonneyIncome(mListTransactionDate , mCategories)
            val monneyExpenses = StatisticalVMD.getMonneyExpenses(mListTransactionDate , mCategories)
            val monneyBalance = StatisticalVMD.getMonneyIncomeANDExpenses(monneyIncome , monneyExpenses)
            val datePaserToday = DateParse.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

            withContext(Dispatchers.Main){
                holder.itemDayofMonth.text = DateParse.dayOfMonth.toString()
                holder.itemDayofWeek.text = DateParse.format(DateTimeFormatter.ofPattern("EE"))
                holder.itemMonth_and_year.text = DateParse.format(DateTimeFormatter.ofPattern("yyyy.MM"))
                holder.itemRvTransaction.layoutManager = linearLayout
                holder.itemRvTransaction.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
                holder.itemRvTransaction.adapter = AdapterTransaction( context,mListTransactionDate , mAccounts , mCategories , datePaserToday)
                holder.tvMonneyOfday.text = NumberTextWatcherForThousand.NumbertoThousand(monneyBalance)
            }
        }

    }

    override fun getItemCount(): Int {
        return mListStringDate.size
    }

    suspend fun ListTransactionDate (itemTrasaction : String): ArrayList<History>{
        val mListTransactionDate = ArrayList<History>()
        for (i in mTransaction){
            if (itemTrasaction == i.Date){
                mListTransactionDate.add(i)
            }
        }
        return mListTransactionDate
    }
}