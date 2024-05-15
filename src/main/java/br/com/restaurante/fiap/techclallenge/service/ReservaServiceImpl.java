package br.com.restaurante.fiap.techclallenge.service;

import br.com.restaurante.fiap.techclallenge.entities.*;
import br.com.restaurante.fiap.techclallenge.exceptions.BusinessException;
import br.com.restaurante.fiap.techclallenge.helper.MesaHelper;
import br.com.restaurante.fiap.techclallenge.repository.ReservaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService{


    private final ReservaRepository reservaRepository;

    private final RestauranteService restauranteService;

    private final MesaService mesaService;

    private final UsuarioService usuarioService;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Reserva reservarMesa(ReservaDTO reservaDTO) {

        Restaurante restaurante = restauranteService.buscaRestaurante(reservaDTO.getRestaurante());
        Usuario usuario = usuarioService.buscaUsuario(reservaDTO.getUsuario());

        Reserva reserva = modelMapper.map(reservaDTO, Reserva.class);

        if(reserva.getPeriodoDe().isAfter(reserva.getPeriodoAte())){
            throw new BusinessException("Período inicial é maior que o período final");
        }


        reserva.setRestaurante(restaurante);
        Mesa mesa = MesaHelper.criarMesa(reserva);


        mesa.setNumero(reservaDTO.getMesa());
        reserva.setUsuario(usuario);
        reserva.setMesa(mesa);

        mesaService.criaMesa(mesa);

        verificaSePeriodoEstaDisponivelParaReserva(reserva);
        return reservaRepository.save(reserva);

    }

    @Override
    public Reserva obterReserva(Long id) {
        return reservaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada"));
    }

    @Override
    public Page<Reserva> listarReservas(Pageable pageable) {
        return reservaRepository.listarReservas(pageable);
    }

    @Override
    public Reserva alterarReserva(Long id, ReservaDTO reservaDTO) {
        var reservaObtida = obterReserva(id);
        var reserva = modelMapper.map(reservaDTO, Reserva.class);

        reserva.setId(id);
        reserva.setRestaurante(reservaObtida.getRestaurante());
        reserva.setUsuario(reservaObtida.getUsuario());
        reserva.setMesa(reservaObtida.getMesa());

        return reservaRepository.save(reserva);
    }

    public void verificaSePeriodoEstaDisponivelParaReserva(Reserva reserva) {
        var reservaSelecionada = reservaRepository.verificarSePeriodoEstaDisponivelParaReserva(reserva.getRestaurante().getId(), reserva.getMesa().getNumero(), reserva.getPeriodoDe(), reserva.getPeriodoAte());

        if(reservaSelecionada != null)
            throw new BusinessException("Já existe reserva para a mesa " + reserva.getMesa().getNumero() + " no horario escolhido");
    }


}
