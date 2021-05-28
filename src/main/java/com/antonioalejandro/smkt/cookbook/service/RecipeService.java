package com.antonioalejandro.smkt.cookbook.service;

import java.util.List;
import java.util.Optional;

import com.antonioalejandro.smkt.cookbook.model.Recipe;
import com.antonioalejandro.smkt.cookbook.model.dto.RecipeDTO;
import com.antonioalejandro.smkt.cookbook.model.exceptions.CookbookException;

/**
 * Recipe Service Interface
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 *
 */
public interface RecipeService {

	/**
	 * Find All recipes associated a userId passed as parameter
	 * 
	 * @param userId {@link String}
	 * @return {@link Optional}&lt;{@link List}&lt;{@link Recipe}&gt;&gt;
	 * @throws CookbookException
	 */
	public Optional<List<Recipe>> findAll(String userId) throws CookbookException;

	/**
	 * Find a recipe by id and userId
	 * 
	 * @param userId {@link String}
	 * @param id     {@link String}
	 * @return {@link Optional}&lt;{@link Recipe}&gt;
	 * @throws CookbookException
	 */
	public Optional<Recipe> findById(String userId, String id) throws CookbookException;

	/**
	 * Find recipes that already one ingredients names matches with names list
	 * passed as parameter parameter
	 * 
	 * @param userId           {@link String}
	 * @param ingredientsNames {@link List}&lt;{@link String}&gt;
	 * @return {@link Optional}&lt;{@link List}&lt;{@link Recipe}&gt;&gt;
	 * @throws CookbookException
	 */
	public Optional<List<Recipe>> findByIngredients(String userId, List<String> ingredientsNames)
			throws CookbookException;

	/**
	 * find Recipes with a couple of filter and her value.
	 * 
	 * @param userId {@link String}
	 * @param filter {@link String}
	 * @param value  {@link String}
	 * @return {@link Optional}&lt;{@link List}&lt;{@link Recipe}&gt;&gt;
	 * @throws CookbookException
	 */
	public Optional<List<Recipe>> findByFilter(String userId, String filter, String value) throws CookbookException;

	/**
	 * Get the PDF for all user recipes if the id optional is empty. If the id is
	 * present the PDF only contains this recipe
	 * 
	 * @param userId {@link String}
	 * @param id     {@link String}
	 * @param token  {@link String}
	 * @return {@link Optional}&lt;{@link byte[]}&gt;
	 * @throws CookbookException
	 */
	public Optional<byte[]> getPdf(String userId, Optional<String> id, String token) throws CookbookException;

	/**
	 * Save a recipe
	 * 
	 * @param userId {@link String}
	 * @param recipe {@link RecipeDTO}
	 * @return {@link Optional}&lt;{@link Recipe}&gt;
	 * @throws CookbookException
	 */
	public Optional<Recipe> createRecipe(String userId, RecipeDTO recipe) throws CookbookException;

	/**
	 * Update a recipe
	 * 
	 * @param userId {@link String}
	 * @param id     {@link String}
	 * @param recipe {@link RecipeDTO}
	 * @return {@link Optional}&lt;{@link Recipe}&gt;
	 * @throws CookbookException
	 */
	public Optional<Recipe> updateRecipe(String userId, String id, RecipeDTO recipe) throws CookbookException;

	/**
	 * Delete a recipe
	 * 
	 * @param userId {@link String}
	 * @param id     {@link String}
	 * @throws CookbookException
	 */
	public void deleteRecipe(String userId, String id) throws CookbookException;

}
