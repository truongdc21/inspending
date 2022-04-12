package com.truongdc21.inspending.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ramotion.foldingcell.FoldingCell
import com.truongdc21.inspending.R
import com.truongdc21.inspending.model.Accounts
import com.truongdc21.inspending.util.NumberTextWatcherForThousand

class AdapterAccounts (
    val context: Context,
    val mListAccounts: List<Accounts> )
    : RecyclerView.Adapter<AdapterAccounts.AccountsViewHodel>() {

    inner class AccountsViewHodel (ctView : View) : RecyclerView.ViewHolder(ctView){
        val imgAccounts = ctView.findViewById<ImageView>(R.id.imgItemAccounts)
        val tvNameAccounts = ctView.findViewById<TextView>(R.id.tvItemNameAcounts)
        val tvMonneyAccounts = ctView.findViewById<TextView>(R.id.tvItemMonneyAccounts)
        val folldingcell = ctView.findViewById<FoldingCell>(R.id.folding_cell_item_account)
        //val btnTest = ctView.findViewById<Button>(R.id.btnTest)

        val tvItemBalance = ctView.findViewById<TextView>(R.id.tvItemMonneyBalance)
        val tvItemName = ctView.findViewById<TextView>(R.id.tvItemNameAccounts)
        val tvItemCurrency = ctView.findViewById<TextView>(R.id.tvItemCurrencyAccounts)
        val tvItemDescription = ctView.findViewById<TextView>(R.id.tvItemDescriptionAccounts)
        val tvItemFirtBalance = ctView.findViewById<TextView>(R.id.tvItemInitialBalanceAccounts)
        val btnAccountsHideViewF2 = ctView.findViewById<CardView>(R.id.btnEditTransaction)
        val btnShowViewF2 = ctView.findViewById<CardView>(R.id.btnShowViewF2)
        val btnEdit = ctView.findViewById<CardView>(R.id.btnCirculaEdit)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountsViewHodel {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_accounts, parent , false)
        return AccountsViewHodel(view)
    }

    override fun onBindViewHolder(holder: AccountsViewHodel, position: Int) {
        val itemAccounts = mListAccounts[position]
        holder.imgAccounts.setBackgroundResource(itemAccounts.img)
        holder.tvNameAccounts.text = itemAccounts.Name
        holder.tvMonneyAccounts.text = NumberTextWatcherForThousand.NumbertoThousand(itemAccounts.monney)

        // Show card
        holder.tvItemBalance.text = "${NumberTextWatcherForThousand.NumbertoThousand(itemAccounts.monney)}"
        holder.tvItemName.text = itemAccounts.Name
        holder.tvItemCurrency.text = itemAccounts.currency
        if (itemAccounts.description == ""){
            holder.tvItemDescription.text = "không"
        }else{
            holder.tvItemDescription.text = itemAccounts.description
        }
        holder.tvItemFirtBalance.text = NumberTextWatcherForThousand.NumbertoThousand(itemAccounts.firtBalance)



        holder.btnShowViewF2.setOnClickListener {
            holder.folldingcell.toggle(false)
        }
        holder.btnAccountsHideViewF2.setOnClickListener {
            holder.folldingcell.toggle(false)
        }
        holder.btnEdit.setOnClickListener {
            Toast.makeText(context, "Xin lỗi, Mục này chưa được cập nhật !", Toast.LENGTH_SHORT).show()
        }



    }

    override fun getItemCount(): Int {
        return mListAccounts.size
    }

}
