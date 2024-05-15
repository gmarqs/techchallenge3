package br.com.restaurante.fiap.techclallenge.controller;

import br.com.restaurante.fiap.techclallenge.entities.Reserva;
import br.com.restaurante.fiap.techclallenge.entities.ReservaDTO;
import br.com.restaurante.fiap.techclallenge.exceptions.CapacidadeMaximaException;
import br.com.restaurante.fiap.techclallenge.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("reserva")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<?> reservarMesa(@Valid @RequestBody ReservaDTO reservaDTO) {
        var reservaCriada = reservaService.reservarMesa(reservaDTO);
        return new ResponseEntity<>(reservaCriada, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obterReservaPorId(@PathVariable Long id) {
        Reserva reserva = reservaService.obterReserva(id);
        return new ResponseEntity<>(reserva, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Reserva>> listarReservas(Pageable pageable) {
        Page<Reserva> reservas = reservaService.listarReservas(pageable);
        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> atualizarReserva(@PathVariable Long id, @RequestBody @Valid ReservaDTO reserva) {
        Reserva reservaAtualizada = reservaService.alterarReserva(id, reserva);
        return new ResponseEntity<>(reservaAtualizada, HttpStatus.ACCEPTED);
    }


}
