package br.com.restaurante.fiap.techclallenge.exceptions;

public class BusinessException extends RuntimeException {
    public BusinessException(String mensagem) {
        super(mensagem);
    }
}
