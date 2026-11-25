package br.edu.cloudchallenge.agenda.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

public record CriarAgendamentoRequest(
        @NotBlank(message = "Informe o nome fictício do cliente.")
        @Size(max = 100, message = "O nome do cliente deve ter no máximo 100 caracteres.")
        String cliente,

        @NotNull(message = "Informe o serviço.")
        @Positive(message = "O serviço informado é inválido.")
        Long servicoId,

        @NotNull(message = "Informe a profissional.")
        @Positive(message = "A profissional informada é inválida.")
        Long profissionalId,

        @NotNull(message = "Informe o início com fuso horário, por exemplo 2026-11-03T14:00:00-03:00.")
        OffsetDateTime dataHoraInicio
) {
}
