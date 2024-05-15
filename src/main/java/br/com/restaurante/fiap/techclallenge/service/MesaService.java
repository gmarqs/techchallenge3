package br.com.restaurante.fiap.techclallenge.service;

import br.com.restaurante.fiap.techclallenge.entities.Mesa;
import br.com.restaurante.fiap.techclallenge.entities.Reserva;
import br.com.restaurante.fiap.techclallenge.entities.Restaurante;

public interface MesaService {

    void buscaMesaDoRestaurante(Reserva reserva, Restaurante restaurante);

    Mesa buscaMesaPorId(Long id, Restaurante restaurante);

    void alterarStatus(Mesa mesa);

    Mesa criaMesa(Mesa mesa);
}
