package com.example.financemanager.data.repositories

import com.example.financemanager.data.models.Account
import kotlinx.coroutines.flow.Flow

interface AccountsRepository {

    fun getAccounts(): Flow<List<Account>>

    suspend fun insertAccount(account: Account)

    suspend fun updateAccount(account: Account)

    suspend fun updateAccountAmountById(id: Int, amount: Double)

    suspend fun deleteAccount(account: Account)
}