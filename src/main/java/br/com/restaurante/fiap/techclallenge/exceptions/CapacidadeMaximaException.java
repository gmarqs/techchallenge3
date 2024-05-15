package br.com.restaurante.fiap.techclallenge.exceptions;

public class CapacidadeMaximaException extends RuntimeException  {
    public CapacidadeMaximaException(String mensagem) {
        super(mensagem);
    }
}
