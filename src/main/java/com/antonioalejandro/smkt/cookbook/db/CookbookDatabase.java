package com.antonioalejandro.smkt.cookbook.db;

import java.util.List;
import java.util.Optional;

import com.antonioalejandro.smkt.cookbook.model.Recipe;

/**
 * CookDatabase Interface
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 *
 */
public interface CookbookDatabase {

	/**
	 * Find All recipes associated a userId passed as parameter
	 * 
	 * @param userId {@link String}
	 * @return {@link Optional}&lt;{@link List}&lt;{@link Recipe}&gt;&gt;
	 */
	public Optional<List<Recipe>> all(String userId);

	/**
	 * Find a recipe by id if the userId already match
	 * 
	 * @param userId {@link String}
	 * @param id     {@link String}
	 * @return {@link Optional}&lt;{@link Recipe}&gt;
	 */
	public Optional<Recipe> byId(String userId, String id);

	/**
	 * Find recipes that already one ingredients names matches with names list
	 * passed as parameter parameter
	 * 
	 * @param userId           {@link String}
	 * @param ingredientsNames {@link List}&lt;{@link String}&gt;
	 * @return {@link Optional}&lt;{@link List}&lt;{@link Recipe}&gt;&gt;
	 */
	public Optional<List<Recipe>> byIngredients(String userId, List<String> ingredientsNames);

	/**
	 * Find recipes with time is less or equals than the time passed as paremeter
	 * 
	 * @param userId {@link String}
	 * @param time   {@link double}
	 * @return {@link Optional}&lt;{@link List}&lt;{@link Recipe}&gt;&gt;
	 */
	public Optional<List<Recipe>> byTime(String userId, double time);

	/**
	 * Find recipes with her title matches with the title passed as parameter
	 * 
	 * @param userId {@link String}
	 * @param name   {@link String}
	 * @return {@link Optional}&lt;{@link List}&lt;{@link Recipe}&gt;&gt;
	 */
	public Optional<List<Recipe>> byTitle(String userId, String name);

	/**
	 * Insert a recipe into a database
	 * 
	 * @param recipe {@link Recipe}
	 * @return {@link Optional}&lt;{@link Recipe}&gt;
	 */
	public Optional<Recipe> insert(Recipe recipe);

	/**
	 * Update a recipe with values passed into recipe object parameter and the id is
	 * used to search recipe.
	 * 
	 * @param userId {@link String}
	 * @param id     {@link String}
	 * @param recipe {@link Recipe}
	 * @return {@link Optional}&lt;{@link Recipe}&gt;
	 */
	public Optional<Recipe> update(String userId, String id, Recipe recipe);

	/**
	 * Delete a recipe that her id is the id passed as parameter
	 * 
	 * @param userId {@link String}
	 * @param id     {@link String}
	 * @return {@link boolean}
	 */
	public boolean delete(String userId, String id);

}
