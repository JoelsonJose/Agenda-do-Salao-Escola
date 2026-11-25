package br.edu.cloudchallenge.agenda.api;

import br.edu.cloudchallenge.agenda.api.dto.HealthResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.ZoneId;

@RestController
public class HealthController {
    private final JdbcTemplate jdbcTemplate;
    private final OffsetDateTime iniciadoEm = OffsetDateTime.now(ZoneId.of("America/Sao_Paulo"));

    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping({"/health", "/api/health"})
    public ResponseEntity<HealthResponse> health() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return ResponseEntity.ok(new HealthResponse("UP", iniciadoEm, "conectado"));
        } catch (DataAccessException exception) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new HealthResponse("DOWN", iniciadoEm, "indisponível"));
        }
    }
}
