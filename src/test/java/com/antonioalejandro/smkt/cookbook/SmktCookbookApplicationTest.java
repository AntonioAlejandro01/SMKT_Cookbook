package com.antonioalejandro.smkt.cookbook;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmktCookbookApplicationTest {

	@Test
	void test() throws Exception {
		assertDoesNotThrow(() -> {
			SmktCookbookApplication.main(new String[] {});
		});
	}
}
