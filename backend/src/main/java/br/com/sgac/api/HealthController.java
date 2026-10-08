package br.com.sgac.api;

import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {
    private final JdbcTemplate jdbcTemplate;

    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        String database = jdbcTemplate.queryForObject("SELECT DB_NAME()", String.class);
        return Map.of(
            "status", "UP",
            "application", "SGAC ERP",
            "database", database == null ? "UNKNOWN" : database
        );
    }
}
