package com.antonioalejandro.smkt.cookbook.utils;

import java.util.List;
import java.util.Optional;

import com.antonioalejandro.smkt.cookbook.db.CookbookDatabase;
import com.antonioalejandro.smkt.cookbook.model.Recipe;

/**
 * Search Function Functional Interface
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 * @see CookbookDatabase
 */
@FunctionalInterface
public interface SearchFunction {

	/**
	 * Search into database passed as parameter with value
	 * 
	 * @param userId {@link String}
	 * @param value  {@link String}
	 * @param db     {@link CookbookDatabase}
	 * @return
	 */
	public Optional<List<Recipe>> search(String userId, String value, CookbookDatabase db);
}
