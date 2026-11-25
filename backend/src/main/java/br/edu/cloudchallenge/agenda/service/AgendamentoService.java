package br.edu.cloudchallenge.agenda.service;

import br.edu.cloudchallenge.agenda.api.dto.AgendamentoResponse;
import br.edu.cloudchallenge.agenda.api.dto.CriarAgendamentoRequest;
import br.edu.cloudchallenge.agenda.domain.Agendamento;
import br.edu.cloudchallenge.agenda.domain.Profissional;
import br.edu.cloudchallenge.agenda.domain.Servico;
import br.edu.cloudchallenge.agenda.domain.StatusAgendamento;
import br.edu.cloudchallenge.agenda.repository.AgendamentoRepository;
import br.edu.cloudchallenge.agenda.repository.ProfissionalRepository;
import br.edu.cloudchallenge.agenda.repository.ServicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class AgendamentoService {
    private static final ZoneId ZONA_DO_SALAO = ZoneId.of("America/Sao_Paulo");
    private static final LocalTime ABERTURA = LocalTime.of(8, 0);
    private static final LocalTime FECHAMENTO = LocalTime.of(20, 0);

    private final AgendamentoRepository agendamentoRepository;
    private final ProfissionalRepository profissionalRepository;
    private final ServicoRepository servicoRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository,
                              ProfissionalRepository profissionalRepository,
                              ServicoRepository servicoRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.profissionalRepository = profissionalRepository;
        this.servicoRepository = servicoRepository;
    }

    @Transactional
    public AgendamentoResponse criar(CriarAgendamentoRequest request) {
        OffsetDateTime inicio = request.dataHoraInicio();
        if (!inicio.isAfter(OffsetDateTime.now().minusSeconds(1))) {
            throw new ErroDeNegocio(HttpStatus.BAD_REQUEST, "O início do agendamento precisa estar no presente ou no futuro.");
        }

        Servico servico = servicoRepository.findById(request.servicoId())
                .orElseThrow(() -> new ErroDeNegocio(HttpStatus.NOT_FOUND, "Serviço não encontrado."));
        if (servico.getDuracaoMinutos() == null || servico.getDuracaoMinutos() < 15 || servico.getDuracaoMinutos() > 240) {
            throw new ErroDeNegocio(HttpStatus.BAD_REQUEST, "A duração do serviço deve estar entre 15 e 240 minutos.");
        }

        Profissional profissional = profissionalRepository.bloquearPorId(request.profissionalId())
                .orElseThrow(() -> new ErroDeNegocio(HttpStatus.NOT_FOUND, "Profissional não encontrado."));

        OffsetDateTime fim = inicio.plusMinutes(servico.getDuracaoMinutos());
        validarExpediente(inicio, fim);

        boolean conflito = agendamentoRepository
                .existsByProfissional_IdAndStatusAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                        profissional.getId(), StatusAgendamento.ATIVO, fim, inicio);
        if (conflito) {
            throw new ErroDeNegocio(HttpStatus.CONFLICT, "A profissional já possui um agendamento nesse intervalo.");
        }

        Agendamento salvo = agendamentoRepository.save(new Agendamento(
                request.cliente().trim(), servico, profissional, inicio, fim));
        return AgendamentoResponse.from(salvo);
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listar(LocalDate data, Long profissionalId) {
        OffsetDateTime inicio = data.atStartOfDay(ZONA_DO_SALAO).toOffsetDateTime();
        OffsetDateTime fim = data.plusDays(1).atStartOfDay(ZONA_DO_SALAO).toOffsetDateTime();
        List<Agendamento> resultados;
        if (profissionalId == null) {
            resultados = agendamentoRepository
                    .findByDataHoraInicioGreaterThanEqualAndDataHoraInicioLessThanOrderByDataHoraInicioAsc(inicio, fim);
        } else {
            if (!profissionalRepository.existsById(profissionalId)) {
                throw new ErroDeNegocio(HttpStatus.NOT_FOUND, "Profissional não encontrado.");
            }
            resultados = agendamentoRepository
                    .findByProfissional_IdAndDataHoraInicioGreaterThanEqualAndDataHoraInicioLessThanOrderByDataHoraInicioAsc(
                            profissionalId, inicio, fim);
        }
        return resultados.stream().map(AgendamentoResponse::from).toList();
    }

    @Transactional
    public AgendamentoResponse cancelar(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ErroDeNegocio(HttpStatus.NOT_FOUND, "Agendamento não encontrado."));
        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new ErroDeNegocio(HttpStatus.CONFLICT, "Esse agendamento já está cancelado.");
        }
        agendamento.cancelar();
        return AgendamentoResponse.from(agendamentoRepository.save(agendamento));
    }

    private void validarExpediente(OffsetDateTime inicio, OffsetDateTime fim) {
        OffsetDateTime inicioLocal = inicio.atZoneSameInstant(ZONA_DO_SALAO).toOffsetDateTime();
        OffsetDateTime fimLocal = fim.atZoneSameInstant(ZONA_DO_SALAO).toOffsetDateTime();
        DayOfWeek dia = inicioLocal.getDayOfWeek();
        boolean diaPermitido = dia != DayOfWeek.SUNDAY;
        boolean cabeNoDia = inicioLocal.toLocalDate().equals(fimLocal.toLocalDate());
        boolean dentroDoHorario = !inicioLocal.toLocalTime().isBefore(ABERTURA)
                && !fimLocal.toLocalTime().isAfter(FECHAMENTO);
        if (!diaPermitido || !cabeNoDia || !dentroDoHorario) {
            throw new ErroDeNegocio(HttpStatus.BAD_REQUEST,
                    "O atendimento precisa ocorrer de segunda a sábado, entre 08:00 e 20:00 (horário de Brasília).");
        }
    }
}
