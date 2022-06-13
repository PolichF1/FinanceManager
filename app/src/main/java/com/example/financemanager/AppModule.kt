package com.example.financemanager

import android.app.Application
import androidx.room.Room
import com.example.financemanager.dataBase.AppDataBase
import com.example.financemanager.data.repositories.AccountsRepository
import com.example.financemanager.data.useCases.*
import com.example.financemanager.dataBase.repositories.AccountsRepositoryImpl
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
    fun provideAccountsDatabase (app: Application) : AppDataBase {
        return Room.databaseBuilder(
            app.applicationContext,
            AppDataBase::class.java,
            AppDataBase.APP_DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideAccountsRepository(db: AppDataBase): AccountsRepository {
        return AccountsRepositoryImpl(db.accountsDao)
    }


    @Provides
    @Singleton
    fun provideAccountsUseCases(repository: AccountsRepository) : AccountsUseCases? {

        return AccountsUseCases(
            getAccounts = GetAccounts(repository),
            getAccount = GetAccount(repository),
            deleteAccount = DeleteAccount(repository),
            addAccount = AddAccount(repository),
            updateAccount = UpdateAccount(repository)
        )
    }
}