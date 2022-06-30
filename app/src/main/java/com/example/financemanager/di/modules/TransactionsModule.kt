package com.example.financemanager.di.modules

import com.example.financemanager.data.repositories.AccountsRepository
import com.example.financemanager.data.repositories.TransactionsRepository
import com.example.financemanager.data.useCases.*
import com.example.financemanager.dataBase.AppDataBase
import com.example.financemanager.dataBase.repositories.TransactionsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TransactionsModule {

    @Provides
    @Singleton
    fun provideTransactionsRepository(db: AppDataBase): TransactionsRepository {
        return TransactionsRepositoryImpl(db.transactionsDao)
    }

    @Provides
    @Singleton
    fun provideTransactionsUseCases(
        transactionsRepository: TransactionsRepository,
        accountsRepository: AccountsRepository
    ): TransactionUseCases {
        return TransactionUseCases(
            getTransactionViews = GetTransactionViews(transactionsRepository),
            addTransaction = AddTransaction(transactionsRepository, accountsRepository),
            deleteTransactionById = DeleteTransactionById(transactionsRepository, accountsRepository),
            getTransactionListWithDayInfo = GetTransactionListWithAmountsPerDay(transactionsRepository)
        )
    }
}