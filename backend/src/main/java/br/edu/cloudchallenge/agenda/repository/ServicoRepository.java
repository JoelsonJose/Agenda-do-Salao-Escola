package br.edu.cloudchallenge.agenda.repository;

import br.edu.cloudchallenge.agenda.domain.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
}
