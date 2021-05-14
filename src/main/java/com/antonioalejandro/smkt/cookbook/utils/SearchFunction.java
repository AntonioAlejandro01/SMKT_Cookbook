package com.antonioalejandro.smkt.cookbook.utils;

import java.util.List;
import java.util.Optional;

import com.antonioalejandro.smkt.cookbook.db.CookbookDatabase;
import com.antonioalejandro.smkt.cookbook.model.Recipe;

@FunctionalInterface
public interface SearchFunction {

	public Optional<List<Recipe>> search(String userId, String value, CookbookDatabase db);
}
