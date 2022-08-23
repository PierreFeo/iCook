package ru.netology.icook.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.netology.icook.data.Recipe
import ru.netology.icook.db.RecipeEntity

interface RecipeRepository {
    fun getAll(): LiveData<List<Recipe>>
    fun favoriteRecipeById(id: Long)
    fun removeRecipeById(id: Long)
    fun saveRecipe(recipe: Recipe)
    fun searchDatabase(searchQuery: String): List<Recipe>?
    fun filterDatabase(filterQuery: List<String?>): List<Recipe>?
    fun getRecipeDataBase(id: Long): Recipe
    fun getRecipeFavoritesDataBase(isTrue: Boolean): LiveData<List<Recipe>>
    fun getRecipeDetailedDataBase(id: Long): LiveData<Recipe>


}