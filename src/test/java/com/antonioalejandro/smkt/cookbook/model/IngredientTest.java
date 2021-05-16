package com.antonioalejandro.smkt.cookbook.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class IngredientTest {

	@Test
	void testEmptyConstructorAndToString() throws Exception {
		assertNotNull(new Ingredient());
		assertFalse(new Ingredient().toString().isBlank());
	}

	@Test
	void testFullConstrcutorAndGettersAndSetters() throws Exception {
		var ingredient = new Ingredient("NAME", "AMOUNT");

		assertEquals("NAME", ingredient.getName());
		assertEquals("AMOUNT", ingredient.getAmount());

		ingredient.setName("NAME:1");
		ingredient.setAmount("AMOUNT:1");

		assertEquals("NAME:1", ingredient.getName());
		assertEquals("AMOUNT:1", ingredient.getAmount());
	}

}
