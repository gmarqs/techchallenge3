package br.com.restaurante.fiap.techclallenge.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ReservaControllerException {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> BusinessException(BusinessException ex, final HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(getStandardError(HttpStatus.BAD_REQUEST.value(), "Reserva Inválida", ex.getMessage(), request.getRequestURI()));
    }

    private StandardError getStandardError(Integer status, String tipoErro, String mensagem, String uri) {

        var erro = new StandardError();

        erro.setTimestamp(Instant.now());
        erro.setStatus(status);
        erro.setError(tipoErro);
        erro.setMessage(mensagem);
        erro.setPath(uri);

        return erro;
    }
}
