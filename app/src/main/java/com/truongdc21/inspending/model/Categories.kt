package com.truongdc21.inspending.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories_table")
open class Categories(
    @PrimaryKey(autoGenerate = true)
    val Id : Int,
    val imgCategories : Int,
    val NameCategories : String,
    val MonneyCategories: Long,
    val typeCategories: String?,
    val typeCategoriesChild : String?

)