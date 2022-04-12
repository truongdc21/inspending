package com.truongdc21.inspending.fragment.BottomSheet

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.truongdc21.inspending.R
import com.truongdc21.inspending.adapter.AdapterSelectAccounts
import com.truongdc21.inspending.adapter.categories.AdapterViewPagerSelectCategories
import com.truongdc21.inspending.databinding.FragmentBottomSheetAddCategoriesBinding
import com.truongdc21.inspending.model.Accounts
import com.truongdc21.inspending.model.Categories
import com.truongdc21.inspending.model.History
import com.truongdc21.inspending.util.Date
import com.truongdc21.inspending.util.KeyWord
import com.truongdc21.inspending.util.NumberTextWatcherForThousand
import com.truongdc21.inspending.viewmodel.MainViewModel
import com.truongdc21.inspending.viewmodel.accounts.AccountsViewmodel
import com.truongdc21.inspending.viewmodel.categories.CategoriesViewmodel
import com.truongdc21.inspending.viewmodel.history.TransactionViewmodel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


class BottomSheetAddCategoriesFragment : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentBottomSheetAddCategoriesBinding

    private lateinit var AccountsVMD : AccountsViewmodel
    private lateinit var CategoriesVMD : CategoriesViewmodel
    private lateinit var TransactionVMD : TransactionViewmodel
    private lateinit var MainVMD : MainViewModel
    private lateinit var dataStore : DataStore<Preferences>

    private var accountsId : Int? = null
    private var categoriesId : Int? = null
    private var accountsIdCheck : Int? = null

    var srtDate : LocalDate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val bottomSheeDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        binding = FragmentBottomSheetAddCategoriesBinding.inflate(layoutInflater)
        bottomSheeDialog.setContentView(binding.root)

        srtDate = Date.mDate
        setUpDataStoreANDViewmodel()
        binding.apply {

            edtAddCategoriesMonney.addTextChangedListener(NumberTextWatcherForThousand(binding.edtAddCategoriesMonney))
            AutoChoiseAccountsAndDate()
            btnCloseBottomSheet.setOnClickListener {
                if(binding.edtAddCategoriesMonney.length() > 0){
                    DialogDiscardChanged(bottomSheeDialog)
                }else{
                    bottomSheeDialog.dismiss()
                }
            }

            val animationClick = AlphaAnimation(9f, 0.1f)
            vgrFrom.setOnClickListener {
                vgrFrom.startAnimation(animationClick)
                DialogSelectAccounts()
            }
            vgrTo.setOnClickListener {
                vgrTo.startAnimation(animationClick)
                DialogSelectCategories()
            }
            tvShowDate.setOnClickListener {
                tvShowDate.startAnimation(animationClick)
                DialogSelectDate()
            }
            btnSaveTransaction.setOnClickListener {
                AddTransaction(bottomSheeDialog)
            }

            acctionDoneInputText(edtAddCategoriesMonney)
            acctionDoneInputText(edtWriteNote)

            lifecycleScope.launch {
                accountsIdCheck = MainVMD.readDataAutoChoiseAccounts(dataStore , "BOTTOM_SHEET_TRANSACTION")
            }


        }
        return bottomSheeDialog
    }

    // get Style for BottomSheet Dialog
    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }


    // Hàm chọn tài khoản
    private fun DialogSelectAccounts(){
        val dialog  = Dialog(this.requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_select_accounts_dialog)
        val window = dialog.window
        if (window == null){ return}
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        val windowAttributes = window.attributes
        windowAttributes.gravity = Gravity.CENTER
        window.attributes = windowAttributes
        val rvAccount = dialog.findViewById<RecyclerView>(R.id.rvSelectAccountsDialog)
        val VgrallAccount  = dialog.findViewById<LinearLayout>(R.id.vgrAllAccounts)
        //val incluGachduoi = dialog.findViewById<TextView>(R.id.inclidedGachduoiofAllAccounts)
        VgrallAccount.visibility = View.GONE
        //incluGachduoi.visibility = View.GONE
        rvAccount.layoutManager = LinearLayoutManager(this.requireContext())
        //val itemDecoration: RecyclerView.ItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        //rvAccount.addItemDecoration(itemDecoration)
            AccountsVMD.getListAccounts.observe(this@BottomSheetAddCategoriesFragment, Observer { accounts ->
                rvAccount.adapter =
                    AdapterSelectAccounts(accountsIdCheck!!, accounts, clickSelectAccounts = { accounts ->
                        binding.apply {
                            imgFromAccounts.setBackgroundResource(accounts.img)
                            tvSelectAccounts.text = accounts.Name
                            accountsId = accounts.Id
                            accountsIdCheck = accounts.Id

                        }
                        dialog.dismiss()
                    })
            })

        dialog.show()
    }



    // hàm thêm giao dịch
    private fun AddTransaction(bottomSheetDialog: BottomSheetDialog){

        // get Long Monney from InputText
        val srtMonney = NumberTextWatcherForThousand.trimCommaOfString(binding.edtAddCategoriesMonney.text.toString())

        // get srtDescription
        val srtDescription = binding.edtWriteNote.text.toString()

        // Check null in view text
        if (accountsId == null || categoriesId == null || srtMonney.isEmpty() ) {
            Toast.makeText(context, "Vui lòng nhập đủ thông tin !!", Toast.LENGTH_SHORT).show()
            return
        }

        val listAccounts = ArrayList<Accounts>()
        val listCategory = ArrayList<Categories>()
        var MonneyAccounts : Long = 0

        CategoriesVMD.getListCategories.observe(this, Observer { categories ->
            for (i in categories){
                if (i.Id == categoriesId){
                    listCategory.add(i)
                }
            }
        })

        AccountsVMD.getListAccounts.observe(this@BottomSheetAddCategoriesFragment, Observer { accounts ->
            for (i in accounts){
                if (i.Id == accountsId){
                    listAccounts.add(i)
                }
            }
            for (i in listAccounts){
                MonneyAccounts = i.monney
            }
        })

       for (i in listCategory){
           if (i.typeCategories == KeyWord.Income){
               AddTransactionF2(srtMonney.toLong(), srtDescription)
               UpdateAccounts(KeyWord.Income , MonneyAccounts , srtMonney.toLong() , listAccounts)
               bottomSheetDialog.dismiss()
           }else{
               if (srtMonney.toLong() < MonneyAccounts){
                   AddTransactionF2(srtMonney.toLong(), srtDescription)
                   UpdateAccounts(KeyWord.Expenses , MonneyAccounts , srtMonney.toLong() , listAccounts)
                   bottomSheetDialog.dismiss()
               }else{
                   Toast.makeText(context, "Số dư không đủ", Toast.LENGTH_SHORT).show()
               }
           }
       }


    }

    private fun AddTransactionF2 (srtMonney : Long , srtDescription : String){
        val transaction = History(
            0,
            accountsId!!,
            categoriesId!!,
            srtMonney,
            srtDate.toString(),
            Date.mDateNow.toString(),
            Date.mTime.toString(),
            srtDescription
        )
        TransactionVMD.addHistory(transaction)
        Toast.makeText(context, "Giao dịch thành công", Toast.LENGTH_SHORT).show()
    }

    private fun UpdateAccounts (Key : String , MonneyAccounts : Long , srtMonney : Long , listAccounts : ArrayList<Accounts>){
        var accId : Int? = null
        var accImg : Int? = null
        var accName : String? = null
        var accType : Int? = null
        var accCurency : String? = null
        var accDescrip : String? = null
        var MonneyBalance : Long? = null
        var firtBalance : Long? = null

        for (i in listAccounts){
            accId = i.Id
            accImg = i.img
            accName = i.Name
            accType = i.type
            accCurency = i.currency
            accDescrip = i.description
            firtBalance = i.firtBalance
        }

        if (Key == KeyWord.Income){
            MonneyBalance = MonneyAccounts + srtMonney
        }else{
            MonneyBalance = MonneyAccounts - srtMonney
        }
        val accounts = Accounts(accId!!, accImg!!,accName!!, MonneyBalance ,firtBalance!!, accType!!, accCurency!! , accDescrip!! )
        AccountsVMD.updateAccounts(accounts)
    }
