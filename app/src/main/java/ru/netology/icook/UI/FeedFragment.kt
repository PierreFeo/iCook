package ru.netology.icook.UI

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import ru.netology.icook.R
import ru.netology.icook.adapter.RecipesAdapter
import ru.netology.icook.databinding.FeedFragmentBinding
import ru.netology.icook.util.RECIPE_ID
import ru.netology.icook.util.bundle
import ru.netology.icook.viewModel.RecipeViewModel


class FeedFragment() : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var binding: FeedFragmentBinding
    private val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val adapter = RecipesAdapter(viewModel)
        binding = FeedFragmentBinding.inflate(inflater, container, false)
        binding.recyclerViewRecipe.adapter = adapter

        viewModel.filterData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                with(binding) {
                    recyclerViewRecipe.visibility = View.GONE
                    notFound.setImageResource(R.drawable.recipe_not_found)
                    notFound.visibility = View.VISIBLE
                }
            } else
                with(binding) {
                    recyclerViewRecipe.visibility = View.VISIBLE
                    notFound.visibility = View.GONE
                    adapter.submitList(it)
                }
            Log.d("query", "RePAINT")
        }


        viewModel.editRecipeEvent.observe(viewLifecycleOwner) {
            bundle.putLong(RECIPE_ID, it.idRecipe)
            findNavController().navigate(R.id.newRecipeFragment, bundle)
            findNavController().clearBackStack(R.id.newRecipeFragment)

        }
        viewModel.getDetailedRecipeEvent.observe(viewLifecycleOwner) {
            bundle.putLong(RECIPE_ID, it.idRecipe)
            findNavController().navigate(R.id.action_feedFragment_to_recipeDetailedFragment, bundle)
        }

        viewModel.filterCategoryList.observe(viewLifecycleOwner) {
            viewModel.filterSearch(it)
        }

        binding.closeFiltersButtonImageView.setOnClickListener {
            with(binding) {
                closeFiltersButtonImageView.visibility = View.GONE
                chipGroup.visibility = View.GONE
                for (item in chipGroup) {
                    chipGroup.check(item.id)
                }
            }
        }


        val ids = binding.chipGroup.checkedChipIds
        for (index in ids) {
            val chip: Chip = binding.chipGroup.findViewById(index)

            // Set the chip checked change listener
            chip.setOnCheckedChangeListener { view, isChecked ->
                viewModel.setCategoryList(view.text.toString(), isChecked)


                //Search unchecked chips
                var countUncheckedChips = 0
                for (i in ids) {
                    val allChips = binding.chipGroup.findViewById(i) as Chip
                    if (!allChips.isChecked) {
                        countUncheckedChips++
                    }
                }
                with(binding) {
                    if (countUncheckedChips == 0) closeFiltersButtonImageView.visibility =
                        View.GONE else closeFiltersButtonImageView.visibility = View.VISIBLE
                    Log.d("unchecked chips", countUncheckedChips.toString())
                }
            }
        }

        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.top_app_bar, menu)
                val search = menu.findItem(R.id.search)

                val searchView = search.actionView as SearchView
                searchView.isSubmitButtonEnabled = true
                searchView.setOnQueryTextListener(this@FeedFragment)
            }


            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.search -> {
                        true
                    }
                    R.id.filter -> {
                        for (item in binding.chipGroup) {
                            binding.chipGroup.check(item.id)
                        }
                        if (menuItem.isChecked) binding.chipGroup.visibility = View.VISIBLE
                        else {
                            binding.chipGroup.visibility = View.GONE
                        }
                        menuItem.isChecked = !menuItem.isChecked
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d("query", "SUBMIT")
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText.isNullOrBlank()) {
            viewModel.setDefaultData()
            Log.d("query", "Text is null set defaultData")

        } else {

            viewModel.currentSearch(newText)
            Log.d("query", newText)
        }
        return true
    }


}