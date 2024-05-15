package br.com.restaurante.fiap.techclallenge.service;

import br.com.restaurante.fiap.techclallenge.entities.Reserva;
import br.com.restaurante.fiap.techclallenge.entities.ReservaDTO;
import br.com.restaurante.fiap.techclallenge.exceptions.BusinessException;
import br.com.restaurante.fiap.techclallenge.repository.MesaRepository;
import br.com.restaurante.fiap.techclallenge.repository.ReservaRepository;
import br.com.restaurante.fiap.techclallenge.repository.RestauranteRepository;
import br.com.restaurante.fiap.techclallenge.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static br.com.restaurante.fiap.techclallenge.utils.ReservaUtils.gerarReserva;
import static br.com.restaurante.fiap.techclallenge.utils.ReservaUtils.gerarReservaDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor
class ReservaServiceIT {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private MesaRepository mesaRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ReservaServiceImpl reservaServiceImpl;

    @Test
    void devePermitirRegistrarReserva(){
        var reservaDTO = gerarReservaDTO();
        var reserva = geraRestauranteMesaEUsuario();

        reservaDTO.setRestaurante(reserva.getRestaurante().getId());
        reservaDTO.setMesa(reserva.getMesa().getId());
        reservaDTO.setUsuario(reserva.getUsuario().getId());

        var reservaSalva = reservaService.reservarMesa(reservaDTO);

        assertThat(reservaSalva).isNotNull().isInstanceOf(Reserva.class);

        assertThat(reservaSalva.getId()).isNotNull();

    }

    @Test
    void devePermitirRegistrarReservar_DatasInvalidas(){
        var reservaDTO = gerarReservaDTO();
        var reserva = geraRestauranteMesaEUsuario();
        reservaDTO.setRestaurante(reserva.getRestaurante().getId());
        reservaDTO.setMesa(reserva.getMesa().getId());
        reservaDTO.setUsuario(reserva.getUsuario().getId());

        reservaDTO.setPeriodoDe(reservaDTO.getPeriodoDe().plusDays(1));

        assertThatThrownBy(() -> reservaService.reservarMesa(reservaDTO)).isInstanceOf(BusinessException.class);

    }
    @Test
    void devePermitirBuscarReserva(){
        //var id = 1L;
        ReservaDTO reservaDTO = gerarReservaDTO();
        Reserva reserva = geraRestauranteMesaEUsuario();
        reservaDTO.setUsuario(reserva.getUsuario().getId());
        reservaDTO.setMesa(reserva.getMesa().getId());
        reservaDTO.setRestaurante(reserva.getRestaurante().getId());
        var reservaSalva = reservaService.reservarMesa(reservaDTO);

        var reservaObtida = reservaService.obterReserva(reservaSalva.getId());

        assertThat(reservaObtida).isNotNull().isInstanceOf(Reserva.class);
        assertThat(reservaObtida.getId()).isNotNull();
    }

    @Test
    void devePermitirBuscarReserva_IDNaoExiste(){
        var id = 1L;
        assertThatThrownBy(() -> reservaService.obterReserva(id)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deveObterTodasReservas() {
        Pageable pageable = PageRequest.of(0, 10);
        var id = 1L;
        var reservaDTO = gerarReservaDTO();
        var reserva = geraRestauranteMesaEUsuario();

        reservaDTO.setRestaurante(reserva.getRestaurante().getId());
        reservaDTO.setMesa(reserva.getMesa().getId());
        reservaDTO.setUsuario(reserva.getUsuario().getId());
        reservaService.reservarMesa(reservaDTO);
        Page<Reserva> reservasPage = reservaService.listarReservas(pageable);

        assertNotNull(reservasPage);
        assertTrue(reservasPage.hasContent());
    }

    @Test
    void devePermitirAtualizarReserva() {
        var id = 1L;
        var reservaDTO = gerarReservaDTO();
        var reserva = geraRestauranteMesaEUsuario();

        reservaDTO.setRestaurante(reserva.getRestaurante().getId());
        reservaDTO.setMesa(reserva.getMesa().getId());
        reservaDTO.setUsuario(reserva.getUsuario().getId());

        var reservaSalva = reservaService.reservarMesa(reservaDTO);

        var reservaAtualizada = reservaService.alterarReserva(reservaSalva.getId(), reservaDTO);

        assertThat(reservaAtualizada.getId()).isEqualTo(reservaSalva.getId());
        assertThat(reservaAtualizada.getPeriodoDe()).isEqualTo(reservaDTO.getPeriodoDe());
        assertThat(reservaAtualizada.getPeriodoAte()).isEqualTo(reservaDTO.getPeriodoAte());
    }

    @Test
    void verificaSePeriodoEstaDisponivelParaReserva_PeriodoDisponivel() {
        var id = 1L;
        var reservaDTO = gerarReservaDTO();
        var reserva = geraRestauranteMesaEUsuario();

        reservaDTO.setRestaurante(reserva.getRestaurante().getId());
        reservaDTO.setMesa(reserva.getMesa().getId());
        reservaDTO.setUsuario(reserva.getUsuario().getId());

        var reservaObtida = reservaService.reservarMesa(reservaDTO);

        reservaServiceImpl.verificaSePeriodoEstaDisponivelParaReserva(reserva);

        assertDoesNotThrow(() -> reservaServiceImpl.verificaSePeriodoEstaDisponivelParaReserva(reserva));


    }

    @Test
    void verificaSePeriodoEstaDisponivelParaReserva_PeriodoNaoDisponivel() {
        var id = 1L;
        var reservaDTO = gerarReservaDTO();
        var reserva = geraRestauranteMesaEUsuario();

        reservaDTO.setRestaurante(reserva.getRestaurante().getId());
        reservaDTO.setMesa(reserva.getMesa().getId());
        reservaDTO.setUsuario(reserva.getUsuario().getId());

        var reservaObtida = reservaService.reservarMesa(reservaDTO);

        assertThatThrownBy(() -> reservaServiceImpl.verificaSePeriodoEstaDisponivelParaReserva(reservaObtida)).isInstanceOf(BusinessException.class);
    }

    private Reserva geraRestauranteMesaEUsuario() {
        var reserva = gerarReserva();
        restauranteRepository.save(reserva.getRestaurante());
        mesaRepository.save(reserva.getMesa());
        usuarioRepository.save(reserva.getUsuario());

        return reserva;
    }

}
