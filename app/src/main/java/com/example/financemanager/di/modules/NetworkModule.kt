package com.example.financemanager.di.modules

import android.content.Context
import com.example.financemanager.data.api.BynApi
import com.example.financemanager.data.api.CurrencyApi
import com.example.financemanager.data.repositories.ConverterRepository
import com.example.financemanager.data.useCases.ConvertCurrencyUseCase
import com.example.financemanager.dataBase.repositories.ConverterRepositoryImpl
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_CURRENCY_URL = "https://freecurrencyapi.net/"
    private const val BASE_BYN_URL = "https://www.nbrb.by/"

    private val moshi = MoshiConverterFactory.create(
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    )

    @Provides
    @Singleton
    fun provideCurrencyApi(): CurrencyApi = Retrofit.Builder()
        .addConverterFactory(moshi)
        .baseUrl(BASE_CURRENCY_URL)
        .build()
        .create(CurrencyApi::class.java)

    @Provides
    @Singleton
    fun provideBynApi(): BynApi = Retrofit.Builder()
        .addConverterFactory(moshi)
        .baseUrl(BASE_BYN_URL)
        .build()
        .create(BynApi::class.java)

    @Provides
    @Singleton
    fun provideCurrencyConverterRepository(
        @ApplicationContext context: Context,
        currencyApi: CurrencyApi,
        bynApi: BynApi
    ): ConverterRepository = ConverterRepositoryImpl(context, currencyApi, bynApi)

    @Provides
    @Singleton
    fun provideConvertCurrencyUseCase(repository: ConverterRepository): ConvertCurrencyUseCase =
        ConvertCurrencyUseCase(repository)
}