package com.example.financemanager.data.useCases

import com.example.financemanager.data.models.Transaction
import com.example.financemanager.data.repositories.AccountsRepository
import com.example.financemanager.data.repositories.TransactionsRepository

class AddTransactionUseCase(
    private val transactionsRepository: TransactionsRepository,
    private val accountsRepository: AccountsRepository
) {

    suspend operator fun invoke(transaction: Transaction) {
        val account = accountsRepository.getAccountById(transaction.accountId)

        if (account != null) {
            val amount = account.amount - transaction.amount
            accountsRepository.updateAccountAmount(transaction.accountId, amount)
            transactionsRepository.insertTransaction(transaction)
        }
    }

}