package com.truongdc21.inspending.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
class History (
   @PrimaryKey(autoGenerate = true)
   val Id : Int,
   val Accounts : Int,
   val Categories : Int,
   val monney : Long,
   val Date : String,
   val DateNow : String?,
   val TimeNow : String?,
   val Description : String?,


)
