package br.edu.cloudchallenge.agenda.api.dto;

import br.edu.cloudchallenge.agenda.domain.Profissional;

public record ProfissionalResponse(Long id, String nome) {
    public static ProfissionalResponse from(Profissional profissional) {
        return new ProfissionalResponse(profissional.getId(), profissional.getNome());
    }
}
