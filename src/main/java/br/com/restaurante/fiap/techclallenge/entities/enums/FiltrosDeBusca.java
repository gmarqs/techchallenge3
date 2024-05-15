package br.com.restaurante.fiap.techclallenge.entities.enums;

public enum FiltrosDeBusca {
    NOME(1), LOCALIZACAO(2), TIPODECOZINHA(3);

    private int value;

    FiltrosDeBusca(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public static boolean fromValue(int value){
        for (FiltrosDeBusca status : FiltrosDeBusca.values()) {
            if (status.getValue() == value) {
                return true;
            }
        }
        throw new IllegalArgumentException("Valor numérico inválido: " + value);
    }
}
