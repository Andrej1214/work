package com.pavlov.first_rest;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

//@SpringBootTest
@ActiveProfiles("test")
public abstract class AbstractTest {
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15.2");
    @BeforeAll
    public static void startContainer() {
        container.start();
    }
}
