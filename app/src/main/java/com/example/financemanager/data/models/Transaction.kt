package com.example.financemanager.data.models

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.example.financemanager.getCurrentLocalDate
import com.example.financemanager.getCurrentLocalTime
import com.example.financemanager.utils.Converter
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["account_id"],
            onDelete = CASCADE
        )
    ]
)
@TypeConverters(Converter::class)
data class Transaction(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val note: String,
    val amount: Double,
    val date: LocalDate = getCurrentLocalDate(),
    val time: LocalTime = getCurrentLocalTime(),

    @ColumnInfo(name = "category_id")
    val categoryId: Int,

    @ColumnInfo(name = "account_id")
    val accountId: Int
)