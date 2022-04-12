package com.truongdc21.inspending.adapter.categories

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.truongdc21.inspending.R
import com.truongdc21.inspending.model.Categories
import com.truongdc21.inspending.util.KeyWord


class AdapterViewPagerSelectCategories(
    val context: Context,
    val mListTypeCategories: ArrayList<String>,
    val mListCategoriesOnFix: ArrayList<Categories>,
    val IdCategories : ((Categories) -> Unit)
) :RecyclerView.Adapter<AdapterViewPagerSelectCategories.VPGSelectAccountsViewHolder>() {
    inner class VPGSelectAccountsViewHolder(val ctView: View) : RecyclerView.ViewHolder(ctView){
        val tvNameAlltypeCategories : TextView = ctView.findViewById(R.id.tvNameAllTypeCategories)
        val rvAllCategories : RecyclerView = ctView.findViewById(R.id.rvAllCategories)
        val itemViewBolderLeft : TextView = ctView.findViewById(R.id.itemViewBolderLeft)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VPGSelectAccountsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_all_categories, parent, false)
        return VPGSelectAccountsViewHolder(view)
    }

    override fun onBindViewHolder(holder: VPGSelectAccountsViewHolder, position: Int) {
        val itemList = mListTypeCategories.get(position)

        holder.rvAllCategories.layoutManager = GridLayoutManager(context , 4)

        val listCategories = ArrayList<Categories>()
        for (i in mListCategoriesOnFix){
            if (itemList == i.typeCategoriesChild){
                listCategories.add(i)
            }
        }

        when(itemList){
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


        // Get ID when clicl Item Categories
        holder.rvAllCategories.adapter = AdapterCategoriesSelectDialog(listCategories , IdCategories = { categories ->
            IdCategories.invoke(categories)
        })


    }

    override fun getItemCount(): Int {
       return mListTypeCategories.size
    }
}