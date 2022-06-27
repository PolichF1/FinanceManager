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
    fun providesTransactionsRepository(db: AppDataBase): TransactionsRepository {
        return TransactionsRepositoryImpl(db.transactionsDao)
    }

    @Provides
    @Singleton
    fun providesTransactionsUseCases(
        transactionsRepository: TransactionsRepository,
        accountsRepository: AccountsRepository
    ): TransactionUseCases {
        return TransactionUseCases(
            getTransactionViews = GetTransactionViews(transactionsRepository),
            addTransaction = AddTransaction(transactionsRepository, accountsRepository),
            getTransactionListWithDayInfo = GetTransactionListWithAmountsPerDay(transactionsRepository),
            deleteTransactionById = DeleteTransactionById(transactionsRepository, accountsRepository)
        )
    }

}