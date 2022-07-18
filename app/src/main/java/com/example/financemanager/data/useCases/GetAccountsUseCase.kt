package com.example.financemanager.data.useCases

import com.example.financemanager.data.models.Account
import com.example.financemanager.data.repositories.AccountsRepository
import kotlinx.coroutines.flow.Flow


class GetAccountsUseCase(private val repository: AccountsRepository) {

    operator fun invoke(): Flow<List<Account>>  {
        return repository.getAccounts()
    }

}