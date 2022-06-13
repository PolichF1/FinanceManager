package com.example.financemanager.data.useCases

import com.example.financemanager.data.models.Category
import com.example.financemanager.data.repositories.CategoriesRepository
import kotlinx.coroutines.flow.Flow

data class CategoriesUseCases(
    private val getCategories: GetCategories,
    private val getCategory: GetCategory,
    private val addCategory: AddCategory,
    private val updateCategory: UpdateCategory,
    private val deleteCategory: DeleteCategory
)

class DeleteCategory(private val repository: CategoriesRepository) {
    suspend operator fun invoke(category: Category){
        return repository.deleteCategory(category)
    }
}

class UpdateCategory(private val repository: CategoriesRepository) {
    suspend operator fun invoke(category: Category){
        return repository.insertCategory(category)
    }
}

class AddCategory(private val repository: CategoriesRepository) {
    suspend operator fun invoke(category: Category) {
        return repository.insertCategory(category)
    }
}

class GetCategories(private val repository: CategoriesRepository) {
    operator fun invoke(): Flow<List<Category>>{
        return repository.getCategories()
    }
}

class GetCategory(private val repository: CategoriesRepository) {
    suspend operator fun invoke(id: Int): Category? {
        return  repository.getCategoryById(id)
    }
}

