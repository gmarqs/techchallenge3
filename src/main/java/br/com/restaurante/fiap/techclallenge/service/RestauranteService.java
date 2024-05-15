package br.com.restaurante.fiap.techclallenge.service;

import br.com.restaurante.fiap.techclallenge.entities.Restaurante;
import br.com.restaurante.fiap.techclallenge.entities.RestauranteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestauranteService {

    Restaurante cadastrarRestaurante(RestauranteDTO restauranteDTO);

    Restaurante buscaRestaurante(Long id);

    Page<RestauranteDTO> buscarTodosRestaurantes(Pageable pageable);

    Page<RestauranteDTO> buscaRestaurantePorIdFiltro(int id, String filtro, Pageable pageable);

}
