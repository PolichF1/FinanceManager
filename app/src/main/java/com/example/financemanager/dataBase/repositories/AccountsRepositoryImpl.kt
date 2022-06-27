package com.example.financemanager.dataBase.repositories

import com.example.financemanager.data.models.Account
import com.example.financemanager.data.repositories.AccountsRepository
import com.example.financemanager.dataBase.dao.AccountsDao
import kotlinx.coroutines.flow.Flow

class AccountsRepositoryImpl(
    private val dao: AccountsDao
): AccountsRepository {

    override fun getAccounts(): Flow<List<Account>> {
        return dao.getAccounts()
    }


    override suspend fun insertAccount(account: Account) {
        return dao.insertAccount(account)
    }

    override suspend fun updateAccount(account: Account) {
        return dao.updateAccount(account)
    }

    override suspend fun updateAccountAmountById(id: Int, amount: Double) {
        dao.updateAccountAmountById(id, amount)
    }

    override suspend fun deleteAccount(account: Account) {
        return dao.deleteAccount(account)
    }
}