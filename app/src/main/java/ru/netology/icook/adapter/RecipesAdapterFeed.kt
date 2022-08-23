package ru.netology.icook.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.icook.R
import ru.netology.icook.UI.FeedFragment
import ru.netology.icook.data.Recipe
import ru.netology.icook.databinding.CardRecipeBinding

internal class RecipesAdapter(
    private val listener: RecipeInteractionListener
) : ListAdapter<Recipe, RecipesViewHolder>(DiffCallBack) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder {
        val view = CardRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipesViewHolder(listener, view)
    }

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    private object DiffCallBack : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.idRecipe == newItem.idRecipe
        }


        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }
    }


}

class RecipesViewHolder(
    private val listener: RecipeInteractionListener,
    private val binding: CardRecipeBinding,
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(recipe: Recipe) = with(binding) {
        titleRecipeTextView.text = recipe.titleRecipe
        authorRecipeTextView.text = recipe.authorRecipe
        favoritesButtonImageView.isChecked = recipe.favorites
        categoryRecipeTextView.text = recipe.categoryRecipe
        if (recipe.previewImageRecipe != null) previewRecipeImage.setImageURI(Uri.parse(recipe.previewImageRecipe)) else previewRecipeImage.setImageResource(
            R.drawable.empty
        )
        favoritesButtonImageView.setOnClickListener { listener.onFavoriteClicked(recipe) }
        root.setOnClickListener { listener.onRecipeClicked(recipe) }


        moreMenuIconButton.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.options_recipe)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.edit_button -> {
                            listener.onEditClicked(recipe)
                            true
                        }
                        R.id.remove_button -> {
                            listener.onRemoveClicked(recipe)
                            true
                        }
                        else -> false
                    }
                }
            }.show()


        }


    }

}


