package br.edu.cloudchallenge.agenda.service;

import org.springframework.http.HttpStatus;

public class ErroDeNegocio extends RuntimeException {
    private final HttpStatus status;

    public ErroDeNegocio(HttpStatus status, String mensagem) {
        super(mensagem);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
