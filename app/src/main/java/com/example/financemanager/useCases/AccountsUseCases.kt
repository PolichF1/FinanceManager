package com.example.financemanager.useCases

import com.example.financemanager.dataBase.repositories.AccountsRepository
import kotlinx.coroutines.flow.Flow

data class AccountsUseCases(
    val getAccounts: GetAccounts,
    val getAccount: GetAccount,
    val addAccount: AddAccount,
    val updateAccount: UpdateAccount,
    val deleteAccount: DeleteAccount

)

class DeleteAccount(private val repository: AccountsRepository) {
    suspend operator fun invoke(account: com.example.financemanager.dataBase.models.Account){
        return repository.deleteAccount(account)
    }
}

class UpdateAccount(private val repository: AccountsRepository) {
    suspend operator fun invoke(account: com.example.financemanager.dataBase.models.Account) {
        return repository.insertAccount(account)
    }
}

class AddAccount(private val repository: AccountsRepository) {
    suspend operator fun invoke(account: com.example.financemanager.dataBase.models.Account) {
        return repository.insertAccount(account)
    }
}

class GetAccount(private val repository: AccountsRepository) {
    suspend operator fun invoke(id: Int): com.example.financemanager.dataBase.models.Account? {
        return repository.getAccountById(id)
    }
}

class GetAccounts(private val repository: AccountsRepository) {
    operator fun invoke(): Flow<List<com.example.financemanager.dataBase.models.Account>> {
        return repository.getAccounts()
    }
}
