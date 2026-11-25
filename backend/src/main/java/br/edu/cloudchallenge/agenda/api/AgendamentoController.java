package br.edu.cloudchallenge.agenda.api;

import br.edu.cloudchallenge.agenda.api.dto.AgendamentoResponse;
import br.edu.cloudchallenge.agenda.api.dto.CriarAgendamentoRequest;
import br.edu.cloudchallenge.agenda.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping({"/agendamentos", "/api/agendamentos"})
public class AgendamentoController {
    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponse> criar(@Valid @RequestBody CriarAgendamentoRequest request) {
        AgendamentoResponse response = agendamentoService.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public List<AgendamentoResponse> listar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) Long profissionalId) {
        return agendamentoService.listar(data, profissionalId);
    }

    @PatchMapping("/{id}/cancelamento")
    public AgendamentoResponse cancelar(@PathVariable Long id) {
        return agendamentoService.cancelar(id);
    }
}
