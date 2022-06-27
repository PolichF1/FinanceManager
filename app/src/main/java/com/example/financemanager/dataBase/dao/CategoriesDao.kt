package com.example.financemanager.dataBase.dao

import androidx.room.*
import com.example.financemanager.data.models.Category
import com.example.financemanager.data.models.CategoryView
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface CategoriesDao {

    @Query("SELECT id, name, icon, icon_color, ifnull((SELECT SUM(amount) FROM transactions WHERE categories.id = category_id AND date >= :from AND date <= :to), 0) AS category_amount FROM categories GROUP BY id")
    fun getCategoryViews(from: LocalDate, to: LocalDate): Flow<List<CategoryView>>


}