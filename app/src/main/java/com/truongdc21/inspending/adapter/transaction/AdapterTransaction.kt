package com.truongdc21.inspending.adapter.transaction

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
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
import com.truongdc21.inspending.model.Categories
import com.truongdc21.inspending.model.History
import com.truongdc21.inspending.util.KeyWord
import com.truongdc21.inspending.util.NumberTextWatcherForThousand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AdapterTransaction (
    val context: Context,
    val mList : List<History> ,
    val mAccounts : List<Accounts>,
    val mCategories : List<Categories>,
    val mDateF2 : String,

): RecyclerView.Adapter<AdapterTransaction.TransactionViewHolder>() {
    inner class TransactionViewHolder (ctView : View):RecyclerView.ViewHolder(ctView){
        val imgCategories = ctView.findViewById<ImageView>(R.id.imgItemTransactionCategories)
        val nameCategories = ctView.findViewById<TextView>(R.id.tvItemtransactionNameCategories)
        val imgAccounts = ctView.findViewById<ImageView>(R.id.imgItemTransactionAccounts)
        val nameAccounts = ctView.findViewById<TextView>(R.id.tvItemTracsactionNameAccounts)
        val tvMonney = ctView.findViewById<TextView>(R.id.tvItemMonneyTransaction)
        val folldingCell = ctView.findViewById<FoldingCell>(R.id.folding_cell_item_transaction)

        // F2

        val tvItemMonneyF2 = ctView.findViewById<TextView>(R.id.tvItemMonneyTransactionTwo)
        val tvItemDateF2= ctView.findViewById<TextView>(R.id.tvItemDateTransactionTwo)
        val tvNameAccountsF2 = ctView.findViewById<TextView>(R.id.tvItemNameAccountsF2)
        val tvNameCategoriesF2 = ctView.findViewById<TextView>(R.id.tvItemNameCategoriesF2)
        val imgAccountsF2 = ctView.findViewById<ImageView>(R.id.imgItemAccountsF2)
        val imgCategoriesF2 = ctView.findViewById<ImageView>(R.id.imgItemCategoriesF2)
        val tvItemDatetimeNow = ctView.findViewById<TextView>(R.id.tvItemDatetimeNow)
        val tvItemDesciption = ctView.findViewById<TextView>(R.id.tvItemDescriptionTransaction)

        // Button Floating
        val btnEdit = ctView.findViewById<CardView>(R.id.btnEditTransaction)
        val btnRemove = ctView.findViewById<CardView>(R.id.btnCirculaRemoveTransaction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_transaction, parent , false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val itemTransaction = mList[position]

        val dateNowf1 = LocalDate.parse(itemTransaction.DateNow)
        val dateNowf2 = dateNowf1.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        // Set Date F2
        CoroutineScope(Dispatchers.Main).launch {
            holder.tvItemDateF2.text = mDateF2
            holder.tvItemDatetimeNow.text = "${dateNowf2} - ${itemTransaction.TimeNow}"
        }

        // set Monney
        CoroutineScope(Dispatchers.Default).launch {
            for (iCategori in mCategories){
                if (itemTransaction.Categories == iCategori.Id){
                    if (iCategori.typeCategories == KeyWord.Income){
                        withContext(Dispatchers.Main){
                            holder.tvMonney.setTextColor(Color.parseColor("#689F38"))
                            holder.tvMonney.text = "+ ${NumberTextWatcherForThousand.NumbertoThousand(itemTransaction.monney)}"
                            // set Item F2
                            holder.tvItemMonneyF2.setTextColor(Color.parseColor("#689F38"))
                            holder.tvItemMonneyF2.text = "+ ${NumberTextWatcherForThousand.NumbertoThousand(itemTransaction.monney)}"
                        }
                        break
                    }else{
                        withContext(Dispatchers.Main){
                            holder.tvMonney.setTextColor(Color.parseColor("#EC1010"))
                            holder.tvMonney.text = "- ${NumberTextWatcherForThousand.NumbertoThousand(itemTransaction.monney)}"
                            // set Item F2
                            holder.tvItemMonneyF2.setTextColor(Color.parseColor("#EC1010"))
                            holder.tvItemMonneyF2.text = "- ${NumberTextWatcherForThousand.NumbertoThousand(itemTransaction.monney)}"
                        }
                        break
                    }
                }
            }
        }

        CoroutineScope(Dispatchers.Default).launch {
            // set Categories
            for (categories in mCategories){
                if (itemTransaction.Categories == categories.Id){
                    withContext(Dispatchers.Main){
                        holder.imgCategories.setBackgroundResource(categories.imgCategories)
                        holder.nameCategories.text = categories.NameCategories
                        //set Item F2
                        holder.imgCategoriesF2.setBackgroundResource(categories.imgCategories)
                        holder.tvNameCategoriesF2.text = categories.NameCategories
                    }
                }
            }
            // set Accounts
            for (accounts in mAccounts){
                if (itemTransaction.Accounts == accounts.Id){
                    withContext(Dispatchers.Main){
                        holder.imgAccounts.setBackgroundResource(accounts.img)
                        holder.nameAccounts.text = accounts.Name
                        // Set Item F2
                        holder.imgAccountsF2.setBackgroundResource(accounts.img)
                        holder.tvNameAccountsF2.text = accounts.Name
                    }
                }
            }
        }

        // Set Descriptiom
        CoroutineScope(Dispatchers.Default).launch {
            if (itemTransaction.Description == ""){
                withContext(Dispatchers.Main){
                    holder.tvItemDesciption.text = "Không .. "
                }
            }else{
                withContext(Dispatchers.Main){
                    holder.tvItemDesciption.text = itemTransaction.Description
                }
            }

        }

        holder.folldingCell.setOnClickListener { f1 ->
            holder.folldingCell.toggle(false)
            holder.btnEdit.setOnClickListener {
                Toast.makeText(context, "Xin lỗi, Mục này chưa được cập nhật !", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            holder.btnRemove.setOnClickListener {
                Toast.makeText(context, "Xin lỗi, Mục này chưa được cập nhật !", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }
}