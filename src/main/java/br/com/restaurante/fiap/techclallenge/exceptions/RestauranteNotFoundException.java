package br.com.restaurante.fiap.techclallenge.exceptions;

public class RestauranteNotFoundException extends RuntimeException {
    public RestauranteNotFoundException(String mensagem) {
        super(mensagem);
    }
}
