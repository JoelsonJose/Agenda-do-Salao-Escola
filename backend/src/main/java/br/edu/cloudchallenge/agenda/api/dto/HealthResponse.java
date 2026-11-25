package br.edu.cloudchallenge.agenda.api.dto;

import java.time.OffsetDateTime;

public record HealthResponse(String status, OffsetDateTime iniciadoEm, String banco) {
}
