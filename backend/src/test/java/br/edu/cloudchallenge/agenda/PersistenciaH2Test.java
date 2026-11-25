package br.edu.cloudchallenge.agenda;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PersistenciaH2Test {
    @Test
    void dadosPermanecemAposFecharEReabrirBancoEmArquivo() throws Exception {
        Path pastaTemporaria = Path.of("target", "test-data");
        Files.createDirectories(pastaTemporaria);
        String url = "jdbc:h2:file:" + pastaTemporaria.resolve("agenda-" + UUID.randomUUID())
                .toAbsolutePath() + ";DB_CLOSE_ON_EXIT=FALSE";
        JdbcDataSource primeiraConexao = dataSource(url);

        try (Connection conexao = primeiraConexao.getConnection(); Statement comando = conexao.createStatement()) {
            comando.execute("CREATE TABLE demonstracao (id INT PRIMARY KEY, cliente VARCHAR(100) NOT NULL)");
            comando.execute("INSERT INTO demonstracao VALUES (1, 'Cliente Fictícia')");
        }

        JdbcDataSource conexaoReaberta = dataSource(url);
        try (Connection conexao = conexaoReaberta.getConnection();
             Statement comando = conexao.createStatement();
             ResultSet resultado = comando.executeQuery("SELECT cliente FROM demonstracao WHERE id = 1")) {
            assertThat(resultado.next()).isTrue();
            assertThat(resultado.getString("cliente")).isEqualTo("Cliente Fictícia");
        }
    }

    private JdbcDataSource dataSource(String url) {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(url);
        dataSource.setUser("sa");
        dataSource.setPassword("");
        return dataSource;
    }
}
