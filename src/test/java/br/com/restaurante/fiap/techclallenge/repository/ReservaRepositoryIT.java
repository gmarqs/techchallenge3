package br.com.restaurante.fiap.techclallenge.repository;

import br.com.restaurante.fiap.techclallenge.entities.Reserva;
import br.com.restaurante.fiap.techclallenge.entities.ReservaDTO;
import br.com.restaurante.fiap.techclallenge.utils.ReservaUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static br.com.restaurante.fiap.techclallenge.utils.ReservaUtils.gerarReserva;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class ReservaRepositoryIT {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private MesaRepository mesaRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void devePermitirRegistrarReserva(){
        var id = 1L;
        var reserva = geraRestauranteMesaEUsuario();
        var reservaRecebida = reservaRepository.save(reserva);

        assertThat(reservaRecebida).isInstanceOf(Reserva.class).isNotNull();
    }

    @Test
    void devePermitirBuscarReserva(){
        var reserva = geraRestauranteMesaEUsuario();

        reservaRepository.save(reserva);

        var reservaRecebidaOptional = reservaRepository.findById(reserva.getId());

        assertThat(reservaRecebidaOptional).isPresent();
    }

    @Test
    void deveVerificarSeEstaDisponivelParaReserva(){
        var reserva = geraRestauranteMesaEUsuario();

        reserva.setPeriodoDe(reserva.getPeriodoDe().minusHours(1));
        reservaRepository.save(reserva);

        var reservaObtida = reservaRepository.verificarSePeriodoEstaDisponivelParaReserva(
                reserva.getRestaurante().getId()
                , reserva.getMesa().getNumero()
                , reserva.getPeriodoDe()
                , reserva.getPeriodoAte());

        assertThat(reservaObtida).isNotNull();
    }

    private Reserva geraRestauranteMesaEUsuario() {
        var reserva = gerarReserva();
        restauranteRepository.save(reserva.getRestaurante());
        mesaRepository.save(reserva.getMesa());
        usuarioRepository.save(reserva.getUsuario());

        return reserva;
    }
}
