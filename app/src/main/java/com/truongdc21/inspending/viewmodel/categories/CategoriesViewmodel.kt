package com.truongdc21.inspending.viewmodel.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.truongdc21.inspending.R
import com.truongdc21.inspending.database.MyDatabase
import com.truongdc21.inspending.model.Categories
import com.truongdc21.inspending.repository.CategoriesRepository
import com.truongdc21.inspending.util.KeyWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class   CategoriesViewmodel(application: Application) : AndroidViewModel(application) {

    // Get listCategories from database {
    val repository: CategoriesRepository
    val getListCategories: LiveData<List<Categories>>

    init {
        val mCategories = MyDatabase.getMyDatabase(application).mCategories()
        repository = CategoriesRepository(mCategories)
        getListCategories = repository.getListCategories
    }
    //  --- }

    fun addCategories(categories: Categories) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCategories(categories)
        }
    }


    // List All Cat
    fun ListAllCategoriesIncome(): ArrayList<String> {
        val mListAllCategoriesIncome = arrayListOf<String>()
        mListAllCategoriesIncome.add(KeyWord.thuNhapCoBan)
        mListAllCategoriesIncome.add(KeyWord.no_thu)
        mListAllCategoriesIncome.add(KeyWord.thuKhac)
        return mListAllCategoriesIncome
    }

    fun ListAllCategoriesExpenses(): ArrayList<String> {
        val mListAllCategoriesExpenses = arrayListOf<String>()
        mListAllCategoriesExpenses.add(KeyWord.canthiet)
        mListAllCategoriesExpenses.add(KeyWord.hangthang)
        mListAllCategoriesExpenses.add(KeyWord.no_chi)
        mListAllCategoriesExpenses.add(KeyWord.chiKhac)
        return mListAllCategoriesExpenses
    }

    fun ListCategoriesOnfix (mListCategories : List<Categories> , Key : String): ArrayList<Categories>{
        val mListCategoriesOnFix = ArrayList<Categories>()
        for (i in mListCategories){
            if (i.typeCategories == Key){
                mListCategoriesOnFix.add(i)
            }
        }
        return mListCategoriesOnFix
    }

    fun getPercentInCome(Income: Long, Expenses: Long): Float {
        val allMonney = Income + Expenses
        allMonney.toFloat()
        var f1 = 0
        if (allMonney < 1) {
            return 0.toFloat()
        }
        f1 = (allMonney / 100).toInt()

        val f2 = Income / f1
        return f2.toFloat()
    }

    fun getPercentExpenses(Income: Long, Expenses: Long): Float {
        val allMonney = Income + Expenses
        allMonney.toFloat()
        var f1 = 0
        if (allMonney < 1) {
            return 0.toFloat()
        }
        f1 = (allMonney / 100).toInt()

        val f2 = Expenses / f1
        return f2.toFloat()
    }




    fun addFirtInstalleCategoriesExpenses (){
        val Categories1 = Categories(1, R.drawable.ic_categories_07, "Ăn uống", 0L,
            KeyWord.Expenses,
            KeyWord.canthiet
        )
        val Categories2 = Categories(2, R.drawable.ic_categories_xe, "Đi lại", 0L,
            KeyWord.Expenses,
            KeyWord.canthiet
        )
        val Categories3 = Categories(3, R.drawable.ic_categories_suc_khoe, "Sức khỏe", 0L,
            KeyWord.Expenses,
            KeyWord.canthiet
        )
        val Categories4 = Categories(4, R.drawable.ic_categories_giaitri, "Giải trí", 0L,
            KeyWord.Expenses,
            KeyWord.canthiet
        )
        val Categories5= Categories(5, R.drawable.ic_categories_hoctap, "Học tập", 0L,
            KeyWord.Expenses,
            KeyWord.canthiet
        )
        val Categories6 = Categories(6, R.drawable.ic_categories_baotri, "Sữa chữa nhà", 0L,
            KeyWord.Expenses,
            KeyWord.canthiet
        )

        val Categories7 = Categories(7, R.drawable.ic_categories_vatnuoi, "Vật nuôi", 0L,
            KeyWord.Expenses,
            KeyWord.canthiet
        )
        addCategories(Categories1)
        addCategories(Categories2)
        addCategories(Categories3)
        addCategories(Categories4)
        addCategories(Categories5)
        addCategories(Categories6)
        addCategories(Categories7)

    }

    fun addFirtInstalledCategoriesExpensesMonths (){
        val Categories1 = Categories(8, R.drawable.ic_categories_01, "Tiền nước", 0L,
            KeyWord.Expenses,
            KeyWord.hangthang
        )
        val Categories2 = Categories(9, R.drawable.ic_categories_02, "Tiền điện", 0L,
            KeyWord.Expenses,
            KeyWord.hangthang
        )
        val Categories3 = Categories(10, R.drawable.ic_categories_03, "Tiền mạng", 0L,
            KeyWord.Expenses,
            KeyWord.hangthang
        )
        val Categories4 = Categories(11, R.drawable.ic_categories_04, "Chi phí TV", 0L,
            KeyWord.Expenses,
            KeyWord.hangthang
        )
        val Categories5 = Categories(12, R.drawable.ic_categories_05, "Tiền gas", 0L,
            KeyWord.Expenses,
            KeyWord.hangthang
        )
        val Categories6 = Categories(13, R.drawable.ic_categories_06, "Thuê Nhà", 0L,
            KeyWord.Expenses,
            KeyWord.hangthang
        )

        val Categories7 = Categories(14, R.drawable.ic_categories_10, "Tiền điện thoại ", 0L,
            KeyWord.Expenses,
            KeyWord.hangthang
        )
        val Categories8 = Categories(15, R.drawable.ic_categories_hoadon, "Các hóa đơn khác", 0L,
            KeyWord.Expenses,
            KeyWord.hangthang
        )

        addCategories(Categories1)
        addCategories(Categories2)
        addCategories(Categories3)
        addCategories(Categories4)
        addCategories(Categories5)
        addCategories(Categories6)
        addCategories(Categories7)
        addCategories(Categories8)


    }

    fun addFirtInstalledCategoriesExpensesVAYNOANDKHAC (){
        val Categories1 = Categories(16, R.drawable.ic_categories_vayno, "Cho Vay", 0L,
            KeyWord.Expenses,
            KeyWord.no_chi
        )
        val Categories2 = Categories(17, R.drawable.ic_categories_khac, "Khác", 0L,
            KeyWord.Expenses,
            KeyWord.chiKhac
        )
        addCategories(Categories1)
        addCategories(Categories2)


    }

    fun addFirtInstalledCategoriesIncome (){
        val Categories1 = Categories(18, R.drawable.ic_categories_luong, "Tiền lương", 0L,
            KeyWord.Income,
            KeyWord.thuNhapCoBan
        )
        val Categories2 = Categories(19, R.drawable.ic_categories_thuong, "Tiền thưởng", 0L,
            KeyWord.Income,
            KeyWord.thuNhapCoBan
        )

        val Categories4 = Categories(20, R.drawable.ic_categories_lai, "Tiền lãi", 0L,
            KeyWord.Income,
            KeyWord.thuNhapCoBan
        )

        val Categories5 = Categories(21, R.drawable.ic_categories_vayno, "Đi vay", 0L,
            KeyWord.Income,
            KeyWord.no_thu
        )
        val Categories6 = Categories(22, R.drawable.ic_categories_khac, "Khác", 0L,
            KeyWord.Income,
            KeyWord.thuKhac
        )
        addCategories(Categories1)
        addCategories(Categories2)
        addCategories(Categories4)
        addCategories(Categories5)
        addCategories(Categories6)

    }



}

