package com.truongdc21.inspending.viewmodel

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.truongdc21.inspending.MainActivity
import com.truongdc21.inspending.database.MyDatabase
import com.truongdc21.inspending.model.DateTime
import com.truongdc21.inspending.repository.DateTimeRepository
import com.truongdc21.inspending.util.Date
import com.truongdc21.inspending.viewmodel.accounts.AccountsViewmodel
import com.truongdc21.inspending.viewmodel.categories.CategoriesViewmodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var IdBottomNav = 1
    val CheckIdBottomNav = MutableLiveData<Int>()

    val CheckIdAccounts = MutableLiveData<Int>()

    val CheckDateLiveData = MutableLiveData<LocalDate>()

    // Date Time
    val getDateTime: LiveData<List<DateTime>>
    val repositoryDateTime: DateTimeRepository


    init {
        CheckIdBottomNav.value = IdBottomNav
        CheckDateLiveData.value = Date.mDate

        // Date Time
        val mDateTime = MyDatabase.getMyDatabase(application).mDateTime()
        repositoryDateTime = DateTimeRepository(mDateTime)
        getDateTime = repositoryDateTime.getDatetime


    }

    // Add DateTime Firts AND End to database ROOM
    fun addDateTime(dateTime: DateTime) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryDateTime.addDateTime(dateTime)
        }
    }

    // Update DateTime firts AND End to databse ROOM
    fun updateDateTime(dateTime: DateTime) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryDateTime.updateDateTime(dateTime)
        }
    }

    // Formatter Date Days to String of Toolbar on Main Activity
    fun formatterDateToolbar(Date: LocalDate): String {
        val formatted = DateTimeFormatter.ofPattern("E - dd  MMMM  yyyy")
        return Date.format(formatted)
    }

    // Formatter Date Days Weeek to String of Toolbar on Main Activity
    fun formatterDateWeeks(Date: LocalDate): String {
        val monday = Date.with(DayOfWeek.MONDAY)
        val sunday = Date.with(DayOfWeek.SUNDAY)
        var allday = ""

        val formattedMonday = monday.format(DateTimeFormatter.ofPattern("dd"))
        val formattedSunday = sunday.format(DateTimeFormatter.ofPattern("dd  MMMM  yyyy"))
        if (monday.month == sunday.month) {
            allday = "${formattedMonday} - ${formattedSunday}"
        } else {
            val formattedMonday1 = monday.format(DateTimeFormatter.ofPattern("dd  MMM"))
            val formattedSunday1 = sunday.format(DateTimeFormatter.ofPattern("dd  MMM  yyyy"))
            allday = "${formattedMonday1} - ${formattedSunday1}"
        }

        return allday
    }

    // Formatter Date Months to String of Toolbar on Main Activity
    fun formatterDateMonths(Date: LocalDate): String {
        val formatted = DateTimeFormatter.ofPattern("MMMM  yyyy")
        return Date.format(formatted)
    }

    // Formatter Date Years to String of Toolbar on Main Activity
    fun formatterDateYears(Date: LocalDate): String {
        val formatted = DateTimeFormatter.ofPattern("yyyy")
        return Date.format(formatted)
    }

    // Formatter Date Range to String of Toolbar on Main Activity
    fun formatterDateRange(firstDate: String, endDate: String): String {
        val first = LocalDate.parse(firstDate)
        val end = LocalDate.parse(endDate)
        var firstEnd = ""

        if (first.month == end.month) {
            firstEnd = "${first.format(DateTimeFormatter.ofPattern("dd"))} - ${
                end.format(
                    DateTimeFormatter.ofPattern("dd  MMM  yyyy")
                )
            }"
        } else if (first.month != end.month) {
            firstEnd = "${first.format(DateTimeFormatter.ofPattern("dd MMM"))} - ${
                end.format(DateTimeFormatter.ofPattern("dd  MMM  yyyy"))
            }"
        } else if (first.year != end.year) {
            firstEnd = "${first.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))} - ${
                end.format(DateTimeFormatter.ofPattern("dd  MMM  yyyy"))
            }"
        }
        return firstEnd
    }

    // Back Days
    fun previousDays() {
        viewModelScope.launch {
            Date.mDate = Date.mDate!!.minusDays(1)
            CheckDateLiveData.value = Date.mDate
        }
    }

    // Next Days
    fun nextDays() {
        viewModelScope.launch {
            Date.mDate = Date.mDate!!.plusDays(1)
            CheckDateLiveData.value = Date.mDate
        }
    }

    // Back Weeks
    fun previousWeeks() {
        viewModelScope.launch {
            Date.mDate = Date.mDate!!.minusWeeks(1)
            CheckDateLiveData.value = Date.mDate
        }
    }

    // Next Week
    fun nextWeeks() {
        viewModelScope.launch {
            Date.mDate = Date.mDate!!.plusWeeks(1)
            CheckDateLiveData.value = Date.mDate
        }
    }

    // Back Months
    fun previousMonths() {
        viewModelScope.launch {
            Date.mDate = Date.mDate!!.minusMonths(1)
            CheckDateLiveData.value = Date.mDate
        }
    }

    // Next Months
    fun nextMonths() {
        viewModelScope.launch {
            Date.mDate = Date.mDate!!.plusMonths(1)
            CheckDateLiveData.value = Date.mDate
        }
    }

    // Back Years
    fun previousYears() {
        viewModelScope.launch {
            Date.mDate = Date.mDate!!.minusYears(1)
            CheckDateLiveData.value = Date.mDate
        }
    }

    // Next Years
    fun nextYears() {
        viewModelScope.launch {
            Date.mDate = Date.mDate!!.plusYears(1)
            CheckDateLiveData.value = Date.mDate
        }
    }

    // Back Range Date
    fun previousRangeDate(mList: List<DateTime>) = viewModelScope.launch {

        var firstDate: String? = null
        var endDate: String? = null

        for (i in mList) {
            firstDate = i.StartDateTime
            endDate = i.EndDateTime
        }

        val first = Date.RangeDateFirst
        var end = Date.RangeDateEnd

        var size = 0

        while (end >= first) {
            size++
            end = end.minusDays(1)
        }
        Date.RangeDateFirst = first.minusDays(size.toLong())
        Date.RangeDateEnd = end
        Date.mDate = Date.RangeDateEnd
        CheckDateLiveData.value = Date.mDate
    }

    // Next Range Date
    fun nextRangeDate(mList: List<DateTime>) = viewModelScope.launch {
        var firstDate: String? = null
        var endDate: String? = null

        for (i in mList) {
            firstDate = i.StartDateTime
            endDate = i.EndDateTime
        }
        var first = Date.RangeDateFirst
        val end = Date.RangeDateEnd

        var next = Date.RangeDateEnd
        var size = 0

        while (first <= end) {
            size++
            first = first.plusDays(1)
        }

        next = next.plusDays(size.toLong())

        Date.RangeDateFirst = end.plusDays(1)
        Date.RangeDateEnd = next
        Date.mDate = Date.RangeDateEnd
        CheckDateLiveData.value = Date.mDate

    }

    // Event when U Click Button Back on toolbar AND Scroll viewpager to left
    fun PreviousDate(list: List<DateTime>) = viewModelScope.launch {
        for (i in list) {
            when (i.TypeDateTime) {
                1 -> {
                    previousDays()
                }
                2 -> {
                    previousWeeks()
                }
                3 -> {
                    previousMonths()
                }
                4 -> {
                    previousYears()
                }
                5 -> {
                    previousDays()
                }
                6 -> {
                    previousRangeDate(list)
                }
                7 -> {
                }
            }
        }
    }

    // Event when U Click button next AND Scroll viewpager to right
    fun NextDate(list: List<DateTime>) = viewModelScope.launch {
        for (i in list) {
            when (i.TypeDateTime) {
                1 -> {
                    nextDays()
                }
                2 -> {
                    nextWeeks()
                }
                3 -> {
                    nextMonths()
                }
                4 -> {
                    nextYears()
                }
                5 -> {
                    nextDays()
                }
                6 -> {
                    nextRangeDate(list)
                }
                7 -> {
                }
            }
        }
    }


    // Data Store Preferencts set data  Check Firs Installed App
    suspend fun saveDataFirstInstalledApp(
        dataStore: DataStore<Preferences>,
        key: String,
        value: Boolean
    ) {
        val dataStoreKey = preferencesKey<Boolean>(key)
        dataStore.edit { data ->
            data[dataStoreKey] = value
        }
    }

    // Data Store Preferncts get Data Check Firs Installed App
    suspend fun readDataFirstInstaledApp(dataStore: DataStore<Preferences>, key: String): Boolean? {
        val dataStoreKey = preferencesKey<Boolean>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    // Data Store set Data Choose Accounts
    suspend fun saveDataAutoChooseAccounts(
        dataStore: DataStore<Preferences>,
        key: String,
        value: Int
    ) {
        val dataStoreKey = preferencesKey<Int>(key)
        dataStore.edit { data ->
            data[dataStoreKey] = value
        }
    }

    // Data Store get Data Choose Accounts
    suspend fun readDataAutoChoiseAccounts(dataStore: DataStore<Preferences>, key: String): Int? {
        val dataStoreKey = preferencesKey<Int>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    // Check Fird InStalled Apps on Main Activity
    fun CheckFirstInstalledApp(
        dataStore: DataStore<Preferences>,
        MainVMD: MainViewModel,
        AccountsViewModel: AccountsViewmodel,
        CategoriesViewModel: CategoriesViewmodel
    ) {

        viewModelScope.launch {
            val value =
                MainVMD.readDataFirstInstaledApp(dataStore, MainActivity.FIRST_INSTALLED_APP)
            if (value == null) {
                AccountsViewModel.addFirstAccounts()
                //CategoriesViewModel.addFirtCategories()
                CategoriesViewModel.addFirtInstalleCategoriesExpenses()
                CategoriesViewModel.addFirtInstalledCategoriesExpensesMonths()
                CategoriesViewModel.addFirtInstalledCategoriesExpensesVAYNOANDKHAC()
                CategoriesViewModel.addFirtInstalledCategoriesIncome()
                val datetime = DateTime(1, 1, "", "")
                MainVMD.addDateTime(datetime)
                MainVMD.saveDataAutoChooseAccounts(
                    dataStore,
                    MainActivity.CHOISE_ACCOUNTS_BOTTOM_SHEET,
                    0
                )

            }
            MainVMD.saveDataFirstInstalledApp(dataStore, MainActivity.FIRST_INSTALLED_APP, true)
        }
    }


}