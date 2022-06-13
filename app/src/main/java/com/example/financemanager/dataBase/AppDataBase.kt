package com.example.financemanager.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.financemanager.dataBase.dao.AccountsDao
import com.example.financemanager.dataBase.dao.CategoriesDao
import com.example.financemanager.dataBase.dao.TransactionsDao
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.models.Category
import com.example.financemanager.data.models.Transaction

@Database(entities = [Account::class, Category::class, Transaction::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract val accountsDao: AccountsDao
    abstract val categoriesDao: CategoriesDao
    abstract val transactionsDao: TransactionsDao



    companion object {
        const val APP_DATABASE_NAME = "finance_manager_db"
    }
}