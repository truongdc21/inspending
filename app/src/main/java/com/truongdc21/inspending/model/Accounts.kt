package com.truongdc21.inspending.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts_table")
open class Accounts(
    @PrimaryKey(autoGenerate = true)
    val Id : Int,
    val img : Int,
    val Name : String,
    val monney : Long,
    val firtBalance : Long,
    val type : Int,
    val currency : String,
    val description: String
)
