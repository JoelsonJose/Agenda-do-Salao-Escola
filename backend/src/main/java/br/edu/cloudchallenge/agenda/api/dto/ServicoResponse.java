package br.edu.cloudchallenge.agenda.api.dto;

import br.edu.cloudchallenge.agenda.domain.Servico;

public record ServicoResponse(Long id, String nome, Integer duracaoMinutos) {
    public static ServicoResponse from(Servico servico) {
        return new ServicoResponse(servico.getId(), servico.getNome(), servico.getDuracaoMinutos());
    }
}
