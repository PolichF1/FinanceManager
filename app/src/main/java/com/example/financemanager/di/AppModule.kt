package com.example.financemanager.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.financemanager.dataBase.AppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppContext(app: Application): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDataBase {
        return Room.databaseBuilder(
            app.applicationContext,
            AppDataBase::class.java,
            AppDataBase.APP_DATABASE_NAME
        ).fallbackToDestructiveMigration().createFromAsset("database/finance_manager.db").build()
    }
}