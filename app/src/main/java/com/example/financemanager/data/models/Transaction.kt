package com.example.financemanager.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction(

    @PrimaryKey
    val id: Int? = null,

    val name: String,
    val note: String,
    val amount: Double,
    val type: TransactionType,
    val categoryId: Int,
    val accountId: Int
)