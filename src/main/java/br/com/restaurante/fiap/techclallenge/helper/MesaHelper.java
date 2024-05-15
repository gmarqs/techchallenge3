package br.com.restaurante.fiap.techclallenge.helper;

import br.com.restaurante.fiap.techclallenge.entities.Mesa;
import br.com.restaurante.fiap.techclallenge.entities.Reserva;
import br.com.restaurante.fiap.techclallenge.entities.enums.MesaStatus;

public class MesaHelper {

    public static Mesa criarMesa(Reserva reserva) {
        return Mesa.builder()
                .restaurante(reserva.getRestaurante())
                .status(MesaStatus.OCUPADO)
                .periodoDe(reserva.getPeriodoDe())
                .periodoAte(reserva.getPeriodoAte())
                .build();
    }
}
