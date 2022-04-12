package com.truongdc21.inspending.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.truongdc21.inspending.model.Accounts
import com.truongdc21.inspending.model.Categories

@Dao
interface CategoriesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCategories (categories: Categories)

    @Update
    suspend fun updateCategories(categories: Categories)

    @Delete
    suspend fun deleteCategories (categories: Categories)

    @Query("SELECT * FROM categories_table ORDER BY Id ASC")
    fun getListCategories () : LiveData<List<Categories>>
}