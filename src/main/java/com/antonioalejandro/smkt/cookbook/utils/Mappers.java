package com.antonioalejandro.smkt.cookbook.utils;

import java.util.stream.Collectors;

import org.bson.Document;

import com.antonioalejandro.smkt.cookbook.model.Ingredient;
import com.antonioalejandro.smkt.cookbook.model.Recipe;
import com.antonioalejandro.smkt.cookbook.model.dto.IngredientDTO;
import com.antonioalejandro.smkt.cookbook.model.dto.RecipeDTO;

public interface Mappers extends ConstantsMappers {

	default Document recipeToDocument(Recipe recipe) {
		return new Document(KEY_ID, recipe.getId()).append(TITLE, recipe.getTitle()).append(TIME, recipe.getTime())
				.append(STEPS, recipe.getSteps())
				.append(INGREDIENTS,
						recipe.getIngredients().stream().map(this::ingredientToDocument).collect(Collectors.toList()))
				.append(USER_ID, recipe.getUserId());
	}

	default Document ingredientToDocument(Ingredient ingredient) {
		return new Document().append(NAME, ingredient.getName()).append(AMOUNT, ingredient.getAmount());
	}

	default Recipe documentToRecipe(Document doc) {
		Recipe recipe = new Recipe();
		recipe.setId(doc.getString(KEY_ID));
		recipe.setTime(doc.getDouble(TIME));
		recipe.setTitle(doc.getString(TITLE));
		recipe.setSteps(doc.getList(STEPS, String.class));
		recipe.setIngredients(doc.getList(INGREDIENTS, Document.class).stream().map(this::documentToIngredient)
				.collect(Collectors.toList()));
		return recipe;
	}

	default Ingredient documentToIngredient(Document doc) {
		Ingredient ingredient = new Ingredient();
		ingredient.setName(doc.getString(NAME));
		ingredient.setAmount(doc.getString(AMOUNT));
		return ingredient;
	}

	default Recipe dtoToRecipe(RecipeDTO dto) {
		Recipe recipe = new Recipe();
		recipe.setIngredients(dto.getIngredients().stream().map(this::dtoToIngredient).collect(Collectors.toList()));
		recipe.setSteps(dto.getSteps());
		recipe.setTime(dto.getTime());
		recipe.setTitle(dto.getTitle());
		return recipe;
	}

	default Ingredient dtoToIngredient(IngredientDTO dto) {
		Ingredient ingredient = new Ingredient();
		ingredient.setAmount(dto.getAmount());
		ingredient.setName(dto.getName());
		return ingredient;
	}

}
