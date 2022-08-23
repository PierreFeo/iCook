package ru.netology.icook.UI

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.icook.R
import ru.netology.icook.databinding.NewRecipeFragmentBinding
import ru.netology.icook.databinding.RecipeDetailedFragmentBinding
import ru.netology.icook.util.RECIPE_ID
import ru.netology.icook.util.bundle
import ru.netology.icook.viewModel.RecipeViewModel

class RecipeDetailedFragment : Fragment() {
    private lateinit var binding: RecipeDetailedFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RecipeDetailedFragmentBinding.inflate(inflater, container, false)
        val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

        val incomeIdRecipe = arguments?.getLong(RECIPE_ID)
        val currentRecipe = incomeIdRecipe?.let { viewModel.getRecipeLiveData(it) }


        currentRecipe?.observe(viewLifecycleOwner) { recipe ->
            with(binding) {
                titleTextView.text = recipe.titleRecipe
                authorTextView.text = recipe.authorRecipe
                categoryTextView.text = recipe.categoryRecipe
                content.text = recipe.descriptionRecipe
                toolbar.menu.findItem(R.id.addFavorites).isChecked = recipe.favorites
                recipe.previewImageRecipe?.let { previewImageImageView.setImageURI(Uri.parse(it)) }
                    ?: previewImageImageView.setImageResource(R.drawable.empty)
                toolbar.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.addFavorites -> {
                            if (it.isChecked) makeSnackBar(getString(R.string.dell_to_bookmarks))
                            else makeSnackBar(getString(R.string.added_to_bookmarks))
                            viewModel.onFavoriteClicked(recipe)
                            true
                        }
                        else -> false
                    }
                }
            }
        }


        binding.toolbar.setNavigationIcon(R.drawable.ic_back_24dp)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun makeSnackBar(textSnack: String) {
        Snackbar.make(
            binding.root,
            textSnack,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}