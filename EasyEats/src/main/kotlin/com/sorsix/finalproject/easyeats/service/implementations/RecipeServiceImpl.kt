package com.sorsix.finalproject.easyeats.service.implementations

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sorsix.finalproject.easyeats.models.Ingredient
import com.sorsix.finalproject.easyeats.models.Recipe
import com.sorsix.finalproject.easyeats.models.User
import com.sorsix.finalproject.easyeats.models.dto.IngredientDto
import com.sorsix.finalproject.easyeats.repository.RecipeRepository
import com.sorsix.finalproject.easyeats.service.CategoryService
import com.sorsix.finalproject.easyeats.service.IngredientService
import com.sorsix.finalproject.easyeats.service.RecipeService
import com.sorsix.finalproject.easyeats.service.SubCategoryService

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.*

@Service
class RecipeServiceImpl(private val recipeRepository: RecipeRepository,
                        private val categoryService: CategoryService,
                        private val subCategoryService: SubCategoryService,
                        private val ingredientService: IngredientService): RecipeService {
    override fun getAllRecipesByCategory(category_id: String): List<Recipe>? {
        val categoryIdLong=category_id.toLongOrNull()
        if(categoryIdLong != null){
            return recipeRepository.findByCategory_Id(categoryIdLong)
        }
        return null
    }

    override fun getAllRecipesByCategoryAndSubCategory(category_id: String,
                                                       subCategory_id: String): List<Recipe>? {
        val categoryIdLong=category_id.toLongOrNull()
        val subCategoryIdLong=subCategory_id.toLongOrNull()
        if(categoryIdLong != null && subCategoryIdLong != null){
            return recipeRepository.findByCategory_IdAndSubCategory_Id(categoryIdLong, subCategoryIdLong)
        }
        return null
    }

    override fun getAllRecipes(): List<Recipe> {
        return recipeRepository.findAll()
    }

    override fun getAllRecipesByTitleContaining(queryText: String): List<Recipe> {
        return recipeRepository.findByTitleContainingIgnoreCase(queryText)
    }

    override fun addRecipe( title: String,
                            description: String,
                            file: MultipartFile,
                            category_id: String,
                            subCategory_id: String,
                            ingredients: String ): Recipe? {
        val category = categoryService.getCategoryById(category_id.toLong())
        val subCategory = subCategoryService.getSubCategoryById(subCategory_id.toLong())
        val imageName = file.originalFilename
        var recipe : Recipe? = null
        if(imageName != null) {
            val imageFilePath = Paths.get("C:\\Users\\Marija Anakieva\\Desktop\\easyEats1\\EasyEats\\EasyEats\\src\\main\\Frontend\\EasyEats\\src\\assets\\recipe_images", imageName).toString()
            Files.copy(file.inputStream, Paths.get(imageFilePath))
            recipe = Recipe(0, title, description,
                mutableListOf() , imageName, LocalDateTime.now(),
                category, subCategory, null)
        }
        if (recipe != null) {
            val objectMapper = jacksonObjectMapper()
            val ingredientsList: List<IngredientDto> = objectMapper.readValue(
                ingredients,
                objectMapper.typeFactory.constructCollectionType(List::class.java, IngredientDto::class.java)
            )
            for (ingredient in ingredientsList) {
                val savedIngredient = ingredientService.saveIngredient(ingredient.name, ingredient.quantity, ingredient.measurementUnit)
                recipe.ingredients.add(savedIngredient)
            }
            return recipeRepository.save(recipe)
        }
        return null
    }

}