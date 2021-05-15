package com.antonioalejandro.smkt.cookbook.utils;

import java.util.stream.Collectors;

import org.bson.Document;

import com.antonioalejandro.smkt.cookbook.model.Ingredient;
import com.antonioalejandro.smkt.cookbook.model.Recipe;
import com.antonioalejandro.smkt.cookbook.model.dto.IngredientDTO;
import com.antonioalejandro.smkt.cookbook.model.dto.RecipeDTO;

public interface Mappers {

	default Document recipeToDocument(Recipe recipe) {
		return new Document(ConstantsMappers.KEY_ID, recipe.getId()).append(ConstantsMappers.TITLE, recipe.getTitle())
				.append(ConstantsMappers.TIME, recipe.getTime()).append(ConstantsMappers.STEPS, recipe.getSteps())
				.append(ConstantsMappers.INGREDIENTS,
						recipe.getIngredients().stream().map(this::ingredientToDocument).collect(Collectors.toList()))
				.append(ConstantsMappers.USER_ID, recipe.getUserId());
	}

	default Document ingredientToDocument(Ingredient ingredient) {
		return new Document().append(ConstantsMappers.NAME, ingredient.getName()).append(ConstantsMappers.AMOUNT,
				ingredient.getAmount());
	}

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

	default Ingredient documentToIngredient(Document doc) {
		var ingredient = new Ingredient();
		ingredient.setName(doc.getString(ConstantsMappers.NAME));
		ingredient.setAmount(doc.getString(ConstantsMappers.AMOUNT));
		return ingredient;
	}

	default Recipe dtoToRecipe(RecipeDTO dto) {
		var recipe = new Recipe();
		recipe.setIngredients(dto.getIngredients().stream().map(this::dtoToIngredient).collect(Collectors.toList()));
		recipe.setSteps(dto.getSteps());
		recipe.setTime(dto.getTime());
		recipe.setTitle(dto.getTitle());
		return recipe;
	}

	default Ingredient dtoToIngredient(IngredientDTO dto) {
		var ingredient = new Ingredient();
		ingredient.setAmount(dto.getAmount());
		ingredient.setName(dto.getName());
		return ingredient;
	}

}
