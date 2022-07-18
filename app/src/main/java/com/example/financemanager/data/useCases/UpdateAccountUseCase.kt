package com.example.financemanager.data.useCases

import com.example.financemanager.data.models.Account
import com.example.financemanager.data.repositories.AccountsRepository

class UpdateAccountUseCase(private val repository: AccountsRepository) {

    suspend operator fun invoke(account: Account) {
        repository.updateAccount(account)
    }
}