package com.example.financemanager.di.modules

import com.example.financemanager.data.repositories.AccountsRepository
import com.example.financemanager.data.useCases.*
import com.example.financemanager.dataBase.AppDataBase
import com.example.financemanager.dataBase.repositories.AccountsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AccountsModule {

    @Provides
    @Singleton
    fun provideAccountsRepository(db: AppDataBase): AccountsRepository =
        AccountsRepositoryImpl(db.accountsDao)

    @Provides
    @Singleton
    fun provideGetAccountsUseCase(repository: AccountsRepository): GetAccountsUseCase =
        GetAccountsUseCase(repository)

    @Provides
    @Singleton
    fun provideAddAccountUseCase(repository: AccountsRepository): AddAccountUseCase =
        AddAccountUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateAccountUseCase(repository: AccountsRepository): UpdateAccountUseCase =
        UpdateAccountUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteAccountUseCase(repository: AccountsRepository): DeleteAccountUseCase =
        DeleteAccountUseCase(repository)
}