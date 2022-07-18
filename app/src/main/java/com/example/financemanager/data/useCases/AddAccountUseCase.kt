package com.example.financemanager.data.useCases

import com.example.financemanager.data.models.Account
import com.example.financemanager.data.repositories.AccountsRepository

class AddAccountUseCase(private val repository: AccountsRepository) {

    suspend operator fun invoke(account: Account) {
        repository.insertAccount(account)
    }

}