package com.example.financemanager.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.financemanager.dataBase.dao.AccountsDao
import com.example.financemanager.dataBase.dao.CategoriesDao
import com.example.financemanager.dataBase.dao.TransactionsDao
import com.example.financemanager.dataBase.models.Account
import dagger.hilt.android.qualifiers.ApplicationContext

@Database(entities = [Account::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract val accountsDao: AccountsDao
    abstract val categoriesDao: CategoriesDao
    abstract val transactionsDao: TransactionsDao



    companion object {
        lateinit var instance: AppDataBase
        fun init(applicationContext: Context) {
            instance = Room
                .databaseBuilder(applicationContext, AppDataBase::class.java, "db")
                .setJournalMode(JournalMode.TRUNCATE)
                .build()
        }
    }
}