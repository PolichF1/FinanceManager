package com.example.financemanager.di.modules

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