package com.example.financemanager.di.modules

import com.example.financemanager.data.repositories.CategoriesRepository
import com.example.financemanager.data.useCases.*
import com.example.financemanager.dataBase.AppDataBase
import com.example.financemanager.dataBase.repositories.CategoriesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CategoriesModule {

    @Provides
    @Singleton
    fun providesCategoriesRepository(db: AppDataBase): CategoriesRepository {
        return CategoriesRepositoryImpl(db.categoriesDao)
    }

    @Provides
    @Singleton
    fun providesCategoriesUseCases(repository: CategoriesRepository): CategoryUseCases {
        return CategoryUseCases(
            getCategory = GetCategory(repository),
            getCategories = GetCategories(repository),
            addCategory = AddCategory(repository),
            updateCategory = UpdateCategory(repository),
            deleteCategory = DeleteCategory(repository)
        )
    }
}