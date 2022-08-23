package ru.netology.icook.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.android.material.chip.ChipGroup
import ru.netology.icook.R
import ru.netology.icook.UI.NewRecipeFragment
import ru.netology.icook.adapter.RecipeInteractionListener
import ru.netology.icook.data.Recipe
import ru.netology.icook.db.AppDb
import ru.netology.icook.repository.RecipeRepository
import ru.netology.icook.repository.RecipeRepositoryRoomImpl
import ru.netology.icook.util.SingleLiveEvent

class RecipeViewModel(application: Application) : AndroidViewModel(application),
    RecipeInteractionListener {


    private val repository: RecipeRepository =
        RecipeRepositoryRoomImpl(dao = AppDb.getInstance(context = application).recipeDao)


    val data = repository.getAll()
    val dataFavorites = repository.getRecipeFavoritesDataBase(true)

    val filterCategoryList =
        MutableLiveData(mutableListOf(*application.resources.getStringArray(R.array.categories_array)))

    val currentRecipe = MutableLiveData<Recipe?>(null)
    val editRecipeEvent = SingleLiveEvent<Recipe>()
    val selectImageEvent = SingleLiveEvent<Recipe>()
    val getDetailedRecipeEvent = SingleLiveEvent<Recipe>()

    var filterData = Transformations.distinctUntilChanged(data) as MutableLiveData<List<Recipe>>


    override fun onFavoriteClicked(recipe: Recipe) {
        repository.favoriteRecipeById(recipe.idRecipe)
    }

    override fun onRemoveClicked(recipe: Recipe) {
        repository.removeRecipeById(recipe.idRecipe)
    }

    override fun onSaveClicked(
        title: String,
        author: String,
        category: String,
        descriptionRecipe: String,
        imageRecipe: String?
    ) {
        repository.saveRecipe(
            currentRecipe.value?.copy(
                titleRecipe = title,
                authorRecipe = author,
                categoryRecipe = category,
                descriptionRecipe = descriptionRecipe,
                previewImageRecipe = imageRecipe
            ) ?: Recipe(
                NewRecipeFragment.NEW_RECIPE_ID,
                author,
                title,
                descriptionRecipe,
                false,
                category,
                imageRecipe
            )
        )
        currentRecipe.value = null
    }

    override fun onEditClicked(recipe: Recipe) {
        editRecipeEvent.value = recipe
        currentRecipe.value = recipe
    }


    override fun onRecipeClicked(recipe: Recipe) {
        getDetailedRecipeEvent.value = recipe
    }

    override fun onSelectImageClicked(recipe: Recipe) {
        selectImageEvent.value = recipe
    }

    override fun currentSearch(query: String) {
        filterData.value = repository.searchDatabase("%$query%")

    }

    override fun filterSearch(queryFilter: List<String?>) {
        filterData.value = repository.filterDatabase(queryFilter)
    }

    override fun getRecipe(id: Long): Recipe {
        return repository.getRecipeDataBase(id)
    }

    override fun getRecipeLiveData(id: Long): LiveData<Recipe> {
        return repository.getRecipeDetailedDataBase(id)
    }

    fun setDefaultData() {
        filterData.value = data.value
    }


    fun setCategoryList(categories: String, isChecked: Boolean) {
        filterCategoryList.value =
            if (isChecked) filterCategoryList.value?.plus(categories) as MutableList<String> else {
                if (filterCategoryList.value?.size == 1) return
                filterCategoryList.value?.minus(categories) as MutableList<String>
            }
        Log.d("filterList", filterCategoryList.value.toString())
    }

    fun setDefaultCurrentRecipe() {
        currentRecipe.value = null
    }
}


