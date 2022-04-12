package com.truongdc21.inspending.adapter.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.truongdc21.inspending.R
import com.truongdc21.inspending.model.Categories
import java.time.LocalDate

class AdapterCategoriesSelectDialog(
    val mListFilterType: List<Categories>,
    val IdCategories: (Categories) -> Unit

    ): RecyclerView.Adapter<AdapterCategoriesSelectDialog.CategoriesViewHolder>() {

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

        holder.itemView.setOnClickListener {
            IdCategories.invoke(itemCategories)
        }
        holder.tvMonneyCategories.visibility = View.GONE





    }

    override fun getItemCount(): Int {
        return mListFilterType.size
    }



}