//
    // hàm tự chọn Accounts và date
    private fun AutoChoiseAccountsAndDate (){
        lifecycleScope.launch {
            val accounts = MainVMD.readDataAutoChoiseAccounts(dataStore , "BOTTOM_SHEET_TRANSACTION")
            AccountsVMD.getListAccounts.observe(this@BottomSheetAddCategoriesFragment, Observer {
                for (i in it) {
                    if (i.Id == accounts) {
                        binding.imgFromAccounts.setBackgroundResource(i.img)
                        binding.tvSelectAccounts.text = i.Name
                        accountsId = i.Id
                    }
                }
            })

            if (Date.mDate == Date.mDateNow){
                    binding.tvShowDate.text = "Hôm nay"

            }else{
                    binding.tvShowDate.text = MainVMD.formatterDateToolbar(Date.mDate)
            }
        }
    }


    // Function Dialog Select Categories
    private fun DialogSelectCategories(){

        val dialog  = Dialog(this.requireContext())
        dialog.setTitle("Test")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_select_categories_dialog)
        val window = dialog.window
        if (window == null){ return}
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        val windowAttributes = window.attributes
        windowAttributes.gravity = Gravity.CENTER
        window.attributes = windowAttributes

        val rvCategories = dialog.findViewById<RecyclerView>(R.id.vpgSelectCategoriesDialog)
        val tabItemLayout = dialog.findViewById<TabLayout>(R.id.tabItemSelectCategories)

        rvCategories.layoutManager = LinearLayoutManager(context)
        CategoriesVMD.getListCategories.observe(this, Observer { mCategories ->
                val listCategoriesIncome = CategoriesVMD.ListAllCategoriesIncome()
                val listCategoriesExpenses = CategoriesVMD.ListAllCategoriesExpenses()
                val listCategoriesOnfixKeyExpenses = CategoriesVMD.ListCategoriesOnfix(mCategories, KeyWord.Expenses)
                val listCategoriesOnfixKeyIncome = CategoriesVMD.ListCategoriesOnfix(mCategories, KeyWord.Income)

                //rvCategories.adapter = AdapterViewPagerSelectCategories(requireContext(), listCategoriesExpenses, listCategoriesOnfixKeyExpenses)
                setUpAdapterOfSelecCategories( dialog,rvCategories , listCategoriesExpenses , listCategoriesOnfixKeyExpenses)
                tabItemLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {

                        when (tab!!.position) {
                            0 -> {
                                setUpAdapterOfSelecCategories( dialog,rvCategories , listCategoriesExpenses , listCategoriesOnfixKeyExpenses)
                            }
                            1 -> {
                                setUpAdapterOfSelecCategories(dialog ,rvCategories , listCategoriesIncome , listCategoriesOnfixKeyIncome)
                            }
                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {}
                    override fun onTabReselected(tab: TabLayout.Tab?) {}

                })
        })

        dialog.show()


    }

    private fun setUpAdapterOfSelecCategories (
        dialog: Dialog,
        rv : RecyclerView,
        listStringTypeCategories : ArrayList<String>,
        listCategoriesFixKey : ArrayList<Categories>
    ){
        rv.adapter = AdapterViewPagerSelectCategories(
            requireContext() ,
            listStringTypeCategories ,
            listCategoriesFixKey , IdCategories = { categories ->
                binding.imgToCategories.setBackgroundResource(categories.imgCategories)
                binding.tvSelectCategories.text = categories.NameCategories
                categoriesId = categories.Id
                dialog.dismiss()
            })
    }

    // Show dialog select date
    private fun DialogSelectDate(){
        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("Date Select")
            .build()
        datePicker.show(childFragmentManager, "date_picker")
        datePicker.addOnPositiveButtonClickListener {
            val DateMaterial = datePicker.selection!!.toLong()
            Date.DateAddTransaction = LocalDate.parse(covertLongtoDate(DateMaterial))
            srtDate = Date.DateAddTransaction
            binding.tvShowDate.text = MainVMD.formatterDateToolbar(Date.DateAddTransaction)
        }
    }

    // Cover Long to Date
    private fun covertLongtoDate(time : Long):String{
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd" , Locale.getDefault())
        return format.format(date)
    }


    // Fun ask again before remove
    private fun DialogDiscardChanged(bottomSheetDialog: BottomSheetDialog){
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle(resources.getString(R.string.cancel_transaction))
        builder.setNegativeButton(resources.getString(R.string.discard_changed_dialog),
            DialogInterface.OnClickListener { dialog, which ->
                bottomSheetDialog.dismiss()
                dialog.dismiss()
            })
        builder.setPositiveButton(
            resources.getString(R.string.keep_edting_dialog),
            DialogInterface.OnClickListener { dialog, which -> })
        builder.create().show()

    }

    // Acction Done whent input text
    private fun acctionDoneInputText (inputEditText: TextInputEditText){
        inputEditText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //Clear focus here from edittext
                inputEditText.clearFocus()
            }
            false
        })
    }

    // Setup Data Store AND Viewmodel
    private fun setUpDataStoreANDViewmodel (){
        dataStore = requireContext().createDataStore(name = "FirstInstalledApp")
        AccountsVMD = ViewModelProvider(this).get(AccountsViewmodel::class.java)
        CategoriesVMD = ViewModelProvider(this).get(CategoriesViewmodel::class.java)
        MainVMD = ViewModelProvider(this).get(MainViewModel::class.java)
        TransactionVMD = ViewModelProvider(this).get(TransactionViewmodel::class.java)
    }





}