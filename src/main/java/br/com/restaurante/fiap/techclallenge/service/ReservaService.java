package br.com.restaurante.fiap.techclallenge.service;

import br.com.restaurante.fiap.techclallenge.entities.Reserva;
import br.com.restaurante.fiap.techclallenge.entities.ReservaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservaService {

    Reserva reservarMesa(ReservaDTO reservaDTO);

    Reserva obterReserva(Long id);

    Page<Reserva> listarReservas(Pageable pageable);

    Reserva alterarReserva(Long id, ReservaDTO reserva);
}
