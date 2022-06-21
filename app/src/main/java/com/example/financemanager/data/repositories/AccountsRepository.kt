package com.example.financemanager.data.repositories

import com.example.financemanager.data.models.Account
import kotlinx.coroutines.flow.Flow

interface AccountsRepository {

    fun getAccounts(): Flow<List<Account>>

    suspend fun getAccountById(id: Int): Account?

    suspend fun insertAccount(account: Account)

    suspend fun updateAccount(account: Account)

    suspend fun deleteAccount(account: Account)
}