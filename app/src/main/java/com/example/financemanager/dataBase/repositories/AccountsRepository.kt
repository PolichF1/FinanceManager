package com.example.financemanager.dataBase.repositories

import com.example.financemanager.dataBase.models.Account
import kotlinx.coroutines.flow.Flow

interface AccountsRepository {

    fun getAccounts(): Flow<List<Account>>

    suspend fun getAccountById(id: Int): Account?

    suspend fun insertAccount(account: Account)

    suspend fun deleteAccount(account: Account)
}