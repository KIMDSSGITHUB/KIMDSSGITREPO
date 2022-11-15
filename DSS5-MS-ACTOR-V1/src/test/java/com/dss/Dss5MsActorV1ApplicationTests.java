package com.dss;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Dss5MsActorV1ApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void applicationContextTest() {
		Dss5MsActorV1Application.main(new String[]{});
		assert (true);
	}

}
