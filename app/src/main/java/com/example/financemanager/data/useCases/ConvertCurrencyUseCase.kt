package com.example.financemanager.data.useCases

import com.example.financemanager.data.repositories.ConverterRepository
import com.example.financemanager.utils.Resource
import javax.inject.Inject

class ConvertCurrencyUseCase @Inject constructor(private val repository: ConverterRepository) {

    suspend operator fun invoke(amount: Double, from: String, to: String): Double {
        if (from == to) return amount
        val isOperationWithBYN = from == "BYN" || to == "BYN"

        return if (!isOperationWithBYN)
            getCurrencyRate(amount, from, to)
        else
            getBynRate(amount, from, to)
    }

    private suspend fun getCurrencyRate(amount: Double, from: String, to: String): Double {
        when (val response = repository.getCurrencyRates(from)) {
            is Resource.Success -> {
                val rates = response.data?.rates ?: return -1.0
                val rate = when (to) {
                    CURRENCY_USD -> rates.usd
                    CURRENCY_EUR -> rates.eur
                    CURRENCY_RUB -> rates.rub
                    CURRENCY_UAH -> rates.uah
                    CURRENCY_PLN -> rates.pln
                    else -> return -1.0
                }
                return amount * rate
            }
            is Resource.Error -> return -2.0
        }
    }

    private suspend fun getBynRate(amount: Double, from: String, to: String): Double {
        val fromIsByn = from == CURRENCY_BYN
        return when (
            val response = repository.getBynRate(
                if (!fromIsByn) from else to
            )
        ) {
            is Resource.Success -> {
                val data = response.data ?: return -1.0

                if (!fromIsByn) amount * (data.rate / data.scale)
                else amount / (data.rate / data.scale)
            }
            is Resource.Error -> -2.0
        }
    }

    companion object {
        private const val CURRENCY_BYN = "BYN"
        private const val CURRENCY_USD = "USD"
        private const val CURRENCY_EUR = "EUR"
        private const val CURRENCY_RUB = "RUB"
        private const val CURRENCY_UAH = "UAH"
        private const val CURRENCY_PLN = "PLN"
    }
}