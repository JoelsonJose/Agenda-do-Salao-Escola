package br.edu.cloudchallenge.agenda.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "agendamentos")
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profissional_id", nullable = false)
    private Profissional profissional;

    @Column(name = "data_hora_inicio", nullable = false)
    private OffsetDateTime dataHoraInicio;

    @Column(name = "data_hora_fim", nullable = false)
    private OffsetDateTime dataHoraFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusAgendamento status = StatusAgendamento.ATIVO;

    protected Agendamento() {
    }

    public Agendamento(String cliente, Servico servico, Profissional profissional,
                       OffsetDateTime dataHoraInicio, OffsetDateTime dataHoraFim) {
        this.cliente = cliente;
        this.servico = servico;
        this.profissional = profissional;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.status = StatusAgendamento.ATIVO;
    }

    public void cancelar() {
        this.status = StatusAgendamento.CANCELADO;
    }

    public Long getId() {
        return id;
    }

    public String getCliente() {
        return cliente;
    }

    public Servico getServico() {
        return servico;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public OffsetDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public OffsetDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public StatusAgendamento getStatus() {
        return status;
    }
}
