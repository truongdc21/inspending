package com.truongdc21.inspending.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.truongdc21.inspending.R
import com.truongdc21.inspending.interfa.OnClickICon
import com.truongdc21.inspending.model.Accounts
import com.truongdc21.inspending.util.NumberTextWatcherForThousand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdapterSelectAccounts (
    val accountsID: Int,
    val mListAccounts : List<Accounts>,
    val clickSelectAccounts :((Accounts) -> Unit)
) : RecyclerView.Adapter<AdapterSelectAccounts.SelectAccountsViewholder>() {

    inner class SelectAccountsViewholder (ctView : View) : RecyclerView.ViewHolder(ctView){
        val imgAccounts = ctView.findViewById<ImageView>(R.id.imgItemSelectAccounts)
        val tvNameAccounts = ctView.findViewById<TextView>(R.id.tvItemSelectNameAcounts)
        val tvMonneyAccounts = ctView.findViewById<TextView>(R.id.tvItemSelectMonneyAccounts)
        val viewLinearItemSelectAccounts =ctView.findViewById<LinearLayout>(R.id.viewLinearItwmSelectAccounts)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectAccountsViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_select_accounts, parent , false)
        return SelectAccountsViewholder(view)
    }

    override fun onBindViewHolder(holder: SelectAccountsViewholder, position: Int) {
        val itemAccounts = mListAccounts[position]

        CoroutineScope(Dispatchers.Default).launch {
           if (accountsID == itemAccounts.Id){
               withContext(Dispatchers.Main){
                   holder.viewLinearItemSelectAccounts.setBackgroundColor(Color.LTGRAY)
               }
           }
        }
        holder.imgAccounts.setBackgroundResource(itemAccounts.img)
        holder.tvNameAccounts.text = itemAccounts.Name
        holder.tvMonneyAccounts.text = "${NumberTextWatcherForThousand.NumbertoThousand(itemAccounts.monney)}"
        holder.itemView.setOnClickListener {
            clickSelectAccounts.invoke(itemAccounts)
        }


    }

    override fun getItemCount(): Int {
        return mListAccounts.size
    }

}