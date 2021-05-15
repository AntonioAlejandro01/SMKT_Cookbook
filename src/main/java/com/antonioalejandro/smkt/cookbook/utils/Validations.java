package com.antonioalejandro.smkt.cookbook.utils;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.antonioalejandro.smkt.cookbook.model.dto.IngredientDTO;
import com.antonioalejandro.smkt.cookbook.model.dto.RecipeDTO;
import com.antonioalejandro.smkt.cookbook.model.exceptions.CookbookException;

/**
 * The Class Validations.
 */
@Component
public class Validations {

	/**
	 * Id.
	 *
	 * @param id the id
	 * @return the validations
	 * @throws CookbookException the error service
	 */
	public Validations id(String id) throws CookbookException {
		if (id == null) {
			throw mandatoryError("id");
		}

		if (id.isBlank()) {
			throw emptyError("id");
		}
		return this;

	}
	
	/**
	 * Id.
	 *
	 * @param id the id
	 * @return the validations
	 * @throws CookbookException the cookbook exception
	 */
	public Validations id(Optional<String> id) throws CookbookException {
		if (id.isPresent()) {
			id(id);
		}
		return this;

	}

	/**
	 * List strings.
	 *
	 * @param field the field
	 * @param strings the strings
	 * @return the validations
	 * @throws CookbookException the cookbook exception
	 */
	public Validations listStrings(String field, List<String> strings) throws CookbookException {
		for (String s : strings) {
			string(field, s);
		}
		return this;
	}

	/**
	 * Product.
	 *
	 * @param recipe the recipe
	 * @return the validations
	 * @throws CookbookException the error service
	 */
	public Validations recipe(RecipeDTO recipe) throws CookbookException {
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
		return this;

	}

	/**
	 * Ingredient.
	 *
	 * @param ingredient the ingredient
	 * @return the validations
	 * @throws CookbookException the cookbook exception
	 */
	public Validations ingredient(IngredientDTO ingredient) throws CookbookException {
		string("ingredient name", ingredient.getName());
		string("ingredient amount", ingredient.getAmount());
		return this;
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @return the validations
	 * @throws CookbookException the error service
	 */
	public Validations value(String value) throws CookbookException {
		if (value == null) {
			throw mandatoryError("value");
		}
		if (value.isBlank()) {
			throw emptyError("value");
		}
		
		return this;
	}

	/**
	 * Amount.
	 *
	 * @param time the time
	 * @return the validations
	 * @throws CookbookException the error service
	 */
	public Validations time(double time) throws CookbookException {
		if (time <= 0) {
			throw negativeOrZeroAmountError("time");
		}
		return this;

	}

	/**
	 * String.
	 *
	 * @param field the field
	 * @param value the value
	 * @return the validations
	 * @throws CookbookException the error service
	 */
	public Validations string(String field, String value) throws CookbookException {
		if (value == null) {
			throw mandatoryError(field);
		}

		if (value.isBlank()) {
			throw emptyError(field);
		}
		return this;

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
