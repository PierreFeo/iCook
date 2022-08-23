package ru.netology.icook.data

import android.app.Application
import android.content.Context
import android.content.res.Resources
import ru.netology.icook.R
import ru.netology.icook.UI.MainActivity

data class Recipe(
    val idRecipe: Long,
    val authorRecipe: String,
    val titleRecipe: String,
    val descriptionRecipe: String,
    val favorites: Boolean,
    val categoryRecipe: String,
    val previewImageRecipe: String? = null,
) {


    enum class RecipeCategory {
        EUROPEAN,
        ASIAN,
        PANASIA,
        EASTERN,
        AMERICAN,
        RUSSIAN,
        MEDITERRANEAN;
            fun getText(context: Context) = when(this) {
                EUROPEAN, -> context.getString(R.string.EUROPEAN)
                ASIAN -> context.getString(R.string.ASIAN)
                PANASIA -> context.getString(R.string.PANASIA)
                EASTERN -> context.getString(R.string.EASTERN)
                AMERICAN -> context.getString(R.string.AMERICAN)
                RUSSIAN -> context.getString(R.string.RUSSIAN)
                MEDITERRANEAN -> context.getString(R.string.MEDITERRANEAN)
            }
    }
}



