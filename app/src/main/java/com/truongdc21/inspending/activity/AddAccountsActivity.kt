package com.truongdc21.inspending.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.truongdc21.inspending.R
import com.truongdc21.inspending.databinding.ActivityAddAccountsBinding
import com.truongdc21.inspending.interfa.OnClickICon
import com.truongdc21.inspending.model.Accounts
import com.truongdc21.inspending.model.Icon
import com.truongdc21.inspending.util.DialogIcon
import com.truongdc21.inspending.util.NumberTextWatcherForThousand
import com.truongdc21.inspending.viewmodel.accounts.AccountsViewmodel
import com.truongdc21.inspending.viewmodel.accounts.AddAccountsViewmodel
import worker8.com.github.radiogroupplus.RadioGroupPlus
import java.util.*


class AddAccountsActivity : AppCompatActivity(),OnClickICon {

    private lateinit var binding : ActivityAddAccountsBinding
    private lateinit var mViewModelAccounts : AccountsViewmodel
    private lateinit var mViewModel : AddAccountsViewmodel

    private var imgAccounts : Int = R.drawable.ic_accounts_6
    private var description : String = ""

    private var mListIcon = mutableListOf<Icon>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAccountsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModelAccounts = ViewModelProvider(this).get(AccountsViewmodel::class.java)
        mViewModel = ViewModelProvider(this).get(AddAccountsViewmodel::class.java)

        binding.apply {
            appbarConstrain.background = null
            toolbarAddAccounts.setNavigationOnClickListener { finish() }
            mViewModel.liveDataImgAccounts.observe(this@AddAccountsActivity, androidx.lifecycle.Observer { ResoureIcon ->
                    icAccounts.setBackgroundResource(ResoureIcon)
                })
        }

        acctionDoneInputText(binding.edtName)
        acctionDoneInputText(binding.edtMonney)

        binding.edtMonney.addTextChangedListener(NumberTextWatcherForThousand(binding.edtMonney))
        binding.btnCheckAdd.setOnClickListener { addAcounts() }

        binding.apply {

            // Checked type
            mViewModel.liveDataTypeAccounts.observe(
                this@AddAccountsActivity,
                androidx.lifecycle.Observer { type ->
                    when (type) {
                        0 -> typeAcounts.text = "Bình thường"
                        1 -> typeAcounts.text = "Nợ"
                        2 -> typeAcounts.text = "Tiết Kiệm"
                    }
                })

            // Checked Currency
            mViewModel.liveDataCurrency.observe(
                this@AddAccountsActivity,
                androidx.lifecycle.Observer { currency ->
                    Log.d("curren", "Currency : ${currency}")
                    when (currency) {
                        "GBP" -> tvCurrency.text = "Bảng Anh - GBP"
                        "EUR" -> tvCurrency.text = "Euro - EUR"
                        "CHF" -> tvCurrency.text = "Franc Thụy Sĩ"
                        "CNY" -> tvCurrency.text = "Nhân dân tệ - CNY"
                        "RUB" -> tvCurrency.text = "Rup Nga - RUB"
                        "USD" -> tvCurrency.text = "Dollar Mỹ - USD"
                        "VND" -> tvCurrency.text = "Vietnamese dong - VND"
                        "JPY" -> tvCurrency.text = "Yên Nhật - JPY"
                        "CAD" -> tvCurrency.text = "Đô la Canada - CAD"
                        "AUD" -> tvCurrency.text = "Đô là Úc - AUD"
                    }
                })

            // Checked descriptions
            mViewModel.liveDataDestription.observe(
                this@AddAccountsActivity,
                androidx.lifecycle.Observer {
                    if (it == "") {
                        descriptionAccounts.text = "Không"
                    } else {
                        binding.descriptionAccounts.text = it
                    }
                })


            linearTypeAccounts.setOnClickListener { DialogType() }
            linearDescription.setOnClickListener { DialogDescription() }
            linearCurrency.setOnClickListener { DialogCurrency() }
            cardViewIconAccounts.setOnClickListener { DialogIconAccounts() }
        }

