package ru.netology.icook.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map

import ru.netology.icook.data.Recipe
import ru.netology.icook.db.RecipeDao
import ru.netology.icook.db.RecipeEntity
import ru.netology.icook.db.toRecipe
import ru.netology.icook.db.toRecipeEntity
import kotlinx.coroutines.flow.Flow

class RecipeRepositoryRoomImpl(
    private val dao: RecipeDao
) : RecipeRepository {


    override fun getAll() = dao.getAll()
        .map { list: List<RecipeEntity> -> list.map { PostEntity -> PostEntity.toRecipe() } }

    override fun favoriteRecipeById(id: Long) {
        dao.addFavoriteById(id)
    }

    override fun removeRecipeById(id: Long) {
        dao.removeRecipeById(id)
    }

    override fun saveRecipe(recipe: Recipe) {
        if (recipe.idRecipe == 0L) dao.insert(recipe.toRecipeEntity()) else dao.updateDescriptionRecipeById(
            recipe.idRecipe,
            recipe.descriptionRecipe,
            recipe.titleRecipe,
            recipe.authorRecipe,
            recipe.categoryRecipe,
            recipe.previewImageRecipe
        )
    }

    override fun searchDatabase(searchQuery: String): List<Recipe>? {
        return dao.searchDatabase(searchQuery)
    }

    override fun filterDatabase(filterQuery: List<String?>): List<Recipe>? {
        return dao.filterDatabase(filterQuery)
    }

    override fun getRecipeDataBase(id: Long): Recipe {
        return dao.getRecipeDataBase(id).toRecipe()
    }

    override fun getRecipeFavoritesDataBase(isTrue: Boolean): LiveData<List<Recipe>> {
        return dao.getRecipeFavoritesDataBase(isTrue)
            .map { list: List<RecipeEntity> -> list.map { PostEntity -> PostEntity.toRecipe() } }
    }

    override fun getRecipeDetailedDataBase(id: Long): LiveData<Recipe> {
        return dao.getRecipeDetailedDataBase(id).map { it.toRecipe() }
    }

}


