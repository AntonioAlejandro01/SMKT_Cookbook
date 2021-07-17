package com.antonioalejandro.smkt.cookbook.model.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.antonioalejandro.smkt.cookbook.repository.CookbookRepository;

class FilterEnumTest {

	@Test
	void testGetterAndFromName() throws Exception {
		var time = "time";
		var title = "title";
		var other = "other";

		assertTrue(FilterEnum.fromName(time).isPresent());
		assertTrue(FilterEnum.fromName(title).isPresent());
		assertTrue(FilterEnum.fromName(other).isEmpty());

		assertEquals(time, FilterEnum.TIME.getName());
		assertEquals(title, FilterEnum.TITLE.getName());

	}

	@Test
	void testToBeSended() throws Exception {
		assertNotNull(FilterEnum.toBeSended());
		assertNotNull(FilterEnum.toBeSended());
	}

	@Test
	void testFunctionSearch() throws Exception {
		assertNotNull(FilterEnum.TIME.getFunctionSearch());
		assertDoesNotThrow(() -> {
			FilterEnum.TIME.getFunctionSearch().search("", "0.23", mock(CookbookRepository.class));
		});
		assertNotNull(FilterEnum.TITLE.getFunctionSearch());
		assertDoesNotThrow(() -> {
			FilterEnum.TITLE.getFunctionSearch().search("", "", mock(CookbookRepository.class));
		});

	}
}
