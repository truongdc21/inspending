package com.truongdc21.inspending.repository

import androidx.lifecycle.LiveData
import com.truongdc21.inspending.database.CategoriesDao
import com.truongdc21.inspending.model.Categories

class CategoriesRepository (private val categoriesDao: CategoriesDao) {

    val getListCategories : LiveData<List<Categories>> = categoriesDao.getListCategories()

    suspend fun addCategories (categories: Categories){
        categoriesDao.addCategories(categories)
    }
}