package com.antonioalejandro.smkt.cookbook.utils;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.antonioalejandro.smkt.cookbook.model.dto.IngredientDTO;
import com.antonioalejandro.smkt.cookbook.model.dto.RecipeDTO;
import com.antonioalejandro.smkt.cookbook.model.exceptions.CookbookException;

@Component
public class Validations {

	/**
	 * Id.
	 *
	 * @param id the id
	 * @throws CookbookException the error service
	 */
	public void id(String id) throws CookbookException {
		if (id == null) {
			throw mandatoryError("id");
		}

		if (id.isBlank()) {
			throw emptyError("id");
		}

	}

	public void listStrings(String field, List<String> strings) throws CookbookException {
		for (String s : strings) {
			string(field, s);
		}
	}

	/**
	 * Product
	 *
	 * @param product the product
	 * @throws CookbookException the error service
	 */
	public void recipe(RecipeDTO recipe) throws CookbookException {
		if (recipe == null) {
			throw mandatoryError("recipe");
		}
		time(recipe.getTime());
		string("title", recipe.getTitle());
		if (recipe.getSteps() == null || recipe.getSteps().isEmpty()) {
			throw new CookbookException(HttpStatus.BAD_REQUEST, "The steps is not valid");
		}
		for (String step : recipe.getSteps()) {
			string("steps", step);
		}
		for (IngredientDTO ingredient : recipe.getIngredients()) {
			ingredient(ingredient);
		}

	}

	/**
	 * Ingredient.
	 *
	 * @param ingredient the ingredient
	 * @throws CookbookException the cookbook exception
	 */
	public void ingredient(IngredientDTO ingredient) throws CookbookException {
		string("ingredient name", ingredient.getName());
		string("ingredient amount", ingredient.getAmount());
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @throws CookbookException the error service
	 */
	public void value(String value) throws CookbookException {
		if (value == null) {
			throw mandatoryError("value");
		}
		if (value.isBlank()) {
			throw emptyError("value");
		}
	}

	/**
	 * Amount.
	 *
	 * @param amount the amount
	 * @throws CookbookException the error service
	 */
	public void time(double time) throws CookbookException {
		if (time <= 0) {
			throw negativeOrZeroAmountError("time");
		}

	}

	/**
	 * String.
	 *
	 * @param field the field
	 * @param value the value
	 * @throws CookbookException the error service
	 */
	public void string(String field, String value) throws CookbookException {
		if (value == null) {
			throw mandatoryError(field);
		}

		if (value.isBlank()) {
			throw emptyError(field);
		}

	}

	/**
	 * Negative or zero amount error.
	 *
	 * @param field the field
	 * @return the error service
	 */
	private CookbookException negativeOrZeroAmountError(String field) {
		return new CookbookException(HttpStatus.BAD_REQUEST,
				String.format("the %s can't be zero or less tha 0", field));
	}

	/**
	 * Mandatory error.
	 *
	 * @param field the field
	 * @return the error service
	 */
	private CookbookException mandatoryError(String field) {
		return new CookbookException(HttpStatus.BAD_REQUEST, String.format("The %s is mandatory", field));
	}

	/**
	 * Empty error.
	 *
	 * @param field the field
	 * @return the error service
	 */
	private CookbookException emptyError(String field) {
		return new CookbookException(HttpStatus.BAD_REQUEST, String.format("The %s can't be empty", field));
	}
}
