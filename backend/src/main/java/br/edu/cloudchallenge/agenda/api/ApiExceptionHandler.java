package br.edu.cloudchallenge.agenda.api;

import br.edu.cloudchallenge.agenda.api.dto.ApiError;
import br.edu.cloudchallenge.agenda.service.ErroDeNegocio;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final ZoneId ZONA = ZoneId.of("America/Sao_Paulo");

    @ExceptionHandler(ErroDeNegocio.class)
    public ResponseEntity<ApiError> tratarNegocio(ErroDeNegocio exception, HttpServletRequest request) {
        return resposta(exception.getStatus(), exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> tratarValidacao(MethodArgumentNotValidException exception,
                                                    HttpServletRequest request) {
        Map<String, String> campos = new LinkedHashMap<>();
        for (FieldError erro : exception.getBindingResult().getFieldErrors()) {
            campos.putIfAbsent(erro.getField(), erro.getDefaultMessage());
        }
        return resposta(HttpStatus.BAD_REQUEST, "Confira os campos enviados.", request, campos);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> tratarCorpoInvalido(HttpMessageNotReadableException exception,
                                                        HttpServletRequest request) {
        return resposta(HttpStatus.BAD_REQUEST,
                "O corpo da requisição está inválido. Verifique os campos e inclua o fuso horário no início.",
                request, Map.of());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> tratarBanco(DataAccessException exception, HttpServletRequest request) {
        return resposta(HttpStatus.SERVICE_UNAVAILABLE,
                "Não foi possível acessar o banco de dados. Tente novamente mais tarde.", request, Map.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> tratarFalhaInesperada(Exception exception, HttpServletRequest request) {
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro interno. Tente novamente mais tarde.", request, Map.of());
    }

    private ResponseEntity<ApiError> resposta(HttpStatus status, String mensagem,
                                              HttpServletRequest request, Map<String, String> campos) {
        ApiError body = new ApiError(OffsetDateTime.now(ZONA), status.value(),
                status.getReasonPhrase(), mensagem, request.getRequestURI(), campos);
        return ResponseEntity.status(status).body(body);
    }
}
