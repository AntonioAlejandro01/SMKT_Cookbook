package com.antonioalejandro.smkt.cookbook.db;

import java.util.List;
import java.util.Optional;

import com.antonioalejandro.smkt.cookbook.model.Recipe;

public interface CookbookDatabase {

	public Optional<List<Recipe>> all(String userId);

	public Optional<Recipe> byId(String userId, String id);

	public Optional<List<Recipe>> byIngredients(String userId, List<String> ingredientsNames);

	public Optional<List<Recipe>> byTime(String userId, double time);

	public Optional<List<Recipe>> byTitle(String userId, String name);

	public Optional<Recipe> insert(Recipe recipe);

	public Optional<Recipe> update(String userId, String id, Recipe recipe);

	public boolean delete(String userId, String id);

}
