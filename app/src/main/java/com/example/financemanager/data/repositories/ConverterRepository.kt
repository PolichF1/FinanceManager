package com.example.financemanager.data.repositories

import com.example.financemanager.data.models.currency.BynResponse
import com.example.financemanager.data.models.currency.CurrencyResponse
import com.example.financemanager.utils.Resource

interface ConverterRepository {

    suspend fun getCurrencyRates(base: String): Resource<CurrencyResponse>
    suspend fun getBynRate(base: String): Resource<BynResponse>
}