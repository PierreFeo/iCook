package ru.netology.icook.adapter

import android.icu.text.CaseMap
import android.media.Image
import androidx.lifecycle.LiveData
import ru.netology.icook.data.Recipe


interface RecipeInteractionListener {
    fun onFavoriteClicked(recipe: Recipe)
    fun onRemoveClicked(recipe: Recipe)
    fun onSaveClicked(
        title: String,
        author: String,
        category: String,
        descriptionRecipe: String,
        imageRecipe: String?
    )

    fun onEditClicked(recipe: Recipe)
    fun onRecipeClicked(recipe: Recipe)
    fun onSelectImageClicked(recipe: Recipe)
    fun currentSearch(query: String)
    fun filterSearch(queryFilter: List<String?>)
    fun getRecipe(id: Long): Recipe
    fun getRecipeLiveData(id: Long): LiveData<Recipe>

}
