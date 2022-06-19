package com.example.financemanager.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.financemanager.utils.Converter
import java.time.LocalDate
import java.time.LocalTime

@Entity
@TypeConverters(Converter::class)
data class Transaction(

    @PrimaryKey
    val id: Int? = null,

    val name: String,
    val note: String = "",
    val amount: Double,
    val date: LocalDate,
    val time: LocalTime,
    val type: TransactionType,
    val categoryId: Int,
    val accountId: Int
)

enum class TransactionType {INCOME, EXPENSE}