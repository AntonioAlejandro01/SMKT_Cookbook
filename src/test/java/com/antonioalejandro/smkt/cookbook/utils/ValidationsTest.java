package com.antonioalejandro.smkt.cookbook.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.antonioalejandro.smkt.cookbook.model.dto.IngredientDTO;
import com.antonioalejandro.smkt.cookbook.model.dto.RecipeDTO;
import com.antonioalejandro.smkt.cookbook.model.exceptions.CookbookException;

class ValidationsTest {

	@InjectMocks
	private Validations validations;

	@BeforeEach
	void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testStringValidation() throws Exception {
		assertDoesNotThrow(() -> {
			validations.string("FIELD", "ID");
		});

		assertThrows(CookbookException.class, () -> validations.string("FIELD", null));
		assertThrows(CookbookException.class, () -> validations.string("FIELD", ""));
	}

	@Test
	void testIdValidation() throws Exception {
		assertDoesNotThrow(() -> {
			validations.id("ID");
		});

		assertThrows(CookbookException.class, () -> validations.id(null));
		assertThrows(CookbookException.class, () -> validations.id(""));
	}

	@Test
	void testOptionalIdValidation() throws Exception {
		assertDoesNotThrow(() -> {
			validations.optionalId(Optional.empty());
		});
		assertDoesNotThrow(() -> {
			validations.optionalId(Optional.of("ID"));
		});
	}

	@Test
	void testListStrings() throws Exception {

		assertDoesNotThrow(() -> {
			validations.listStrings("FIELD", List.of("X", "Y"));
		});

		assertDoesNotThrow(() -> {
			validations.listStrings("FIELD", List.of());
		});
	}

	@Test
	void tesRecipeValidation() throws Exception {
		assertDoesNotThrow(() -> {
			validations
					.recipe(new RecipeDTO("TITLE", List.of(new IngredientDTO("NAME", "AMOUNT")), List.of("STEP"), 10));
		});

		assertThrows(CookbookException.class, () -> validations.recipe(null));
		assertThrows(CookbookException.class, () -> validations.recipe(new RecipeDTO(null, null, null, -1d)));
		assertThrows(CookbookException.class, () -> validations.recipe(new RecipeDTO("", null, null, 1d)));
		assertThrows(CookbookException.class, () -> validations.recipe(new RecipeDTO("TITLE", null, List.of(), 1d)));
		assertThrows(CookbookException.class, () -> validations.recipe(new RecipeDTO("TITLE", null, null, 1d)));
		assertThrows(CookbookException.class,
				() -> validations.recipe(new RecipeDTO("TITLE", null, List.of("STEP"), 1d)));
		assertThrows(CookbookException.class,
				() -> validations.recipe(new RecipeDTO("TITLE", List.of(), List.of("STEP"), 1d)));

	}

	@Test
	void testValueValidation() throws Exception {
		assertDoesNotThrow(() -> validations.value("VALUE"));
		assertThrows(CookbookException.class, () -> validations.value(""));
		assertThrows(CookbookException.class, () -> validations.value(null));

	}
}
