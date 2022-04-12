package com.truongdc21.inspending.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "date_time_table")
 data class DateTime (
    @PrimaryKey(autoGenerate = false)
    val Id : Int,
    val TypeDateTime : Int,
    val StartDateTime : String,
    val EndDateTime : String

)