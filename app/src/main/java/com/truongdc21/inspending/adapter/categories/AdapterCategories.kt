package com.truongdc21.inspending.adapter.categories

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.truongdc21.inspending.R
import com.truongdc21.inspending.interfa.DataCategoriesInterface
import com.truongdc21.inspending.model.Categories
import com.truongdc21.inspending.model.DateTime
import com.truongdc21.inspending.model.History
import com.truongdc21.inspending.util.Date
import com.truongdc21.inspending.util.KeyWord
import com.truongdc21.inspending.util.NumberTextWatcherForThousand
import com.truongdc21.inspending.viewmodel.MainViewModel
import com.truongdc21.inspending.viewmodel.categories.CategoriesViewmodel
import com.truongdc21.inspending.viewmodel.statisicals.StatisticalViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate

class AdapterCategories
    (
        val StatisticalsVMD : StatisticalViewmodel,
        val mList : List<Categories> ,
        val mListFilterType : List<Categories>,
        val accountsId : Int? = null,
        val mTransaction : List<History>,
        val mListDateTime : List<DateTime>,
        val MainVMD : MainViewModel,
        val DateLive : LocalDate,
    ): RecyclerView.Adapter<AdapterCategories.CategoriesViewHolder>() {

    inner class CategoriesViewHolder(ctview : View):RecyclerView.ViewHolder(ctview){
        val imgCategories = ctview.findViewById<ImageView>(R.id.imgCategories)
        val tvMonneyCategories = ctview.findViewById<TextView>(R.id.tvMonneyCategories)
        val tvNameCategories = ctview.findViewById<TextView>(R.id.tvCategories)
        //val tvPrecentCategories = ctview.findViewById<TextView>(R.id.tvPrecentCategories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_categories , parent ,false)
        return CategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val itemCategories = mListFilterType.get(position)

        holder.imgCategories.setBackgroundResource(itemCategories.imgCategories)
        holder.tvNameCategories.text = itemCategories.NameCategories

        CoroutineScope(Dispatchers.Default).launch {
            val mListTransactionDate = StatisticalsVMD.getListDate(mTransaction , mListDateTime, MainVMD )
            val mListTransactionAccounts = StatisticalsVMD.getListAccounts(mListTransactionDate , accountsId!!)
            val ListFilterAccountsformListDate = StatisticalsVMD.getListMonneyCategoriesInListTransaction(mListTransactionAccounts , itemCategories.Id)
            val monneyCate = StatisticalsVMD.getMonneyfromListTransactionForItemCategories(ListFilterAccountsformListDate)

            if (itemCategories.typeCategories == KeyWord.Income && monneyCate > 0 ){
                withContext(Dispatchers.Main) {
                    holder.tvMonneyCategories.setTextColor(Color.parseColor("#9ACD32"))
                }
            }else if (itemCategories.typeCategories == KeyWord.Expenses && monneyCate > 0){
                withContext(Dispatchers.Main){
                    holder.tvMonneyCategories.setTextColor(Color.parseColor("#FA8072"))
                }
            }
            withContext(Dispatchers.Main){
                holder.tvMonneyCategories.text = NumberTextWatcherForThousand.NumbertoThousand(monneyCate)
            }

        }





    }

    override fun getItemCount(): Int {
        return mListFilterType.size
    }



}