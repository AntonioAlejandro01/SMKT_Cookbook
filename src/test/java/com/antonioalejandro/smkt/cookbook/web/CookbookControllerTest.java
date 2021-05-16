package com.antonioalejandro.smkt.cookbook.web;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.antonioalejandro.smkt.cookbook.model.Ingredient;
import com.antonioalejandro.smkt.cookbook.model.Recipe;
import com.antonioalejandro.smkt.cookbook.model.dto.IngredientDTO;
import com.antonioalejandro.smkt.cookbook.model.dto.RecipeDTO;
import com.antonioalejandro.smkt.cookbook.model.exceptions.CookbookException;
import com.antonioalejandro.smkt.cookbook.service.RecipeService;
import com.antonioalejandro.smkt.cookbook.utils.Validations;

class CookbookControllerTest {

	@Mock
	private RecipeService service;

	@Mock
	private Validations validations;

	@InjectMocks
	private CookbookController controller;

	@BeforeEach
	void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testAllOk() throws Exception {
		when(service.findAll(Mockito.anyString())).thenReturn(Optional.of(List.of()));
		assertEquals(HttpStatus.OK, controller.all("Admin").getStatusCode());
	}

	@Test
	void testAllNoContent() throws Exception {
		when(service.findAll(Mockito.anyString())).thenReturn(Optional.empty());
		assertEquals(HttpStatus.NO_CONTENT, controller.all("Admin").getStatusCode());
	}

	@Test
	void testIdOk() throws Exception {
		when(service.findById(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(new Recipe()));
		assertEquals(HttpStatus.OK, controller.byId("Admin", "ID").getStatusCode());
	}

	@Test
	void testByIdNoContent() throws Exception {
		when(service.findById(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.empty());
		assertEquals(HttpStatus.NOT_FOUND, controller.byId("Admin", "ID").getStatusCode());
	}

	@Test
	void testIngredientsOk() throws Exception {
		when(service.findByIngredients(Mockito.anyString(), Mockito.anyList())).thenReturn(Optional.of(List.of()));
		assertEquals(HttpStatus.OK, controller.byIngredients("Admin", Arrays.asList("gg", "ee")).getStatusCode());
	}

	@Test
	void testIngredientsNoContent() throws Exception {
		when(service.findByIngredients(Mockito.anyString(), Mockito.anyList())).thenReturn(Optional.empty());
		assertEquals(HttpStatus.NO_CONTENT,
				controller.byIngredients("Admin", Arrays.asList("GG", "EE")).getStatusCode());
	}

	@Test
	void testFilterOk() throws Exception {
		when(service.findByFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.of(List.of()));
		assertEquals(HttpStatus.OK, controller.byFilterAndValue("Admin", "filter", "value").getStatusCode());
	}

	@Test
	void testFilterNoContent() throws Exception {
		when(service.findByFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.empty());
		assertEquals(HttpStatus.NO_CONTENT, controller.byFilterAndValue("Admin", "filter", "value").getStatusCode());
	}

	@Test
	void testPdfOk() throws Exception {

		when(service.getPdf(Mockito.anyString(), Mockito.any(), Mockito.anyString()))
				.thenReturn(Optional.of(new byte[] {}));
		// all
		assertEquals(HttpStatus.OK, controller.pdf("Admin", "token", Optional.empty()).getStatusCode());
		// id
		assertEquals(HttpStatus.OK, controller.pdf("Admin", "token", Optional.of("ID")).getStatusCode());
	}

	@Test
	void testPdfNoContent() throws Exception {
		when(service.getPdf(Mockito.anyString(), Mockito.any(), Mockito.anyString())).thenReturn(Optional.empty());
		assertEquals(HttpStatus.NO_CONTENT, controller.pdf("Admin", "token", Optional.empty()).getStatusCode());
	}

	@Test
	void testFilters() throws Exception {
		assertNotNull(controller.filters());
	}

	@Test
	void testCreateRecipeCreated() throws Exception {
		when(service.createRecipe(Mockito.anyString(), Mockito.any(RecipeDTO.class)))
				.thenReturn(Optional.of(new Recipe("ID", "TITLE", List.of(new Ingredient("NAME", "AMOUNT")),
						List.of("Step 1"), 0.3d, "USER_ID")));

		assertEquals(HttpStatus.CREATED,
				controller.createRecipe("Admin",
						new RecipeDTO("TITLE", List.of(new IngredientDTO("NAME", "AMOUNT")), List.of("Step"), 0.3d))
						.getStatusCode());
	}

	@Test
	void testCreateRecipeForbidden() throws Exception {
		when(service.createRecipe(Mockito.anyString(), Mockito.any(RecipeDTO.class))).thenReturn(Optional.empty());

		assertEquals(HttpStatus.FORBIDDEN,
				controller.createRecipe("Admin",
						new RecipeDTO("TITLE", List.of(new IngredientDTO("NAME", "AMOUNT")), List.of("Step"), 0.3d))
						.getStatusCode());
	}

	@Test
	void testUpdateAccepted() throws Exception {
		when(service.updateRecipe(Mockito.anyString(), Mockito.anyString(), Mockito.any(RecipeDTO.class)))
				.thenReturn(Optional.of(new Recipe("ID", "TITLE", List.of(new Ingredient("NAME", "AMOUNT")),
						List.of("Step 1"), 0.3d, "USER_ID")));

		assertEquals(HttpStatus.ACCEPTED,
				controller.updateRecipe("Admin", "ID",
						new RecipeDTO("TITLE", List.of(new IngredientDTO("NAME", "AMOUNT")), List.of("Step"), 0.3d))
						.getStatusCode());
	}

	@Test
	void testUpdateForbidden() throws Exception {
		when(service.updateRecipe(Mockito.anyString(), Mockito.anyString(), Mockito.any(RecipeDTO.class)))
				.thenReturn(Optional.empty());

		assertEquals(HttpStatus.FORBIDDEN,
				controller.updateRecipe("Admin", "ID",
						new RecipeDTO("TITLE", List.of(new IngredientDTO("NAME", "AMOUNT")), List.of("Step"), 0.3d))
						.getStatusCode());
	}

	@Test
	void testDelete() throws Exception {
		doNothing().when(service).deleteRecipe(Mockito.anyString(), Mockito.anyString());

		assertEquals(HttpStatus.ACCEPTED, controller.deleteRecipe("Admin", "ID").getStatusCode());
	}

	@Test
	void testHandleExeptions() throws Exception {
		var e = new CookbookException(HttpStatus.I_AM_A_TEAPOT, HttpStatus.I_AM_A_TEAPOT.getReasonPhrase(), 2L);
		var response = controller.handleErrorService(e);
		assertEquals(HttpStatus.I_AM_A_TEAPOT, response.getStatusCode());
		assertTrue(response.getBody().contains(HttpStatus.I_AM_A_TEAPOT.getReasonPhrase()));
		assertTrue(response.getBody().contains("2"));
		assertTrue(response.getBody().contains(String.valueOf(response.getStatusCodeValue())));
	}

}
