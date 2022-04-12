package com.truongdc21.inspending.adapter.statistical

import android.R.attr.*
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.truongdc21.inspending.R
import com.truongdc21.inspending.model.Categories
import com.truongdc21.inspending.model.Chart
import com.truongdc21.inspending.model.DateTime
import com.truongdc21.inspending.model.History
import com.truongdc21.inspending.util.Date
import com.truongdc21.inspending.util.KeyWord
import com.truongdc21.inspending.util.NumberTextWatcherForThousand
import com.truongdc21.inspending.viewmodel.MainViewModel
import java.time.DayOfWeek
import java.time.LocalDate


class AdapterChartSatisticals(
    val context: Context,
    val mListChart : MutableList<Chart>,
    val MaxListChart : Int,
    val mListCategories: List<Categories>,
)  : RecyclerView.Adapter<AdapterChartSatisticals.ChartViewHolder>(){

    var ParentHeight = 0
    var ParrenWith = 0

    inner class ChartViewHolder(ctView: View) : RecyclerView.ViewHolder(ctView){
        val viewChart = ctView.findViewById<View>(R.id.itemViewChart)
        val imgItemChartCategory = ctView.findViewById<ImageView>(R.id.imgItemChartCategory)
        val itemChartCardView = ctView.findViewById<CardView>(R.id.itemChartCardView)
        val viewItemIconViewChart = ctView.findViewById<ConstraintLayout>(R.id.viewItemIconViewChart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_chart_statisticals, parent, false)
        ParentHeight = parent.height
        ParrenWith = parent.width
        return ChartViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
        val items = mListChart[position]
        val Max = MaxListChart
        var MaxOne = Max / 100

       if (MaxOne == 0){
           MaxOne = 1
       }
        val Chart = items.monneyCategory / MaxOne

        val heightOne = (ParentHeight.toDouble() / 100)
        val heightCardView : Double = 11 * heightOne
        val MarginCardViewTop : Double = 3 * heightOne
        val MarginCardViewBottom : Double = 3 * heightOne

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams(55, heightCardView.toInt()),
        )
        params.setMargins(0, MarginCardViewTop.toInt(), 0, MarginCardViewBottom.toInt())
        val ViewChart : Double = ( Chart * 0.83 )* heightOne

        holder.viewChart.layoutParams = LinearLayout.LayoutParams(55, ViewChart.toInt())

        holder.itemChartCardView.layoutParams = params
        var nameCategory = ""
        for (i in mListCategories){
            if (items.IdCategory == i.Id){
                holder.imgItemChartCategory.setBackgroundResource(i.imgCategories)
                nameCategory = i.NameCategories
            }
        }


        holder.itemView.setOnClickListener {
            Toast.makeText(context, "${nameCategory}: ${NumberTextWatcherForThousand.NumbertoThousand(items.monneyCategory)}", Toast.LENGTH_SHORT).show()
        }

        // Check Type Categories AND Set Color view
        for (i in mListCategories){
            if (items.IdCategory == i.Id &&  i.typeCategories == KeyWord.Expenses){
                holder.viewItemIconViewChart.background = ColorDrawable(Color.parseColor("#FA8072"))
            }else if (items.IdCategory == i.Id &&  i.typeCategories == KeyWord.Income){
                holder.viewItemIconViewChart.background = ColorDrawable(Color.parseColor("#9ACD32"))
            }
        }

    }

    override fun getItemCount(): Int {
        return mListChart.size
    }


}
