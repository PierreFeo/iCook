package ru.netology.icook.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.launch
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import ru.netology.icook.R
import ru.netology.icook.data.Recipe
import ru.netology.icook.databinding.NewRecipeFragmentBinding
import ru.netology.icook.util.RECIPE_ID
import ru.netology.icook.util.bundle
import ru.netology.icook.viewModel.RecipeViewModel


class NewRecipeFragment : Fragment() {
    private lateinit var binding: NewRecipeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = NewRecipeFragmentBinding.inflate(inflater, container, false)
        val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)


        val incomeIdRecipe = arguments?.getLong(RECIPE_ID)
        val currentRecipe = incomeIdRecipe?.let { viewModel.getRecipe(it) }

        currentRecipe?.let {
            with(binding) {
                if (!bundle.getString(IMAGE_RECIPE).isNullOrBlank()) {
                    binding.topImage.setImageURI(Uri.parse(bundle.getString(IMAGE_RECIPE)))
                } else if (!it.previewImageRecipe.isNullOrBlank()) {
                    binding.topImage.setImageURI(Uri.parse(it.previewImageRecipe))
                } else {
                    binding.topImage.setImageResource(R.drawable.noimage)
                }

                recipeTitleEditText.setText(it.titleRecipe)
                recipeAuthorEditText.setText(it.authorRecipe)
                recipeContentEditText.setText(it.descriptionRecipe)
                categoryMenu.editText?.setText(it.categoryRecipe)
            }
        } ?: if (!bundle.getString(IMAGE_RECIPE).isNullOrBlank())
            binding.topImage.setImageURI(
                Uri.parse(bundle.getString(IMAGE_RECIPE))
            ) else binding.topImage.setImageResource(R.drawable.noimage)


        binding.saveRecipeButton.setOnClickListener {
            allTextEdit().forEach {
                if (it.editText?.text.isNullOrBlank()) {
                    Snackbar.make(
                        binding.root,
                        R.string.cant_be_empty,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            }

            val title = binding.recipeTitleEditText.text.toString()
            val author = binding.recipeAuthorEditText.text.toString()
            val description = binding.recipeContentEditText.text.toString()
            val category = binding.categoryMenu.editText?.text.toString()
            val imageRecipe =
                if (bundle.getString(IMAGE_RECIPE) == null) currentRecipe?.previewImageRecipe
                else bundle.getString(IMAGE_RECIPE)


            viewModel.onSaveClicked(title, author, category, description, imageRecipe)
            bundle.putString(IMAGE_RECIPE, null)
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this@NewRecipeFragment) {
            bundle.putString(IMAGE_RECIPE, null)
            viewModel.setDefaultCurrentRecipe()
            findNavController().navigateUp()
            Log.d("backButton", "OnBackPressed")
        }


        //SelectImageFromGallery
        val selectImageFromGalleryResult = registerForActivityResult(
            SelectImageFromGallery
        ) { uri ->
            uri ?: return@registerForActivityResult
            requireContext().contentResolver.takePersistableUriPermission(
                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                        + Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            //save image in bundle
            bundle.putString(IMAGE_RECIPE, uri.toString())

            //insert in the preview
            binding.topImage.setImageURI(Uri.parse(bundle.getString(IMAGE_RECIPE)))

        }

        binding.addImageButton.setOnClickListener {
            selectImageFromGalleryResult.launch()
        }


//categories choice menu
        val itemsForChoiceCategoryMenu =
            Recipe.RecipeCategory.values().map { it.getText(requireContext()) }.toTypedArray()

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, itemsForChoiceCategoryMenu)
        (binding.categoryMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        return binding.root
    }

    //SelectImageFromGallery
    object SelectImageFromGallery : ActivityResultContract<Unit, Uri?>() {
        override fun createIntent(context: Context, input: Unit) =
            Intent(
                Intent.ACTION_OPEN_DOCUMENT,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )

        override fun parseResult(resultCode: Int, intent: Intent?) =
            if (resultCode == Activity.RESULT_OK) {
                intent?.data
            } else null
    }

    companion object {
        const val NEW_RECIPE_ID = 0L
        const val IMAGE_RECIPE = "ImageRecipe"
    }


    private fun allTextEdit(): List<TextInputLayout> {
        return listOf(
            binding.recipeTitleLayout,
            binding.recipeAuthorLayout,
            binding.recipeContentLayout,
            binding.categoryMenu
        )
    }

}


