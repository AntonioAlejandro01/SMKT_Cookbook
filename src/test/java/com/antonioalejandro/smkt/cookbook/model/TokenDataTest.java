package com.antonioalejandro.smkt.cookbook.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class TokenDataTest {

	@Test
	void testEmptyConstrcutorsAndToString() throws Exception {
		assertNotNull(new TokenData());
		assertFalse(new TokenData().toString().isBlank());
	}
	
	@Test
	void testConstructorandGetters() throws Exception {
		var tokenData = new TokenData("",List.of(),"",false,1L,List.of(),"","","","","");
		assertEquals("", tokenData.getClientId());
		assertEquals("", tokenData.getEmail());
		assertEquals("", tokenData.getJti());
		assertEquals("", tokenData.getLastname());
		assertEquals("", tokenData.getLastname());
		assertEquals("", tokenData.getUsername());
		assertEquals("", tokenData.getUsernameC());
		
		assertFalse(tokenData.isActive());
		assertTrue(tokenData.getAuthorities().isEmpty());
		assertTrue(tokenData.getScope().isEmpty());
		
	}
}
