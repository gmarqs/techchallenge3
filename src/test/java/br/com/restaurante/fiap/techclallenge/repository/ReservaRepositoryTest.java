package br.com.restaurante.fiap.techclallenge.repository;

import br.com.restaurante.fiap.techclallenge.entities.Reserva;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static br.com.restaurante.fiap.techclallenge.utils.ReservaUtils.gerarReserva;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReservaRepositoryTest {

    @Mock
    private ReservaRepository reservaRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setup(){
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception{
        openMocks.close();
    }

    @Test
    void devePermitirRegistrarReserva(){
        var reserva = gerarReserva();

        when(reservaRepository.save(reserva)).thenReturn(reserva);

        var reservaRecebida = reservaRepository.save(reserva);

        assertThat(reservaRecebida).isNotNull().isEqualTo(reserva);

        verify(reservaRepository, times(1)).save(any(Reserva.class));

    }

    @Test
    void devePermitirBuscarReserva(){
        var id = 1L;
        var reserva = gerarReserva();

        when(reservaRepository.findById(id)).thenReturn(Optional.of(reserva));

        var reservaOptional = reservaRepository.findById(id);

        assertThat(reservaOptional).isPresent().containsSame(reserva);

        verify(reservaRepository, times(1)).findById(any(Long.class));
    }
}
