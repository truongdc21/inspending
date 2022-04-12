package com.truongdc21.inspending.adapter.categories

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.truongdc21.inspending.R
import com.truongdc21.inspending.interfa.DataCategoriesInterface
import com.truongdc21.inspending.model.Categories
import com.truongdc21.inspending.model.DateTime
import com.truongdc21.inspending.model.History
import com.truongdc21.inspending.util.KeyWord
import com.truongdc21.inspending.viewmodel.MainViewModel
import com.truongdc21.inspending.viewmodel.categories.CategoriesViewmodel
import com.truongdc21.inspending.viewmodel.statisicals.StatisticalViewmodel
import java.time.LocalDate
import kotlin.collections.ArrayList

class AdapterAllCategories (
    val context: Context,
    val CategoriesVMD : CategoriesViewmodel,
    val mListAllCategories : ArrayList<String>,
    val mListCategories : List<Categories>,
    val accountsID : Int,
    val mListTransaction : List<History>,
    val mListDateTime : List<DateTime>,
    val MainVMD : MainViewModel,
    val Datelive : LocalDate,
    val mListCategoriesOnFix : ArrayList<Categories>,
    val StatisticalVMD : StatisticalViewmodel

    ) : RecyclerView.Adapter<AdapterAllCategories.AllCategoriesViewHolder>() {

    inner class AllCategoriesViewHolder ( ctView : View) : RecyclerView.ViewHolder(ctView){
        val tvNameAlltypeCategories : TextView = ctView.findViewById(R.id.tvNameAllTypeCategories)
        val rvAllCategories : RecyclerView = ctView.findViewById(R.id.rvAllCategories)
        val itemViewBolderLeft : TextView = ctView.findViewById(R.id.itemViewBolderLeft)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_all_categories , parent , false)
        return AllCategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllCategoriesViewHolder, position: Int) {
        val item = mListAllCategories[position]
        holder.rvAllCategories.layoutManager = GridLayoutManager(context,4)

        val listCategories = ArrayList<Categories>()
        for (i in mListCategoriesOnFix){
            if (item == i.typeCategoriesChild){
                listCategories.add(i)
            }
        }

        when(item){
            // Expenses
            KeyWord.canthiet -> {
                holder.tvNameAlltypeCategories.text = "Cần Thiết :"
                holder.itemViewBolderLeft.setBackgroundColor(Color.parseColor("#FA8072"))
            }
            KeyWord.hangthang -> {
                holder.tvNameAlltypeCategories.text = "Hàng Tháng"
                holder.itemViewBolderLeft.setBackgroundColor(Color.parseColor("#FA8072"))
            }
            KeyWord.no_chi -> {
                holder.tvNameAlltypeCategories.text = "Vay Nợ"
                holder.itemViewBolderLeft.setBackgroundColor(Color.parseColor("#FA8072"))
            }

            KeyWord.chiKhac -> {
                holder.tvNameAlltypeCategories.text = "Khác"
                holder.itemViewBolderLeft.setBackgroundColor(Color.parseColor("#FA8072"))
            }

            // Income
            KeyWord.thuNhapCoBan -> {
                holder.tvNameAlltypeCategories.text = "Thu nhập cơ bản"
                holder.itemViewBolderLeft.setBackgroundColor(Color.parseColor("#9ACD32"))
            }

            KeyWord.no_thu -> {
                holder.tvNameAlltypeCategories.text = "Vay Nợ"
                holder.itemViewBolderLeft.setBackgroundColor(Color.parseColor("#9ACD32"))
            }
            KeyWord.thuKhac -> {
                holder.tvNameAlltypeCategories.text = "Khác"
                holder.itemViewBolderLeft.setBackgroundColor(Color.parseColor("#9ACD32"))
            }
        }

        holder.rvAllCategories.adapter =
            AdapterCategories(
                StatisticalVMD,
                mListCategories,
                listCategories,
                accountsID,
                mListTransaction,
                mListDateTime,
                MainVMD,
                Datelive,
            )

    }

    override fun getItemCount(): Int {
       return mListAllCategories.size
    }

}