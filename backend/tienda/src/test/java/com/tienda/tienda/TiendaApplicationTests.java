package com.tienda.tienda;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class TiendaApplicationTests {

	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
			.withDatabaseName("tienda_test")
			.withUsername("postgres")
			.withPassword("admin123");

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://"
				+ postgres.getHost() + ":" + postgres.getMappedPort(5432)
				+ "/" + postgres.getDatabaseName());
		registry.add("spring.r2dbc.username", postgres::getUsername);
		registry.add("spring.r2dbc.password", postgres::getPassword);

		registry.add("spring.liquibase.url", postgres::getJdbcUrl);
		registry.add("spring.liquibase.user", postgres::getUsername);
		registry.add("spring.liquibase.password", postgres::getPassword);
	}

	@Test
	void contextLoads() {
	}

}
