package com.antonioalejandro.smkt.cookbook.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import com.antonioalejandro.smkt.cookbook.db.CookbookDatabase;
import com.antonioalejandro.smkt.cookbook.model.Recipe;
import com.antonioalejandro.smkt.cookbook.model.dto.IngredientDTO;
import com.antonioalejandro.smkt.cookbook.model.dto.RecipeDTO;
import com.antonioalejandro.smkt.cookbook.model.enums.FilterEnum;
import com.antonioalejandro.smkt.cookbook.model.exceptions.CookbookException;
import com.antonioalejandro.smkt.cookbook.service.impl.RecipeServiceImpl;

class RecipeServiceImplTest {

	@Mock
	private CookbookDatabase db;

	@Mock
	private DiscoveryClient client;

	@InjectMocks
	private RecipeServiceImpl service;

	@BeforeEach
	void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testFindAll() throws Exception {
		when(db.all(Mockito.anyString())).thenReturn(Optional.empty());

		assertTrue(service.findAll("ADMIN").isEmpty());
	}

	@Test
	void testFindById() throws Exception {
		when(db.byId(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.empty());

		assertTrue(service.findById("ADMIN", "ID").isEmpty());
	}

	@Test
	void testFindByIngredients() throws Exception {
		when(db.byIngredients(Mockito.anyString(), Mockito.anyList())).thenReturn(Optional.empty());

		assertTrue(service.findByIngredients("ADMIN", List.of()).isEmpty());
	}

	@Test
	void testByFilter() throws Exception {
		when(db.byTime(Mockito.anyString(), Mockito.anyDouble())).thenReturn(Optional.empty());
		when(db.byTitle(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.empty());

		assertThrows(CookbookException.class, () -> service.findByFilter("ADMIN", "X", "VALUE"));

		assertTrue(service.findByFilter("ADMIN", FilterEnum.TIME.getName(), "0.34").isEmpty());
		assertTrue(service.findByFilter("ADMIN", FilterEnum.TITLE.getName(), "0.34").isEmpty());

	}

	@Test
	void testPdfNotFound() throws Exception {
		when(db.all(Mockito.anyString())).thenReturn(Optional.empty());
		assertThrows(CookbookException.class, () -> service.getPdf("ADMIN", Optional.empty(), "VALUE"));
	}

	@Test
	void testPdfNotFound2() throws Exception {
		when(db.all(Mockito.anyString())).thenReturn(Optional.of(new ArrayList<>()));
		assertThrows(CookbookException.class, () -> service.getPdf("ADMIN", Optional.empty(), "VALUE"));
	}

	@Test
	void testPdfServiceUnavailable() throws Exception {
		when(db.all(Mockito.anyString())).thenReturn(Optional.of(Arrays.asList(new Recipe())));
		var instance = mock(ServiceInstance.class);
		when(instance.getPort()).thenReturn(8000);
		when(instance.getHost()).thenReturn("127.0.0.1");
		when(client.getInstances(Mockito.any())).thenReturn(Arrays.asList());
		assertThrows(CookbookException.class, () -> service.getPdf("ADMIN", Optional.empty(), "VALUE"));
	}

	@Test
	void testPdfServiceAvailable() throws Exception {
		when(db.all(Mockito.anyString())).thenReturn(Optional.of(Arrays.asList(new Recipe())));
		var instance = mock(ServiceInstance.class);
		when(instance.getPort()).thenReturn(8000);
		when(instance.getHost()).thenReturn("127.0.0.1");
		when(client.getInstances(Mockito.any())).thenReturn(Arrays.asList(instance));
		assertThrows(CookbookException.class, () -> service.getPdf("ADMIN", Optional.empty(), "VALUE"));
	}

	@Test
	void testPdfId() throws Exception {
		when(db.byId(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(new Recipe()));
		var instance = mock(ServiceInstance.class);
		when(instance.getPort()).thenReturn(8000);
		when(instance.getHost()).thenReturn("127.0.0.1");
		when(client.getInstances(Mockito.any())).thenReturn(Arrays.asList(instance));
		assertThrows(CookbookException.class, () -> service.getPdf("ADMIN", Optional.of("ID"), "VALUE"));
	}

	@Test
	void testPdfId2() throws Exception {
		when(db.byId(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.empty());
		var instance = mock(ServiceInstance.class);
		when(instance.getPort()).thenReturn(8000);
		when(instance.getHost()).thenReturn("127.0.0.1");
		when(client.getInstances(Mockito.any())).thenReturn(Arrays.asList(instance));
		assertThrows(CookbookException.class, () -> service.getPdf("ADMIN", Optional.of("ID"), "VALUE"));
	}

	@Test
	void testCreateRecipe() throws Exception {
		when(db.insert(Mockito.any())).thenReturn(Optional.empty());
		assertTrue(service
				.createRecipe("ADMIN",
						new RecipeDTO("TITLE", Arrays.asList(new IngredientDTO("X", "Y")), Arrays.asList("STEP 1"), 1d))
				.isEmpty());
	}

	@Test
	void testUpdateRecipe() throws Exception {
		when(db.update(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(Optional.empty());
		assertTrue(service
				.updateRecipe("ADMIN", "ID",
						new RecipeDTO("TITLE", Arrays.asList(new IngredientDTO("X", "Y")), Arrays.asList("STEP 1"), 1d))
				.isEmpty());
	}

	@Test
	void testDeleteOk() throws Exception {
		when(db.delete(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		assertDoesNotThrow(() -> service.deleteRecipe("UserId", "ID"));
	}

	@Test
	void testDeleteBad() throws Exception {
		when(db.delete(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
		assertThrows(CookbookException.class, () -> service.deleteRecipe("UserId", "ID"));
	}

}
