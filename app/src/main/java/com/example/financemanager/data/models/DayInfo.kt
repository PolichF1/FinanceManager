package com.example.financemanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.time.LocalDate

@Entity
data class DayInfo(
    @ColumnInfo(name = "date")
    val transactionDate: LocalDate,
    @ColumnInfo(name = "amount_per_day")
    val amountPerDay: Double
)

