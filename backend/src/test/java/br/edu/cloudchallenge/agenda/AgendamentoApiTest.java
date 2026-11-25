package br.edu.cloudchallenge.agenda;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AgendamentoApiTest {
    private static final String USUARIO = "test-user";
    private static final String SENHA = "test-password";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void limparAgendamentos() {
        jdbcTemplate.update("DELETE FROM agendamentos");
    }

    @Test
    void healthInformaConexaoEHorarioDeInicio() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.banco").value("conectado"))
                .andExpect(jsonPath("$.iniciadoEm").exists());
    }

    @Test
    void listaCatalogoDeDadosFicticios() throws Exception {
        mockMvc.perform(get("/servicos").with(httpBasic(USUARIO, SENHA)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.nome == 'Corte')].duracaoMinutos").value(30));

        mockMvc.perform(get("/profissionais").with(httpBasic(USUARIO, SENHA)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.nome == 'Ana')]").isNotEmpty())
                .andExpect(jsonPath("$[?(@.nome == 'Bruno')]").isNotEmpty());
    }

    @Test
    void criaAgendamentoCalculandoOFimEPermiteHorarioContiguo() throws Exception {
        OffsetDateTime inicio = proximoDiaUtil().atTime(14, 0).atOffset(ZoneOffset.ofHours(-3));

        JsonNode criado = criar("Cliente Fictícia", 1, 1, inicio, 201);
        assertThat(OffsetDateTime.parse(criado.get("dataHoraFim").asText()))
                .isEqualTo(inicio.plusMinutes(30));

        criar("Outra Cliente", 1, 1, inicio.plusMinutes(30), 201);
    }

    @Test
    void recusaSobreposicaoDaMesmaProfissionalCom409() throws Exception {
        OffsetDateTime inicio = proximoDiaUtil().atTime(14, 0).atOffset(ZoneOffset.ofHours(-3));
        criar("Ana Cliente Um", 1, 1, inicio, 201);
        criar("Ana Cliente Dois", 1, 1, inicio.plusMinutes(15), 409);
    }

    @Test
    void aceitaMesmoHorarioParaOutraProfissional() throws Exception {
        OffsetDateTime inicio = proximoDiaUtil().atTime(14, 0).atOffset(ZoneOffset.ofHours(-3));
        criar("Ana Cliente", 1, 1, inicio, 201);
        criar("Bruno Cliente", 1, 2, inicio, 201);
    }

    @Test
    void cancelamentoLiberaHorarioEConservaHistorico() throws Exception {
        OffsetDateTime inicio = proximoDiaUtil().atTime(14, 0).atOffset(ZoneOffset.ofHours(-3));
        JsonNode criado = criar("Cliente Fictícia", 1, 1, inicio, 201);
        long id = criado.get("id").asLong();

        mockMvc.perform(patch("/agendamentos/{id}/cancelamento", id).with(httpBasic(USUARIO, SENHA)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELADO"));

        criar("Cliente Substituta", 1, 1, inicio, 201);
        Integer registros = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM agendamentos", Integer.class);
        assertThat(registros).isEqualTo(2);

        mockMvc.perform(patch("/agendamentos/{id}/cancelamento", id).with(httpBasic(USUARIO, SENHA)))
                .andExpect(status().isConflict());
    }

    @Test
    void recusaAtendimentoQueUltrapassaFechamentoEDomingo() throws Exception {
        LocalDate diaUtil = proximoDiaUtil();
        OffsetDateTime tarde = diaUtil.atTime(19, 45).atOffset(ZoneOffset.ofHours(-3));
        criar("Cliente Fictícia", 2, 1, tarde, 400);

        LocalDate domingo = diaUtil.plusDays(1);
        while (domingo.getDayOfWeek() != DayOfWeek.SUNDAY) {
            domingo = domingo.plusDays(1);
        }
        criar("Cliente Fictícia", 1, 1,
                domingo.atTime(14, 0).atOffset(ZoneOffset.ofHours(-3)), 400);
    }

    @Test
    void validaCamposEIdsInexistentesComErrosClaros() throws Exception {
        OffsetDateTime inicio = proximoDiaUtil().atTime(14, 0).atOffset(ZoneOffset.ofHours(-3));
        criar(" ", 1, 1, inicio, 400);
        criar("Cliente", 9999, 1, inicio, 404);
        mockMvc.perform(patch("/agendamentos/{id}/cancelamento", 9999)
                        .with(httpBasic(USUARIO, SENHA)))
                .andExpect(status().isNotFound());
    }

    @Test
    void filtraAgendaDoDiaPorProfissional() throws Exception {
        LocalDate dia = proximoDiaUtil();
        OffsetDateTime inicio = dia.atTime(14, 0).atOffset(ZoneOffset.ofHours(-3));
        criar("Ana Cliente", 1, 1, inicio, 201);
        criar("Bruno Cliente", 1, 2, inicio, 201);

        mockMvc.perform(get("/agendamentos")
                        .param("data", dia.toString())
                        .param("profissionalId", "1")
                        .with(httpBasic(USUARIO, SENHA)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].profissional").value("Ana"));
    }

    private JsonNode criar(String cliente, long servicoId, long profissionalId,
                           OffsetDateTime inicio, int statusEsperado) throws Exception {
        String corpo = objectMapper.writeValueAsString(new Pedido(
                cliente, servicoId, profissionalId, inicio.toString()));
        String resposta = mockMvc.perform(post("/agendamentos")
                        .with(httpBasic(USUARIO, SENHA))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andExpect(status().is(statusEsperado))
                .andReturn().getResponse().getContentAsString();
        return resposta.isBlank() ? objectMapper.createObjectNode() : objectMapper.readTree(resposta);
    }

    private LocalDate proximoDiaUtil() {
        LocalDate data = LocalDate.now().plusDays(1);
        while (data.getDayOfWeek() == DayOfWeek.SUNDAY) {
            data = data.plusDays(1);
        }
        return data;
    }

    private record Pedido(String cliente, long servicoId, long profissionalId, String dataHoraInicio) {
    }
}
