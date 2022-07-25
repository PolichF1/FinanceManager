package com.example.financemanager.data.api

import com.example.financemanager.data.models.currency.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface CurrencyApi {

    @GET("api/v2/latest?apikey=ryoEhqo7nin1b63iA4K7JqviutXTAX1d4tCf6ZDY")
    suspend fun getRates(
        @Query("base_currency") base: String
    ): Response<CurrencyResponse>

}