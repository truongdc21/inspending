package com.truongdc21.inspending.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.truongdc21.inspending.R
import com.truongdc21.inspending.model.Categories

class AdapterSelectCategoriesDialog (
    val mList : List<Categories>,
    val clickItemCategories :((Categories) -> Unit?)
): RecyclerView.Adapter<AdapterSelectCategoriesDialog.CategoriesDialogViewHolder>()  {

    inner class CategoriesDialogViewHolder (ctView : View): RecyclerView.ViewHolder(ctView){
        val imgCategories = ctView.findViewById<ImageView>(R.id.imgCategoriesDialogSelect)
        val tvNameCategories = ctView.findViewById<TextView>(R.id.tvItemNameCategoriesDialogSelect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesDialogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_categories_dialog , parent , false)
        return CategoriesDialogViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriesDialogViewHolder, position: Int) {
        val item = mList[position]
            holder.imgCategories.setBackgroundResource(item.imgCategories)
            holder.tvNameCategories.text = item.NameCategories
        holder.itemView.setOnClickListener {
            clickItemCategories.invoke(item)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }


}