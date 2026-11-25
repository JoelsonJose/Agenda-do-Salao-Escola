package br.edu.cloudchallenge.agenda.repository;

import br.edu.cloudchallenge.agenda.domain.Agendamento;
import br.edu.cloudchallenge.agenda.domain.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    boolean existsByProfissional_IdAndStatusAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
            Long profissionalId,
            StatusAgendamento status,
            OffsetDateTime fimNovo,
            OffsetDateTime inicioNovo);

    List<Agendamento> findByDataHoraInicioGreaterThanEqualAndDataHoraInicioLessThanOrderByDataHoraInicioAsc(
            OffsetDateTime inicioDoDia,
            OffsetDateTime inicioDoProximoDia);

    List<Agendamento> findByProfissional_IdAndDataHoraInicioGreaterThanEqualAndDataHoraInicioLessThanOrderByDataHoraInicioAsc(
            Long profissionalId,
            OffsetDateTime inicioDoDia,
            OffsetDateTime inicioDoProximoDia);
}
