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
    fun provideAccountsRepository(db: AppDataBase): AccountsRepository {
        return AccountsRepositoryImpl(db.accountsDao)
    }

    @Provides
    @Singleton
    fun providesAccountsUseCases(repository: AccountsRepository): AccountsUseCases {
        return AccountsUseCases(
            getAccounts = GetAccounts(repository),
            addAccount = AddAccount(repository),
            updateAccount = UpdateAccount(repository),
            deleteAccount = DeleteAccount(repository),
        )
    }

}