package com.antonioalejandro.smkt.cookbook.service;

import java.util.List;
import java.util.Optional;

import com.antonioalejandro.smkt.cookbook.model.Recipe;
import com.antonioalejandro.smkt.cookbook.model.dto.RecipeDTO;
import com.antonioalejandro.smkt.cookbook.model.exceptions.CookbookException;

public interface RecipeService {

	public Optional<List<Recipe>> findAll(String userId) throws CookbookException;

	public Optional<Recipe> findById(String userId, String id) throws CookbookException;

	public Optional<List<Recipe>> findByIngredients(String userId, List<String> ingredientsNames) throws CookbookException;

	public Optional<List<Recipe>> findByFilter(String userId, String filter, String value) throws CookbookException;

	public Optional<byte[]> getPdf(String userId, Optional<String> id, String token) throws CookbookException;

	public Optional<Recipe> createRecipe(String userId, RecipeDTO recipe) throws CookbookException;

	public Optional<Recipe> updateRecipe(String userId, String id, RecipeDTO recipe) throws CookbookException;

	public void deleteRecipe(String userId, String id) throws CookbookException;

}
