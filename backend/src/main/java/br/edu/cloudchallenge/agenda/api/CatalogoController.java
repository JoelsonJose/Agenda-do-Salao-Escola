package br.edu.cloudchallenge.agenda.api;

import br.edu.cloudchallenge.agenda.api.dto.ProfissionalResponse;
import br.edu.cloudchallenge.agenda.api.dto.ServicoResponse;
import br.edu.cloudchallenge.agenda.repository.ProfissionalRepository;
import br.edu.cloudchallenge.agenda.repository.ServicoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CatalogoController {
    private final ServicoRepository servicoRepository;
    private final ProfissionalRepository profissionalRepository;

    public CatalogoController(ServicoRepository servicoRepository, ProfissionalRepository profissionalRepository) {
        this.servicoRepository = servicoRepository;
        this.profissionalRepository = profissionalRepository;
    }

    @GetMapping("/servicos")
    public List<ServicoResponse> servicos() {
        return servicoRepository.findAll().stream().map(ServicoResponse::from).toList();
    }

    @GetMapping("/profissionais")
    public List<ProfissionalResponse> profissionais() {
        return profissionalRepository.findAll().stream().map(ProfissionalResponse::from).toList();
    }
}
