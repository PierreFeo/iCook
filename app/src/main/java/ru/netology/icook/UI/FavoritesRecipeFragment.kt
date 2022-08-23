package ru.netology.icook.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.icook.R
import ru.netology.icook.adapter.RecipesAdapter
import ru.netology.icook.databinding.FavoritesRecipesFragmentBinding
import ru.netology.icook.util.RECIPE_ID
import ru.netology.icook.util.bundle
import ru.netology.icook.viewModel.RecipeViewModel

class FavoritesRecipeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FavoritesRecipesFragmentBinding.inflate(inflater, container, false)
        val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val adapter = RecipesAdapter(viewModel)
        binding.recyclerViewRecipeFavorites.adapter = adapter

        viewModel.dataFavorites.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.recyclerViewRecipeFavorites.visibility = View.GONE
                binding.notFound.setImageResource(R.drawable.recipe_not_found)
                binding.notFound.visibility = View.VISIBLE
            } else
                binding.notFound.visibility = View.GONE
            binding.recyclerViewRecipeFavorites.visibility = View.VISIBLE
            adapter.submitList(it)
        }

        viewModel.getDetailedRecipeEvent.observe(viewLifecycleOwner) {
            bundle.putLong(RECIPE_ID, it.idRecipe)
            findNavController().navigate(R.id.recipeDetailedFragment, bundle)
        }

        viewModel.editRecipeEvent.observe(viewLifecycleOwner) {
            bundle.putLong(RECIPE_ID, it.idRecipe)
            findNavController().navigate(
                R.id.newRecipeFragment,
                bundle
            )
            findNavController().clearBackStack(R.id.newRecipeFragment)
            findNavController().clearBackStack(R.id.favoritesRecipeFragment)
        }
        return binding.root
    }

}
