package com.example.financemanager.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import com.example.financemanager.data.repositories.AccountsRepository
import com.example.financemanager.data.repositories.CategoriesRepository
import com.example.financemanager.data.repositories.TransactionsRepository
import com.example.financemanager.data.useCases.*
import com.example.financemanager.dataBase.AppDataBase
import com.example.financemanager.dataBase.repositories.AccountsRepositoryImpl
import com.example.financemanager.dataBase.repositories.CategoriesRepositoryImpl
import com.example.financemanager.dataBase.repositories.TransactionsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppContext(app: Application): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideDataBase(app: Application): AppDataBase {
        return Room.databaseBuilder(
            app.applicationContext,
            AppDataBase::class.java,
            AppDataBase.APP_DATABASE_NAME
        ).fallbackToDestructiveMigration().createFromAsset("database/finance_manager.db").build()
    }

    @Provides
    @Singleton
    fun provideAccountsRepository(db: AppDataBase): AccountsRepository {
        return AccountsRepositoryImpl(db.accountsDao)
    }

    @Provides
    @Singleton
    fun providesCategoriesRepository(db: AppDataBase): CategoriesRepository {
        return CategoriesRepositoryImpl(db.categoriesDao)
    }

    @Provides
    @Singleton
    fun providesTransactionsRepository(db: AppDataBase): TransactionsRepository {
        return TransactionsRepositoryImpl(db.transactionsDao)
    }

    @Provides
    @Singleton
    fun providesAccountsUseCases(repository: AccountsRepository):AccountsUseCases {
        return AccountsUseCases(
            getAccount = GetAccount(repository),
            getAccounts = GetAccounts(repository),
            addAccount = AddAccount(repository),
            updateAccount = UpdateAccount(repository),
            deleteAccount = DeleteAccount(repository),
        )
    }

    @Provides
    @Singleton
    fun providesCategoriesUseCases(repository: CategoriesRepository): CategoryUseCases {
        return CategoryUseCases(
            getCategory = GetCategory(repository),
            getCategories = GetCategories(repository),
            addCategory = AddCategory(repository),
            updateCategory = UpdateCategory(repository),
            deleteCategory = DeleteCategory(repository)
        )
    }

    @Provides
    @Singleton
    fun providesTransactionsUseCases(repository: TransactionsRepository): TransactionUseCases {
        return TransactionUseCases(
            getTransaction = GetTransaction(repository),
            getTransactions = GetTransactions(repository),
            addTransaction = AddTransaction(repository),
            updateTransaction = UpdateTransaction(repository),
            deleteTransaction = DeleteTransaction(repository),
            getTransactionListForRecyclerView = GetTransactionListFotRecyclerView(repository)
        )
    }


}