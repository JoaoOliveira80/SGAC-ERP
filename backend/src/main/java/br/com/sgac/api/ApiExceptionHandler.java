package br.com.sgac.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.Map;

/** Respostas de erro padronizadas; sem expor excecoes internas ou dados de conexao. */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetail> tratarStatus(
            ResponseStatusException ex, HttpServletRequest request) {
        String mensagem = ex.getReason() != null ? ex.getReason() : "Operacao nao concluida";
        return resposta(ex.getStatusCode(), mensagem, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> tratarValidacao(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(erro ->
            campos.putIfAbsent(erro.getField(), erro.getDefaultMessage() != null
                ? erro.getDefaultMessage() : "Valor invalido"));

        ProblemDetail problema = problema(HttpStatus.BAD_REQUEST,
            "Existem campos invalidos na requisicao", request);
        problema.setProperty("campos", campos);
        return ResponseEntity.badRequest().body(problema);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> tratarJson(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        return resposta(HttpStatus.BAD_REQUEST,
            "JSON invalido ou campos com tipos incorretos", request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> tratarIntegridade(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        return resposta(HttpStatus.CONFLICT,
            "Os dados entram em conflito com uma restricao do banco", request);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ProblemDetail> tratarConcorrencia(
            ObjectOptimisticLockingFailureException ex, HttpServletRequest request) {
        return resposta(HttpStatus.CONFLICT,
            "Registro modificado por outra operacao. Atualize e tente novamente", request);
    }

    private ResponseEntity<ProblemDetail> resposta(
            HttpStatusCode status, String mensagem, HttpServletRequest request) {
        return ResponseEntity.status(status).body(problema(status, mensagem, request));
    }

    private ProblemDetail problema(
            HttpStatusCode status, String mensagem, HttpServletRequest request) {
        ProblemDetail detalhe = ProblemDetail.forStatusAndDetail(status, mensagem);
        detalhe.setProperty("timestamp", OffsetDateTime.now(ZoneOffset.UTC).toString());
        detalhe.setProperty("path", request.getRequestURI());
        return detalhe;
    }
}
