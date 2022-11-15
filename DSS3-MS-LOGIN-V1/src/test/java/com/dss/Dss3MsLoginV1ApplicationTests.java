package com.dss;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Dss3MsLoginV1ApplicationTests {

	@Test
	void contextLoads() {
	}
	@Test
	void applicationContextTest() {
		Dss3MsLoginV1Application.main(new String[]{});
		assert (true);
	}

}
