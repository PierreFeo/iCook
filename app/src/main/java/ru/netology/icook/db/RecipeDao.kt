package ru.netology.icook.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.icook.data.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipe_table ORDER BY idRecipe DESC")
    fun getAll(): LiveData<List<RecipeEntity>>


    @Insert
    fun insert(post: RecipeEntity)


    @Query("UPDATE recipe_table SET descriptionRecipe = :descriptionRecipe, titleRecipe = :titleRecipe, authorRecipe = :authorRecipe, categoryRecipe = :categoryRecipe, previewImageRecipe = :previewImageRecipe WHERE idRecipe = :id")
    fun updateDescriptionRecipeById(
        id: Long,
        descriptionRecipe: String,
        titleRecipe: String,
        authorRecipe: String,
        categoryRecipe: String,
        previewImageRecipe: String?
    )


    @Query("UPDATE recipe_table SET favorites = CASE WHEN favorites THEN 0 ELSE 1 END WHERE idRecipe = :id")
    fun addFavoriteById(id: Long)

    @Query("DELETE FROM recipe_table WHERE idRecipe = :id")
    fun removeRecipeById(id: Long)

    @Query("SELECT * FROM recipe_table WHERE titleRecipe LIKE :searchQuery OR authorRecipe LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): List<Recipe>?

    @Query("SELECT * FROM recipe_table WHERE categoryRecipe IN (:searchQuery) ORDER BY idRecipe DESC")
    fun filterDatabase(searchQuery: List<String?>): List<Recipe>?

    @Query("SELECT * FROM recipe_table WHERE idRecipe LIKE :id")
    fun getRecipeDataBase(id: Long): RecipeEntity

    @Query("SELECT * FROM recipe_table WHERE favorites LIKE :isTrue ORDER BY idRecipe DESC")
    fun getRecipeFavoritesDataBase(isTrue: Boolean): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM recipe_table WHERE idRecipe LIKE :id")
    fun getRecipeDetailedDataBase(id: Long): LiveData<RecipeEntity>

}