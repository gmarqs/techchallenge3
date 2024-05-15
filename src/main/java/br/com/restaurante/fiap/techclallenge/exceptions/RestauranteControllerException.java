package br.com.restaurante.fiap.techclallenge.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class RestauranteControllerException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, final HttpServletRequest request) {
        StringBuilder message = new StringBuilder("Erro no campo ");

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            message.append(fieldName).append(": ").append(errorMessage).append("; ");

        });

        return ResponseEntity.badRequest().body(getStandardError(HttpStatus.BAD_REQUEST.value(), "Parâmetro Obrigatório", message.toString(), request.getRequestURI()));
    }

    @ExceptionHandler(RestauranteNotFoundException.class)
    public ResponseEntity<Object> RestauranteNotFoundException(RestauranteNotFoundException ex, final HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(getStandardError(HttpStatus.NOT_FOUND.value(), "Entidade Não Encontrada", ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(IdFilterNotExistException.class)
    public ResponseEntity<Object> IdFilterNotExistException(IdFilterNotExistException ex, final HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(getStandardError(HttpStatus.BAD_REQUEST.value(),
                        "Erro de Filtro. Apenas são válidas opções: 1 - Nome, 2 - Localização e 3 - Tipo de Cozinha",
                        ex.getMessage(), request.getRequestURI()));
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
