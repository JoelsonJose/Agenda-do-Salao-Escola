package br.edu.cloudchallenge.agenda;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class PersistenciaReinicioTest {

    @Autowired
    private ConfigurableApplicationContext context;

    private Path tempDir;

    @Test
    void dadosPersistemAposFecharEReabrirAplicacao() throws Exception {
        // 1️⃣ criar pasta temporária para o arquivo H2
        tempDir = Files.createTempDirectory("agenda-restart-");
        String url = "jdbc:h2:file:" + tempDir.resolve("agenda-" + UUID.randomUUID())
                .toAbsolutePath() + ";DB_CLOSE_ON_EXIT=FALSE";

        // 2️⃣ primeira inicialização – cria tabela e insere registro
        JdbcDataSource ds1 = new JdbcDataSource();
        ds1.setURL(url);
        ds1.setUser("sa");
        ds1.setPassword("");
        try (Connection c = ds1.getConnection(); Statement s = c.createStatement()) {
            s.execute("CREATE TABLE demonstracao (id INT PRIMARY KEY, cliente VARCHAR(100))");
            s.execute("INSERT INTO demonstracao VALUES (1,'Cliente Persistente')");
        }

        // 3️⃣ fechar contexto (simula reinício da aplicação)
        context.close();

        // 4️⃣ segunda inicialização – verifica se o registro continua
        JdbcDataSource ds2 = new JdbcDataSource();
        ds2.setURL(url);
        ds2.setUser("sa");
        ds2.setPassword("");
        try (Connection c = ds2.getConnection(); Statement s = c.createStatement()) {
            var rs = s.executeQuery("SELECT cliente FROM demonstracao WHERE id = 1");
            assertThat(rs.next()).isTrue();
            assertThat(rs.getString("cliente")).isEqualTo("Cliente Persistente");
        }
    }

    @AfterEach
    void cleanup() throws Exception {
        if (tempDir != null) {
            Files.walk(tempDir)
                 .sorted((a, b) -> b.compareTo(a))
                 .forEach(p -> {
                     try { Files.deleteIfExists(p); } catch (Exception ignored) {}
                 });
        }
    }
}
