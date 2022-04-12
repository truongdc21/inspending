package com.truongdc21.inspending.model

class SubCategory(
    val Id : Int,
    val imgSubCategory : Int,
    val nameSubCategory : String,
    val moneySubCategory : Long,
    val typeCategory : Int // số Id của của Danh Mục Chính
)