package com.antonioalejandro.smkt.cookbook.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class IngredientDTOTest {

	@Test
	void testConstructorEmptyAndToString() throws Exception {
		assertNotNull(new IngredientDTO());
		assertNotNull(new IngredientDTO("", ""));
		assertFalse(new IngredientDTO().toString().isBlank());
	}

	@Test
	void testConstructor() throws Exception {
		var dto = new IngredientDTO();

		dto.setName("TEST");
		dto.setAmount("TEST");

		assertEquals("TEST", dto.getName());
		assertEquals("TEST", dto.getAmount());

	}

}
