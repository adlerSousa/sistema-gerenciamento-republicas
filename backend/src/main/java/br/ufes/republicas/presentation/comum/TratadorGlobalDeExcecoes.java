package br.ufes.republicas.presentation.comum;

import br.ufes.republicas.domain.comum.RecursoNaoEncontradoException;
import br.ufes.republicas.domain.comum.RegraDeNegocioException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Converte as excecoes do sistema em respostas HTTP padronizadas.
 */
@RestControllerAdvice
public class TratadorGlobalDeExcecoes {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<RespostaErro> tratarRecursoNaoEncontrado(
            RecursoNaoEncontradoException excecao, HttpServletRequest requisicao) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(RespostaErro.de(
                HttpStatus.NOT_FOUND.value(),
                "Nao encontrado",
                excecao.getMessage(),
                requisicao.getRequestURI()));
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<RespostaErro> tratarRegraDeNegocio(
            RegraDeNegocioException excecao, HttpServletRequest requisicao) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(RespostaErro.de(
                HttpStatus.CONFLICT.value(),
                "Conflito",
                excecao.getMessage(),
                requisicao.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespostaErro> tratarEntradaInvalida(
            MethodArgumentNotValidException excecao, HttpServletRequest requisicao) {

        String mensagem = excecao.getBindingResult().getFieldErrors().stream()
                .map(erro -> "%s: %s".formatted(erro.getField(), erro.getDefaultMessage()))
                .collect(Collectors.joining("; "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RespostaErro.de(
                HttpStatus.BAD_REQUEST.value(),
                "Requisicao invalida",
                mensagem,
                requisicao.getRequestURI()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RespostaErro> tratarArgumentoInvalido(
            IllegalArgumentException excecao, HttpServletRequest requisicao) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RespostaErro.de(
                HttpStatus.BAD_REQUEST.value(),
                "Requisicao invalida",
                excecao.getMessage(),
                requisicao.getRequestURI()));
    }
}
