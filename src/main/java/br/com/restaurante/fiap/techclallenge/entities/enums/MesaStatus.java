package br.com.restaurante.fiap.techclallenge.entities.enums;

public enum MesaStatus {
    LIVRE(1), OCUPADO(2);

    private int value;

    MesaStatus(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
}
