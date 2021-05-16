package com.antonioalejandro.smkt.cookbook.model.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class RecipeDTOTest {

	@Test
	void testConstructorEmptyAndToString() throws Exception {
		assertNotNull(new RecipeDTO());
		assertNotNull(new RecipeDTO("", null, null, 0d));
		assertFalse(new RecipeDTO().toString().isBlank());
	}

	@Test
	void testConstructor() throws Exception {
		var dto = new RecipeDTO();

		dto.setIngredients(List.of());
		dto.setSteps(List.of());
		dto.setTime(0d);
		dto.setTitle("TEST");

		assertNotNull(dto.getIngredients());
		assertNotNull(dto.getSteps());
		assertEquals("TEST", dto.getTitle());
		assertEquals(0d, dto.getTime());

	}

}
