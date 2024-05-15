package br.com.restaurante.fiap.techclallenge.service;

import br.com.restaurante.fiap.techclallenge.entities.Mesa;
import br.com.restaurante.fiap.techclallenge.entities.Reserva;
import br.com.restaurante.fiap.techclallenge.entities.Restaurante;
import br.com.restaurante.fiap.techclallenge.entities.enums.MesaStatus;
import br.com.restaurante.fiap.techclallenge.repository.MesaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.com.restaurante.fiap.techclallenge.helper.MesaHelper.criarMesa;

@Service
@RequiredArgsConstructor
public class MesaServiceImpl implements MesaService{

    private final MesaRepository mesaRepository;

    @Override
    public void buscaMesaDoRestaurante(Reserva reserva, Restaurante restaurante) {

    }

    @Override
    public Mesa buscaMesaPorId(Long id, Restaurante restaurante) {
        return mesaRepository.findByIdAndRestauranteId(id, restaurante.getId());
    }

    @Override
    public void alterarStatus(Mesa mesa) {
        mesa.setStatus(MesaStatus.OCUPADO);
        mesaRepository.save(mesa);
    }

    @Override
    public Mesa criaMesa(Mesa mesa) {
       return mesaRepository.save(mesa);
    }


}
