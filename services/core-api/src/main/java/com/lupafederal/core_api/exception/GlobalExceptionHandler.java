package com.lupafederal.core_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErroCampo(String campo, String mensagem) {}

    public record ErroValidacao(List<ErroCampo> erros) {}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroValidacao handleValidacao(MethodArgumentNotValidException ex) {
        List<ErroCampo> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> new ErroCampo(e.getField(), e.getDefaultMessage()))
                .toList();

        return new ErroValidacao(erros);
    }
}
