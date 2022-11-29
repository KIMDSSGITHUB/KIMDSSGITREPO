package com.dss;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Dss4MsMovieV1ApplicationTests {

	@Test
	void contextLoads() {
		Assertions.assertDoesNotThrow(this::doNotThrowException);
	}
	private void doNotThrowException() {
		System.out.println("Test");
	}

	@Test
	void applicationContextTest() {
		Dss4MsMovieV1Application.main(new String[]{});
		Assertions.assertTrue (true);
	}
}
