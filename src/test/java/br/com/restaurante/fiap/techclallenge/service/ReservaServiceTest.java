package br.com.restaurante.fiap.techclallenge.service;

import br.com.restaurante.fiap.techclallenge.entities.*;
import br.com.restaurante.fiap.techclallenge.exceptions.BusinessException;
import br.com.restaurante.fiap.techclallenge.repository.MesaRepository;
import br.com.restaurante.fiap.techclallenge.repository.ReservaRepository;
import br.com.restaurante.fiap.techclallenge.repository.RestauranteRepository;
import br.com.restaurante.fiap.techclallenge.repository.UsuarioRepository;
import br.com.restaurante.fiap.techclallenge.utils.ReservaUtils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @InjectMocks
    private RestauranteServiceImpl restauranteService;

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private MesaRepository mesaRepository;
    @InjectMocks
    private MesaServiceImpl mesaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        restauranteService = new RestauranteServiceImpl(modelMapper, restauranteRepository);
        reservaService = new ReservaServiceImpl(reservaRepository, restauranteService, mesaService ,usuarioService, modelMapper);
    }

    @Test
    void devePermitirRegistrarReserva(){
        ReservaDTO reservaDTO = ReservaUtils.gerarReservaDTO();
        var reserva = ReservaUtils.gerarReserva();
        var usuario = reserva.getUsuario();
        var mesa = reserva.getMesa();
        Restaurante restaurante = reserva.getRestaurante();

        restaurante.setId(1L);
        usuario.setId(1L);
        mesa.setId(1L);
        reserva.setId(1L);

        when(restauranteRepository.findById(any())).thenReturn(Optional.of(restaurante));
        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuario));
        when(modelMapper.map(reservaDTO, Reserva.class)).thenReturn(reserva);
        when(mesaRepository.save(mesa)).thenReturn(mesa);

        when(reservaRepository.save(reserva)).thenAnswer(i -> i.getArgument(0));


        var reservaRegistrada = reservaService.reservarMesa(reservaDTO);

        assertThat(reservaRegistrada).isInstanceOf(Reserva.class).isNotNull();
        assertThat(reservaRegistrada.getId()).isNotNull();
    }

    @Test
    void devePermitirRegistrarReservar_DatasInvalidas(){
        ReservaDTO reservaDTO = ReservaUtils.gerarReservaDTO();
        var reserva = ReservaUtils.gerarReserva();
        var usuario = reserva.getUsuario();
        var mesa = reserva.getMesa();
        Restaurante restaurante = reserva.getRestaurante();

        restaurante.setId(1L);
        usuario.setId(1L);
        mesa.setId(1L);
        reserva.setId(1L);

        reserva.setPeriodoDe(LocalDateTime.now().plusHours(1));
        reserva.setPeriodoAte(LocalDateTime.now());

        when(restauranteRepository.findById(any())).thenReturn(Optional.of(restaurante));
        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuario));
        when(modelMapper.map(reservaDTO, Reserva.class)).thenReturn(reserva);
        when(mesaRepository.save(mesa)).thenReturn(mesa);
        when(reservaRepository.save(reserva)).thenAnswer(i -> i.getArgument(0));

        assertThatThrownBy(() -> reservaService.reservarMesa(reservaDTO)).isInstanceOf(BusinessException.class).hasMessage("Período inicial é maior que o período final");

    }
    @Test
    void devePermitirBuscarReserva(){
        var id = 1L;
        var reserva = ReservaUtils.gerarReserva();

        when(reservaRepository.findById(id)).thenReturn(Optional.of(reserva));

        var reservaObtida = reservaService.obterReserva(id);

        assertThat(reservaObtida).isNotNull();

        verify(reservaRepository, times(1)).findById(id);
    }

    @Test
    void devePermitirBuscarReserva_IDNaoExiste(){
        var id = 1L;

        when(reservaRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservaService.obterReserva(id)).isInstanceOf(EntityNotFoundException.class).hasMessage("Reserva não encontrada");

        verify(reservaRepository, times(1)).findById(id);
    }

    @Test
    void deveObterTodasReservas() {
        List<Reserva> reservas = Arrays.asList(ReservaUtils.gerarReserva(), ReservaUtils.gerarReserva());

        when(reservaRepository.listarReservas(Pageable.unpaged())).thenReturn(new PageImpl<>(reservas));

        Page<Reserva> resultado = reservaService.listarReservas(Pageable.unpaged());

        assertEquals(reservas.size(), resultado.getContent().size());
    }

    @Test
    void devePermitirAtualizarReserva() {
        var id = 1L;
        ReservaDTO reservaDTO = ReservaUtils.gerarReservaDTO();
        var reserva = ReservaUtils.gerarReserva();

        reserva.setId(id);

        when(modelMapper.map(reservaDTO, Reserva.class)).thenReturn(reserva);
        when(reservaRepository.findById(reserva.getId())).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(reserva)).thenReturn(reserva);

        Reserva reservaAtualizada = reservaService.alterarReserva(reserva.getId(), reservaDTO);

        verify(reservaRepository, times(1)).findById(1L);
        verify(reservaRepository, times(1)).save(reserva);
        assertThat(reservaAtualizada).isNotNull().isInstanceOf(Reserva.class);
        assertEquals(reservaAtualizada, reserva);

    }

    @Test
    void verificaSePeriodoEstaDisponivelParaReserva_PeriodoDisponivel() {
        var id = 1L;
        var reserva = ReservaUtils.gerarReserva();
        reserva.setId(id);

        when(reservaRepository.verificarSePeriodoEstaDisponivelParaReserva(anyLong(), anyLong(), any(), any()))
                .thenReturn(null);

        reservaService.verificaSePeriodoEstaDisponivelParaReserva(reserva);

        verify(reservaRepository).verificarSePeriodoEstaDisponivelParaReserva(reserva.getRestaurante().getId(), reserva.getMesa().getNumero(), reserva.getPeriodoDe(), reserva.getPeriodoAte());

    }

    @Test
    void verificaSePeriodoEstaDisponivelParaReserva_PeriodoNaoDisponivel() {
        var id = 1L;
        Reserva reserva = ReservaUtils.gerarReserva();
        reserva.getRestaurante().setId(1L);
        reserva.setId(id);

        when(reservaRepository.verificarSePeriodoEstaDisponivelParaReserva(eq(reserva.getRestaurante().getId()), eq(reserva.getMesa().getNumero()), any(), any()))
                .thenReturn(reserva);

        assertThatThrownBy(() -> reservaService.verificaSePeriodoEstaDisponivelParaReserva(reserva)).isInstanceOf(BusinessException.class).hasMessage("Já existe reserva para a mesa " + reserva.getMesa().getNumero() + " no horario escolhido");

        verify(reservaRepository).verificarSePeriodoEstaDisponivelParaReserva(anyLong(), anyLong(), any(), any());
    }








}