        mListIcon.add(Icon(R.drawable.ic_accounts_4))
        mListIcon.add(Icon(R.drawable.ic_accounts_5))
        mListIcon.add(Icon(R.drawable.ic_accounts_6))
        mListIcon.add(Icon(R.drawable.ic_accounts_7))
        mListIcon.add(Icon(R.drawable.ic_accounts_8))

    }

    private fun addAcounts (){
        val srtName = binding.edtName.text.toString().trim()
        val srtMonney = NumberTextWatcherForThousand.trimCommaOfString(binding.edtMonney.text.toString())

        if (srtMonney.isEmpty() || srtName.isEmpty()) return
        val accounts = Accounts(
            0,
            mViewModel.ImgAccounts,
            srtName,
            srtMonney.toLong(),
            srtMonney.toLong(),
            mViewModel.TypeAccountss,
            mViewModel.Currency,
            mViewModel.Destription
        )

        mViewModelAccounts.addAccounts(accounts)
        Toast.makeText(this, resources.getString(R.string.add_accounts_succsessfully), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun DialogType(){
        val listItems = arrayOf("Bình thường", "Nợ", "Tiết Kiệm")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Loại")
        val checkedItem = mViewModel.TypeAccountss //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(
            listItems,
            checkedItem,
            DialogInterface.OnClickListener { dialog, which ->
                //Toast.makeText(this, "Position: " + which + " Value: " + listItems[which], Toast.LENGTH_LONG).show()
                binding.typeAcounts.text = listItems[which]
                when (which) {
                    0, 1, 2 -> {
                        mViewModel.TypeAccountss = which
                        dialog.dismiss()
                    }
                }
            })
        builder.create().show()
    }

    private fun DialogDescription (){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Mô tả")
        val input = EditText(this)
        input.setText(mViewModel.Destription)
        val layoutInput = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
        )
        input.layoutParams = layoutInput
        builder.setView(input)
        builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
            val srtDescription = input.text.toString()
            mViewModel.Destription = srtDescription
            mViewModel.liveDataDestription.value = mViewModel.Destription
            dialog.dismiss()
        })
        builder.setNegativeButton("Hủy", DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
        })
        builder.create().show()
    }

    private fun DialogCurrency(){
        val dialog  = Dialog(this)
        dialog.setTitle("Đơn vị tiền tệ")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_dialog_currency)
        val window = dialog.window
        if (window == null){ return}
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val windowAttributes = window.attributes
        windowAttributes.gravity = Gravity.CENTER
        window.attributes = windowAttributes

        val radioGroupCurrency = dialog.findViewById<RadioGroupPlus>(R.id.radioGroupCurrency)
        val buttonDone = dialog.findViewById<Button>(R.id.btnDone)
        val buttonCancel = dialog.findViewById<Button>(R.id.btnCancel)
        buttonDone.background = null
        buttonCancel.background = null
        buttonCancel.setOnClickListener { dialog.dismiss() }
        buttonDone.setOnClickListener {
            val id = radioGroupCurrency.checkedRadioButtonId
            //val rd = dialog.findViewById<RadioButton>(id)
            when(id){
                R.id.tvCurrencyBangAnh -> mViewModel.Currency = "GBP"
                R.id.tvCurrencyEuro -> mViewModel.Currency = "EUR"
                R.id.tvCurrencyThuySi -> mViewModel.Currency = "CHF"
                R.id.tvCurrencyNhandante -> mViewModel.Currency = "CNY"
                R.id.tvCurrencyRupNga -> mViewModel.Currency = "RUB"
                R.id.tvCurrencyDollar -> mViewModel.Currency = "USD"
                R.id.tvCurrencyVietnam -> mViewModel.Currency = "VND"
                R.id.tvCurrencyYennhat -> mViewModel.Currency = "JPY"
                R.id.tvCurrencyDollarCanada -> mViewModel.Currency = "CAD"
                R.id.tvCurrencyDollarUc -> mViewModel.Currency = "AUD"
            }
            mViewModel.liveDataCurrency.value = mViewModel.Currency
            Toast.makeText(this, "${mViewModel.Currency}", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.show()
    }

    fun DialogIconAccounts() {

        val dialog  = Dialog(this)
        dialog.setTitle("Icon Accounts")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_select_acounts_categories)
        val window = dialog.window
        if (window == null){ return}
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val windowAttributes = window.attributes
        windowAttributes.gravity = Gravity.CENTER
        window.attributes = windowAttributes
        val rvAccount = dialog.findViewById<RecyclerView>(R.id.rvSelectIconAccounts)
        val iconHome = dialog.findViewById<ImageView>(R.id.icHomeDialog)
        setAdapter(rvAccount, this, mViewModel.mListIcon)
        mViewModel.currenIcon.observe(this, androidx.lifecycle.Observer {
            setAdapter(rvAccount, this, mViewModel.mListIcon)
        })
        mViewModel.liveDataImgAccounts.observe(this,
            androidx.lifecycle.Observer { ResoureIcon -> iconHome.setBackgroundResource(ResoureIcon) })
        dialog.show()

        
    }

    override fun Iclick(position: Int, ResoureIcon: Int) {
        DialogIcon.positonIcon = position
        mViewModel.currenIcon.value = DialogIcon.positonIcon

        mViewModel.ImgAccounts = ResoureIcon
        mViewModel.liveDataImgAccounts.value = mViewModel.ImgAccounts
    }

    override fun IclickSelectAccounts(accounts: Accounts) {
        // not use
    }

    private fun acctionDoneInputText (inputEditText: TextInputEditText){
        inputEditText.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //Clear focus here from edittext
                inputEditText.clearFocus()
            }
            false
        })
    }

}