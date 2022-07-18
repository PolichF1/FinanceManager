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
    fun provideTransactionsRepository(db: AppDataBase): TransactionsRepository =
        TransactionsRepositoryImpl(db.transactionsDao)

    @Provides
    @Singleton
    fun provideGetTransactionViewsUserCase(transactionsRepository: TransactionsRepository): GetTransactionViewsUseCase =
        GetTransactionViewsUseCase(transactionsRepository)

    @Provides
    @Singleton
    fun provideGetTransactionListWithDayInfoUseCase(transactionsRepository: TransactionsRepository): GetTransactionsWithDayInfoUseCase =
        GetTransactionsWithDayInfoUseCase(transactionsRepository)

    @Provides
    @Singleton
    fun provideAddTransactionUseCase(
        transactionsRepository: TransactionsRepository,
        accountsRepository: AccountsRepository
    ): AddTransactionUseCase = AddTransactionUseCase(transactionsRepository, accountsRepository)

    @Provides
    @Singleton
    fun provideDeleteTransactionByIdUseCase(
        transactionsRepository: TransactionsRepository,
        accountsRepository: AccountsRepository
    ): DeleteTransactionByIdUseCase = DeleteTransactionByIdUseCase(transactionsRepository, accountsRepository)
}