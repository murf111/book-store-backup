package com.epam.rd.autocode.spring.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.mail.username=test@bookstore.com",
        "app.base-url=http://localhost:8080"
})
class BookStoreServiceSolutionApplicationTests {

	@Test
	void contextLoads() {
	}

}
