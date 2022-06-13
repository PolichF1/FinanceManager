package com.example.financemanager.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Account(

    @PrimaryKey
    val id: Int? = null,

    val name: String,
    val amount: Double = 0.0,
    val currency: String = "USD",
    val color: Int
)
