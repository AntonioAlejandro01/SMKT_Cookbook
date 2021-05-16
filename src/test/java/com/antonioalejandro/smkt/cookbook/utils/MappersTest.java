package com.antonioalejandro.smkt.cookbook.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.antonioalejandro.smkt.cookbook.model.Ingredient;
import com.antonioalejandro.smkt.cookbook.model.dto.IngredientDTO;
import com.antonioalejandro.smkt.cookbook.model.dto.RecipeDTO;

class MappersTest {

	@Test
	void testDtos() throws Exception {
		var mappers = new Mappers() {
		};
		var ingredient = mappers.dtoToIngredient(new IngredientDTO("NAME", "AMOUNT"));
		assertEquals("AMOUNT", ingredient.getAmount());
		assertEquals("NAME", ingredient.getName());

		var recipe = mappers.dtoToRecipe(new RecipeDTO("TITLE", List.of(), List.of(), 0d));
		assertEquals("TITLE", recipe.getTitle());
		assertNotNull(recipe.getIngredients());
		assertNotNull(recipe.getSteps());
		assertEquals(0d, recipe.getTime());
	}

	@Test
	void testIngredientToDocument() throws Exception {
		var mapper = new Mappers() {
		};

		assertNotNull(mapper.ingredientToDocument(new Ingredient("NAME", "INGREDIENT")));
	}
}
