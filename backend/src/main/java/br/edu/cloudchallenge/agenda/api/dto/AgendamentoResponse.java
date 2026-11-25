package br.edu.cloudchallenge.agenda.api.dto;

import br.edu.cloudchallenge.agenda.domain.Agendamento;
import br.edu.cloudchallenge.agenda.domain.StatusAgendamento;

import java.time.OffsetDateTime;

public record AgendamentoResponse(
        Long id,
        String cliente,
        Long servicoId,
        String servico,
        Integer duracaoMinutos,
        Long profissionalId,
        String profissional,
        OffsetDateTime dataHoraInicio,
        OffsetDateTime dataHoraFim,
        StatusAgendamento status
) {
    public static AgendamentoResponse from(Agendamento agendamento) {
        return new AgendamentoResponse(
                agendamento.getId(),
                agendamento.getCliente(),
                agendamento.getServico().getId(),
                agendamento.getServico().getNome(),
                agendamento.getServico().getDuracaoMinutos(),
                agendamento.getProfissional().getId(),
                agendamento.getProfissional().getNome(),
                agendamento.getDataHoraInicio(),
                agendamento.getDataHoraFim(),
                agendamento.getStatus()
        );
    }
}
