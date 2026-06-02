package com.example.demo.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        try {
            jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS voice_recordings (
                    id              BIGSERIAL       PRIMARY KEY,
                    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
                    audio_data      BYTEA           NOT NULL,
                    duration        INTEGER
                )
            """);
            System.out.println("✓ voice_recordings table ready");
        } catch (Exception e) {
            System.err.println("Failed to initialize voice_recordings table: " + e.getMessage());
        }
    }
}
