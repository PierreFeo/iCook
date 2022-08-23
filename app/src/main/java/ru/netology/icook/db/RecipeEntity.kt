package ru.netology.icook.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.icook.data.Recipe


@Entity(tableName = "recipe_table")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idRecipe")
    val idRecipe: Long,

    @ColumnInfo(name = "authorRecipe")
    val authorRecipe: String,

    @ColumnInfo(name = "titleRecipe")
    val titleRecipe: String,

    @ColumnInfo(name = "descriptionRecipe")
    val descriptionRecipe: String,

    @ColumnInfo(name = "favorites")
    val favorites: Boolean,

    @ColumnInfo(name = "categoryRecipe")
    val categoryRecipe: String,

    @ColumnInfo(name = "previewImageRecipe")
    val previewImageRecipe: String? = null,
)

fun RecipeEntity.toRecipe() = Recipe(
    titleRecipe= titleRecipe,
    idRecipe = idRecipe,
    authorRecipe = authorRecipe,
    descriptionRecipe = descriptionRecipe,
    favorites = favorites,
    previewImageRecipe = previewImageRecipe,
    categoryRecipe = categoryRecipe
)

fun Recipe.toRecipeEntity() = RecipeEntity(
    titleRecipe = titleRecipe,
    idRecipe = idRecipe,
    authorRecipe = authorRecipe,
    favorites = favorites,
    previewImageRecipe = previewImageRecipe,
    categoryRecipe = categoryRecipe,
    descriptionRecipe = descriptionRecipe
)
