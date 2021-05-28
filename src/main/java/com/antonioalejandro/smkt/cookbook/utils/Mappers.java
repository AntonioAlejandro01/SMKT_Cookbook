package com.antonioalejandro.smkt.cookbook.utils;

import java.util.stream.Collectors;

import org.bson.Document;

import com.antonioalejandro.smkt.cookbook.model.Ingredient;
import com.antonioalejandro.smkt.cookbook.model.Recipe;
import com.antonioalejandro.smkt.cookbook.model.dto.IngredientDTO;
import com.antonioalejandro.smkt.cookbook.model.dto.RecipeDTO;

/**
 * Mappers Interface
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 *
 */
public interface Mappers {

	/**
	 * Default map recipe to document
	 * 
	 * @param recipe {@link Recipe}
	 * @return {@link Document}
	 */
	default Document recipeToDocument(Recipe recipe) {
		return new Document(ConstantsMappers.KEY_ID, recipe.getId()).append(ConstantsMappers.TITLE, recipe.getTitle())
				.append(ConstantsMappers.TIME, recipe.getTime()).append(ConstantsMappers.STEPS, recipe.getSteps())
				.append(ConstantsMappers.INGREDIENTS,
						recipe.getIngredients().stream().map(this::ingredientToDocument).collect(Collectors.toList()))
				.append(ConstantsMappers.USER_ID, recipe.getUserId());
	}

	/**
	 * Default map ingredient to document
	 * 
	 * @param ingredient {@link Ingredient}
	 * @return {@link Document}
	 */
	default Document ingredientToDocument(Ingredient ingredient) {
		return new Document().append(ConstantsMappers.NAME, ingredient.getName()).append(ConstantsMappers.AMOUNT,
				ingredient.getAmount());
	}

	/**
	 * Default map document to recipe
	 * 
	 * @param doc {@link Document}
	 * @return {@link Recipe}
	 */
	default Recipe documentToRecipe(Document doc) {
		var recipe = new Recipe();
		recipe.setId(doc.getString(ConstantsMappers.KEY_ID));
		recipe.setTime(doc.getDouble(ConstantsMappers.TIME));
		recipe.setTitle(doc.getString(ConstantsMappers.TITLE));
		recipe.setSteps(doc.getList(ConstantsMappers.STEPS, String.class));
		recipe.setIngredients(doc.getList(ConstantsMappers.INGREDIENTS, Document.class).stream()
				.map(this::documentToIngredient).collect(Collectors.toList()));
		return recipe;
	}

	/**
	 * Default map document to Ingredient
	 * 
	 * @param doc {@link Document}
	 * @return {@link Ingredient}
	 */
	default Ingredient documentToIngredient(Document doc) {
		var ingredient = new Ingredient();
		ingredient.setName(doc.getString(ConstantsMappers.NAME));
		ingredient.setAmount(doc.getString(ConstantsMappers.AMOUNT));
		return ingredient;
	}

	/**
	 * Default map RecipeDTO to Recipe
	 * 
	 * @param dto {@link RecipeDTO}
	 * @return {@link Recipe}
	 */
	default Recipe dtoToRecipe(RecipeDTO dto) {
		var recipe = new Recipe();
		recipe.setIngredients(dto.getIngredients().stream().map(this::dtoToIngredient).collect(Collectors.toList()));
		recipe.setSteps(dto.getSteps());
		recipe.setTime(dto.getTime());
		recipe.setTitle(dto.getTitle());
		return recipe;
	}

	/**
	 * Default map IngredientDTO to Ingredient
	 * 
	 * @param dto {@link IngredientDTO}
	 * @return {@link Ingredient}
	 */
	default Ingredient dtoToIngredient(IngredientDTO dto) {
		var ingredient = new Ingredient();
		ingredient.setAmount(dto.getAmount());
		ingredient.setName(dto.getName());
		return ingredient;
	}

}
