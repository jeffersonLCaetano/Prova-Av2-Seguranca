package com.example.prova;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EntityScan("com.example.prova.model")
class ProvaApplicationTests {

	@Test
	void contextLoads() {
	}

}
