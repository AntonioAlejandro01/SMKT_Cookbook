package com.antonioalejandro.smkt.cookbook.config;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;

class SwaggerConfigTest {

	@Test
	void testNotNull() throws Exception {
		var config = new SwaggerConfig();
		assertNotNull(config);
		assertNotNull(config.usersApi());
	}
}
