package com.truongdc21.inspending.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.truongdc21.inspending.model.Accounts
import com.truongdc21.inspending.model.Categories
import com.truongdc21.inspending.model.DateTime
import com.truongdc21.inspending.model.History

@Database(
    entities = [History::class , Accounts::class , Categories::class , DateTime::class] ,
    version = 5,
    exportSchema = false,
    )
abstract class MyDatabase : RoomDatabase(){

    abstract fun mHistoryDao () : HistoryDao
    abstract fun mAccountsDao () : AccountsDao
    abstract fun mCategories () : CategoriesDao
    abstract fun mDateTime () : DateTimeDao

    companion object {
        @Volatile
        private var INSTANCE : MyDatabase? =null

            fun getMyDatabase (context: Context): MyDatabase{
                val tempInstance = INSTANCE
                if (tempInstance  != null){
                    return tempInstance
                }
                synchronized(this){
                    val instance = Room.databaseBuilder(context.applicationContext , MyDatabase::class.java , "user_db")
                        .allowMainThreadQueries()
                        .addMigrations(
                            MIGRATION_1_2,
                            MIGRATION_2_3 ,
                            MIGRATION_3_4 ,
                            MIGRATION_4_5)
                        .build()
                    INSTANCE = instance
                    return instance
                }
            }
        }

    }
    val MIGRATION_4_5 = object : Migration(4,5){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE 'history_table' ADD COLUMN 'DateNow' TEXT")
            database.execSQL("ALTER TABLE 'history_table' ADD COLUMN 'TimeNow' TEXT")
            database.execSQL("ALTER TABLE 'history_table' ADD COLUMN 'Description' TEXT")

        }

    }
    val MIGRATION_3_4 = object : Migration(3,4){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE 'accounts_table' ADD COLUMN 'firtBalance'  LONG NOT NULL DEFAULT 0 ")
        }

    }
    val MIGRATION_2_3 = object : Migration(2,3){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE 'categories_table' ADD COLUMN 'typeCategoriesChild' TEXT ")
        }

    }
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE 'categories_table' ADD COLUMN 'typeCategories' TEXT ")
        }

}