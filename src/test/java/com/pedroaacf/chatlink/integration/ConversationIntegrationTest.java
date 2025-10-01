package com.pedroaacf.chatlink.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConversationIntegrationTest {

    // 1) Define um container Postgres (Testcontainers)
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("chatdb")
            .withUsername("postgres")
            .withPassword("secret");

    // 2) Informa ao Spring Boot para usar as propriedades do container
    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TestRestTemplate rest;

    @Test
    void createConversationAndReadMessages_shouldReturnEmptyListInitially() {
        // 3) Chama POST /api/conversations para criar conversa
        ResponseEntity<Map> postResp = rest.postForEntity("/api/conversations", null, Map.class);
        assertThat(postResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(postResp.getBody()).isNotNull();
        assertThat(postResp.getBody()).containsKey("token");

        String token = postResp.getBody().get("token").toString();

        // 4) Chama GET /api/conversations/{token}/messages
        ResponseEntity<List> getResp = rest.getForEntity("/api/conversations/" + token + "/messages", List.class);
        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        // inicialmente sem mensagens
        assertThat(getResp.getBody()).isEmpty();
    }
}
