package com.dss;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Dss6MsReviewV1ApplicationTests {

	@Mock
	private SpringApplicationBuilder springApplicationBuilder;

	@Test
	void contextLoads() {
	}

	@Test
	void applicationContextTest() {
		Dss6MsReviewV1Application.main(new String[]{});
		assert (true);
	}

}
