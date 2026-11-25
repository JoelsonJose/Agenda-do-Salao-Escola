package br.edu.cloudchallenge.agenda.repository;

import br.edu.cloudchallenge.agenda.domain.Profissional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Profissional p where p.id = :id")
    Optional<Profissional> bloquearPorId(@Param("id") Long id);
}
