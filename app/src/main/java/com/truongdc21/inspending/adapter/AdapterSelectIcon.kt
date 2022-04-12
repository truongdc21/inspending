package com.truongdc21.inspending.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.truongdc21.inspending.R
import com.truongdc21.inspending.interfa.OnClickICon
import com.truongdc21.inspending.model.Icon
import com.truongdc21.inspending.util.DialogIcon

class AdapterSelectIcon(

    val mList : MutableList<Int>,
    val onClickICon: OnClickICon
    )
    : RecyclerView.Adapter<AdapterSelectIcon.IconViewHodel>() {
    inner class IconViewHodel (ctView : View) : RecyclerView.ViewHolder(ctView){
        val icon = ctView.findViewById<ImageView>(R.id.item_dialog_select_icon)
        val constrianIcon = ctView.findViewById<ConstraintLayout>(R.id.constraintIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHodel {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_icon , parent , false)
        return IconViewHodel(view)
    }

    override fun onBindViewHolder(holder: IconViewHodel, position: Int) {
        val itemIcon = mList[position]

        holder.icon.setBackgroundResource(itemIcon)
        holder.constrianIcon.setOnClickListener {
            onClickICon.Iclick(position , itemIcon)
            Log.d("test" , "Position ${itemIcon.toString()}")
        }
        if (position == DialogIcon.positonIcon){
            holder.constrianIcon.setBackgroundResource(R.drawable.backgr_icon_was_select)
            holder.icon.backgroundTintList = ColorStateList.valueOf(R.color.white)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}