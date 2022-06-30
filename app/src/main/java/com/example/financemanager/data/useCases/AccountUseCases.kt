package com.example.financemanager.data.useCases

import com.example.financemanager.data.models.Account
import com.example.financemanager.data.repositories.AccountsRepository
import kotlinx.coroutines.flow.Flow

data class AccountUseCases(
    val getAccounts: GetAccounts,
    val addAccount: AddAccount,
    val updateAccount: UpdateAccount,
    val deleteAccount: DeleteAccount,
)

class GetAccounts(private val repository: AccountsRepository) {
    operator fun invoke(): Flow<List<Account>> {
        return repository.getAccounts()
    }
}

class AddAccount(private val repository: AccountsRepository) {
    suspend operator fun invoke(account: Account) {
        repository.insertAccount(account)
    }
}

class UpdateAccount(private val repository: AccountsRepository) {
    suspend operator fun invoke(account: Account) {
        repository.updateAccount(account)
    }
}

class DeleteAccount(private val repository: AccountsRepository) {
    suspend operator fun invoke(account: Account) {
        repository.deleteAccount(account);
    }
}





