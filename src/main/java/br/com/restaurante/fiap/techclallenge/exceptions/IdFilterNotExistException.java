package br.com.restaurante.fiap.techclallenge.exceptions;

public class IdFilterNotExistException extends RuntimeException {
    public IdFilterNotExistException(String mensagem ) {
        super(mensagem);
    }
}
