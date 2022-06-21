package com.example.financemanager.data.useCases

import com.example.financemanager.data.models.Category
import com.example.financemanager.data.repositories.CategoriesRepository
import kotlinx.coroutines.flow.Flow

data class CategoryUseCases(
    val getCategories: GetCategories,
    val getCategory: GetCategory,
    val addCategory: AddCategory,
    val updateCategory: UpdateCategory,
    val deleteCategory: DeleteCategory
)

class GetCategories(private val repository: CategoriesRepository) {
    operator fun invoke(): Flow<List<Category>> {
        return repository.getCategories()
    }
}

class GetCategory(private val repository: CategoriesRepository) {
    suspend operator fun invoke(id: Int): Category? {
        return repository.getCategoryById(id)
    }
}

class AddCategory(private val repository: CategoriesRepository) {
    suspend operator fun invoke(category: Category) {
        return repository.updateCategory(category)
    }
}

class UpdateCategory(private val repository: CategoriesRepository) {
    suspend operator fun invoke(category: Category) {
        return repository.insertCategory(category)
    }
}

class DeleteCategory(private val repository: CategoriesRepository) {
    suspend operator fun invoke(category: Category) {
        return repository.deleteCategory(category)
    }
}





