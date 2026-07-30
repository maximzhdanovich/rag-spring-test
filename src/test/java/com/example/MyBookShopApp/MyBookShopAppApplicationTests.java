package com.example.MyBookShopApp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyBookShopAppApplicationTests {

	@Value("${auth.secret}")
	String authSecret;

	private final MyBookShopAppApplication application;

	@Autowired
	MyBookShopAppApplicationTests(MyBookShopAppApplication application) {
		this.application = application;
	}


	@Test
	void contextLoads() {
		assertNotNull(application);
	}

	@Test
	void verifyAuthSecret() {
		assertThat(authSecret, Matchers.containsString("box"));
	}

}
