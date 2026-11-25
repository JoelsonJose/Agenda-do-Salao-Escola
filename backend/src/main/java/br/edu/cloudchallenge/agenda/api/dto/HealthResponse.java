package br.edu.cloudchallenge.agenda.api.dto;

import java.time.OffsetDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public record HealthResponse(String status, @JsonProperty("iniciado_em") OffsetDateTime iniciadoEm, String banco) {
}
