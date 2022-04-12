package com.truongdc21.inspending

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationBarView
import com.truongdc21.inspending.activity.AddAccountsActivity
import com.truongdc21.inspending.adapter.AdapterSelectAccounts
import com.truongdc21.inspending.databinding.ActivityMainBinding
import com.truongdc21.inspending.fragment.BottomSheet.BottomSheetAddCategoriesFragment
import com.truongdc21.inspending.fragment.BottomSheet.BottomSheetSelectDateFragment
import com.truongdc21.inspending.fragment.accounts.AccountsFragment
import com.truongdc21.inspending.fragment.categories.CategoriesVPGHomeFragement
import com.truongdc21.inspending.fragment.statisticals.StatisticalsVPGHomeFragement
import com.truongdc21.inspending.fragment.transaction.TransactionVPGHomeFragement
import com.truongdc21.inspending.model.DateTime
import com.truongdc21.inspending.util.Date
import com.truongdc21.inspending.util.NumberTextWatcherForThousand
import com.truongdc21.inspending.viewmodel.MainViewModel
import com.truongdc21.inspending.viewmodel.ViewModelFactory
import com.truongdc21.inspending.viewmodel.accounts.AccountsViewmodel
import com.truongdc21.inspending.viewmodel.categories.CategoriesViewmodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.*

class MainActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var MainVMD: MainViewModel

    private lateinit var AccountsViewModel: AccountsViewmodel
    private lateinit var CategoriesViewModel: CategoriesViewmodel

    private lateinit var dataStore: DataStore<Preferences>

    companion object {
        const val FIRST_INSTALLED_APP = "FIRST_INSTALLED_APP"
        const val CHOISE_ACCOUNTS_BOTTOM_SHEET = "BOTTOM_SHEET_TRANSACTION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setUpViewModelandDataStore()
        MainVMD.CheckFirstInstalledApp(dataStore, MainVMD, AccountsViewModel, CategoriesViewModel)
        setUpBottomNavigation()
        CheckIdAccounts()
        CheckTypeDate()

        window.statusBarColor = resources.getColor(R.color.colorf3)

        binding.apply {
            btnBackDate.setOnClickListener {
                var mListDate: List<DateTime>? = null
                MainVMD.getDateTime.observe(this@MainActivity, Observer { date ->
                    mListDate = date
                })
                MainVMD.PreviousDate(mListDate!!)
            }
            btnNextDate.setOnClickListener {
                var mListDate: List<DateTime>? = null
                MainVMD.getDateTime.observe(this@MainActivity, Observer { date ->
                    mListDate = date
                })
                MainVMD.NextDate(mListDate!!)
            }

            // Event Scroll form Screen Left to Right will Show Navigation Drawer
            btnOpenNavigationDrawer.setOnClickListener {
                binding.drawerLayout.openDrawer(Gravity.LEFT)
            }

            // Event Click  Show Dialog Select Accounts
            vgrAccounts.setOnClickListener {
                DialogSelectAccounts()
            }

            // Event Click Show BottomSheet Dialog let Add transaction
            navAdd.setOnClickListener {
                ShowBottomSheetAddCategoriesFragment()
            }

            // Event Click Show BottomSheet Dialog Select Type Date 1 .. 7
            tvDateToolbar.setOnClickListener {
                ShowBottomShettSelectDate()
            }

            // Event Click icon Add let to next Intent AddAccountsAcivity
            btnAddAcounts.setOnClickListener {
                startActivity(Intent(this@MainActivity, AddAccountsActivity::class.java))
            }

            btnEditCategories.setOnClickListener {
                Toast.makeText(this@MainActivity, "Xin lỗi, Mục này đang trong quá trình cập nhật!", Toast.LENGTH_SHORT).show()
            }
            btnSearchHistory.setOnClickListener {
                Toast.makeText(this@MainActivity, "Xin lỗi, Mục này đang trong quá trình cập nhật!", Toast.LENGTH_SHORT).show()
            }

        }

    }


  // Event use let Check Type Date AND set Change Color TextView Date On Toolbar
    private fun CheckTypeDate() = lifecycleScope.launchWhenCreated {

        MainVMD.getDateTime.observe(this@MainActivity, Observer { datetime ->
            var typedatetime: Int? = null
            var firstDate: String = ""
            var endDate: String = ""

            for (i in datetime) {
                typedatetime = i.TypeDateTime
                firstDate = i.StartDateTime
                endDate = i.EndDateTime
                if (i.TypeDateTime == 6) {
                    Date.RangeDateFirst = LocalDate.parse(firstDate)
                    Date.RangeDateEnd = LocalDate.parse(endDate)
                }
            }

            MainVMD.CheckDateLiveData.observe(this@MainActivity, Observer { date ->
                when (typedatetime) {
                    1 -> {
                        binding.tvDateToolbar.text = MainVMD.formatterDateToolbar(date)
                        CheckDateANDChangeColor(date, Date.mDateNow)
                    }
                    2 -> {
                        binding.tvDateToolbar.text = MainVMD.formatterDateWeeks(date)
                        CheckDateANDChangeColor(date, Date.mDateNow)
                    }
                    3 -> {
                        binding.tvDateToolbar.text = MainVMD.formatterDateMonths(date)
                        CheckDateANDChangeColor(date, Date.mDateNow)
                    }
                    4 -> {
                        binding.tvDateToolbar.text = MainVMD.formatterDateYears(date)
                        CheckDateANDChangeColor(date, Date.mDateNow)
                    }
                    5 -> {
                        binding.tvDateToolbar.text = MainVMD.formatterDateToolbar(date)
                        CheckDateANDChangeColor(date, Date.mDateNow)
                    }
                    6 -> {
                        binding.tvDateToolbar.text = MainVMD.formatterDateRange(
                            Date.RangeDateFirst.toString(),
                            Date.RangeDateEnd.toString()
                        )
                        CheckDateANDChangeColor(Date.RangeDateEnd, LocalDate.parse(endDate))
                    }
                    7 -> {
                        binding.tvDateToolbar.text = "Tất cả thời gian"
                    }
                }
            })
        })


    }

    // Function use Change Color for TextView Date on Toolbar
    private fun CheckDateANDChangeColor(date: LocalDate, dateNow: LocalDate) = lifecycleScope.launch(Dispatchers.Default) {
        if (date != dateNow) {
            withContext(Dispatchers.Main){
                binding.AppBarLayout.setBackgroundColor(resources.getColor(R.color.gray))
                window.statusBarColor = resources.getColor(R.color.gray)
                binding.tabLayoutToolbar.setBackgroundColor(resources.getColor(R.color.gray))
                binding.tvDateToolbar.setBackgroundColor(resources.getColor(R.color.gray))
            }
        } else {
            withContext(Dispatchers.Main){
                binding.AppBarLayout.setBackgroundColor(resources.getColor(R.color.colorf5))
                window.statusBarColor = resources.getColor(R.color.colorf5)
                binding.tabLayoutToolbar.setBackgroundColor(resources.getColor(R.color.colorf5))
                binding.tvDateToolbar.background = resources.getDrawable(R.drawable.backgr_gray)
            }

        }
    }


    // Event Check number Int ID Accounts
    private fun CheckIdAccounts() {
        lifecycleScope.launch {
            val accounts =
                MainVMD.readDataAutoChoiseAccounts(dataStore, CHOISE_ACCOUNTS_BOTTOM_SHEET)

            MainVMD.CheckIdAccounts.value = accounts

            MainVMD.CheckIdAccounts.observe(this@MainActivity, Observer { IdAccounts ->
                AccountsViewModel.getListAccounts.observe(this@MainActivity, Observer { accounts ->
                    for (i in accounts) {
                        if (i.Id == IdAccounts) {
                            binding.imgSelectAccounts.setBackgroundResource(i.img)
                            binding.tvSelectMonneyBar.text = "Tài khoản - ${i.Name}"
                            binding.tvMonneyBar.text =
                                "${NumberTextWatcherForThousand.NumbertoThousand(i.monney)}"

                        } else if (IdAccounts == 0) {
                            binding.imgSelectAccounts.setBackgroundResource(R.drawable.ic_accounts_9)
                            binding.tvSelectMonneyBar.text = "Tất cả các tài khoản"
                            binding.tvMonneyBar.text =
                                "${AccountsViewModel.MonneyAccounts(accounts)}"

                        }
                    }
                })
            })
        }
    }


    // Function Show Dialog let Select Accounts
    private fun DialogSelectAccounts() {
        val dialog = Dialog(this)
        dialog.setTitle("Icon Accounts")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_select_accounts_dialog)
        val window = dialog.window
        if (window == null) {
            return
        }
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val windowAttributes = window.attributes
        windowAttributes.gravity = Gravity.CENTER
        window.attributes = windowAttributes
        val rvAccount = dialog.findViewById<RecyclerView>(R.id.rvSelectAccountsDialog)
        rvAccount.layoutManager = LinearLayoutManager(this)
        //rvAccount.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        val tvMonneyAllAccountsDialog = dialog.findViewById<TextView>(R.id.tvItemMonneyAccountsDialog)
        val viewLinearAllAccounts = dialog.findViewById<LinearLayout>(R.id.viewLinearAllAccounts)

        lifecycleScope.launch {
            val accountsID = MainVMD.readDataAutoChoiseAccounts(dataStore , "BOTTOM_SHEET_TRANSACTION")
            if (accountsID == 0 ){
                withContext(Dispatchers.Main){
                    viewLinearAllAccounts.setBackgroundColor(Color.LTGRAY)
                }
            }
            AccountsViewModel.getListAccounts.observe(this@MainActivity, Observer { accounts ->
                tvMonneyAllAccountsDialog.text = AccountsViewModel.MonneyAccounts(accounts)
                rvAccount.adapter = AdapterSelectAccounts( accountsID!! ,accounts, clickSelectAccounts = { accounts ->
                        SaveIdAccoutsDialog(accounts.Id)
                        dialog.dismiss()
                    })
            })
        }
        val vgrAllAccounts = dialog.findViewById<LinearLayout>(R.id.vgrAllAccounts)
        vgrAllAccounts.setOnClickListener {
            SaveIdAccoutsDialog(0)
            dialog.dismiss()
        }

        dialog.show()
    }

    // Function Save ID Accounts
    private fun SaveIdAccoutsDialog(id: Int) = lifecycleScope.launch() {
        lifecycleScope.launch {
            MainVMD.saveDataAutoChooseAccounts(dataStore, CHOISE_ACCOUNTS_BOTTOM_SHEET, id)
            val accounts =
                MainVMD.readDataAutoChoiseAccounts(dataStore, CHOISE_ACCOUNTS_BOTTOM_SHEET)
            MainVMD.CheckIdAccounts.value = accounts
        }
    }

    // Function Show Bottom Sheet AddTransaction
    private fun ShowBottomSheetAddCategoriesFragment() {
        val BottomSheetDialog = BottomSheetAddCategoriesFragment()
        BottomSheetDialog.show(supportFragmentManager, BottomSheetDialog.tag)
        BottomSheetDialog.isCancelable = false
    }

    // Function Show BottomSheet Choose Date
    private fun ShowBottomShettSelectDate() {
        val bottomSheetDialog = BottomSheetSelectDateFragment()
        bottomSheetDialog.show(supportFragmentManager, bottomSheetDialog.tag)
    }

    // Function SetUp BottomNavigationVIEW
    private fun setUpBottomNavigation() = lifecycleScope.launchWhenCreated {

        binding.apply {
            bottomNav.background = null
            bottomNav.menu.getItem(2).isEnabled = false

            // Event when click in ItemMenu
            bottomNav.selectedItemId = (R.id.categories) // Set ItemMenu default when open app
            bottomNav.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener {
                when (it.itemId) {
                    R.id.accounts -> {

                        if (MainVMD.IdBottomNav != 0) {
                            MainVMD.IdBottomNav = 0
                            MainVMD.CheckIdBottomNav.value = MainVMD.IdBottomNav
                        }
                    }
                    R.id.categories -> {

                        if (MainVMD.IdBottomNav != 1) {
                            MainVMD.IdBottomNav = 1
                            MainVMD.CheckIdBottomNav.value = MainVMD.IdBottomNav
                        }

                    }
                    R.id.history -> {
                        if (MainVMD.IdBottomNav != 2) {
                            MainVMD.IdBottomNav = 2
                            MainVMD.CheckIdBottomNav.value = MainVMD.IdBottomNav
                        }

                    }
                    R.id.statisticals -> {
                        if (MainVMD.IdBottomNav != 3) {
                            MainVMD.IdBottomNav = 3
                            MainVMD.CheckIdBottomNav.value = MainVMD.IdBottomNav
                        }
                    }
                }
                true
            })

            // Get ID Of BottomNavigationVIEW form LiveDate ON Viewmodel
            MainVMD.CheckIdBottomNav.observe(this@MainActivity, Observer { Id ->

                when (Id) {
                    0 -> {
                        setUpFragment(AccountsFragment())
                        viewDate.visibility = View.GONE
                        // --- icon
                        btnAddAcounts.visibility = View.VISIBLE
                        btnEditCategories.visibility = View.GONE
                        btnSearchHistory.visibility = View.GONE
                        // --- tabLayout
                        tabLayoutToolbar.visibility = View.VISIBLE
                    }
                    1 -> {
                        setUpFragment(CategoriesVPGHomeFragement())
                        viewDate.visibility = View.VISIBLE
                        // --- icon
                        btnAddAcounts.visibility = View.GONE
                        btnEditCategories.visibility = View.VISIBLE
                        btnSearchHistory.visibility = View.GONE
                        //-- tabLayout
                        tabLayoutToolbar.visibility = View.GONE
                    }
                    2 -> {
                        setUpFragment(TransactionVPGHomeFragement())
                        viewDate.visibility = View.VISIBLE
                        // --- icon
                        btnAddAcounts.visibility = View.GONE
                        btnEditCategories.visibility = View.GONE
                        btnSearchHistory.visibility = View.VISIBLE
                        // --- tabLayout
                        tabLayoutToolbar.visibility = View.GONE

                    }
                    3 -> {
                        setUpFragment(StatisticalsVPGHomeFragement())
                        viewDate.visibility = View.VISIBLE
                        // --- icon
                        btnAddAcounts.visibility = View.GONE
                        btnEditCategories.visibility = View.GONE
                        btnSearchHistory.visibility = View.GONE
                        // --- tabLayout
                        tabLayoutToolbar.visibility = View.GONE
                    }
                }
            })
        }
    }

    // Function SetUp Fragment
    private fun setUpFragment(fragment: Fragment) = lifecycleScope.launch(Dispatchers.Main) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentActivity, fragment).commit()
    }


    // Function SetUp DataStoew Preferences AND Viewmodel
    private fun setUpViewModelandDataStore() {
        val viewmodelFactory = ViewModelFactory(application)
        MainVMD = ViewModelProvider(this, viewmodelFactory).get(MainViewModel::class.java)
        AccountsViewModel = ViewModelProvider(this).get(AccountsViewmodel::class.java)
        CategoriesViewModel = ViewModelProvider(this).get(CategoriesViewmodel::class.java)
        dataStore = createDataStore(name = "FirstInstalledApp")
    }


}