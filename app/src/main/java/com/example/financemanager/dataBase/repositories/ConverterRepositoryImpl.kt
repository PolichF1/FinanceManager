package com.example.financemanager.dataBase.repositories

import android.content.Context
import com.example.financemanager.R
import com.example.financemanager.data.api.BynApi
import com.example.financemanager.data.api.CurrencyApi
import com.example.financemanager.data.models.currency.BynResponse
import com.example.financemanager.data.models.currency.CurrencyResponse
import com.example.financemanager.data.repositories.ConverterRepository
import com.example.financemanager.utils.Resource

class ConverterRepositoryImpl(
    private val context: Context,
    private val currencyApi: CurrencyApi,
    private val bynApi: BynApi
) : ConverterRepository {

    override suspend fun getCurrencyRates(base: String): Resource<CurrencyResponse> {
        return try {
            val response = currencyApi.getRates(base)
            val result = response.body()

            if (response.isSuccessful && result != null)
                Resource.Success(result)
            else Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(context.getString(R.string.connection_error))
        }
    }

    override suspend fun getBynRate(base: String): Resource<BynResponse> {
        return  try {

            val response = bynApi.getRates(base)
            val result = response.body()

            if (response.isSuccessful && result != null)
                Resource.Success(result)
            else Resource.Error(response.message())
        } catch (e: Exception) {
            Resource.Error(context.getString(R.string.connection_error))
        }
    }
}