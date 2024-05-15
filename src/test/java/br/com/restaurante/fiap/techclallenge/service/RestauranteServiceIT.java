package br.com.restaurante.fiap.techclallenge.service;

import br.com.restaurante.fiap.techclallenge.entities.Restaurante;
import br.com.restaurante.fiap.techclallenge.entities.RestauranteDTO;
import br.com.restaurante.fiap.techclallenge.exceptions.RestauranteNotFoundException;
import br.com.restaurante.fiap.techclallenge.repository.RestauranteRepository;
import br.com.restaurante.fiap.techclallenge.utils.RestauranteUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor
class RestauranteServiceIT {

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Test
    void devePermitirRegistrarRestaurante() {
        var restauranteDTO = RestauranteUtils.gerarRestauranteDTO();

        var restauranteSalvo = restauranteService.cadastrarRestaurante(restauranteDTO);

        assertThat(restauranteSalvo).isNotNull().isInstanceOf(Restaurante.class);

        assertThat(restauranteSalvo.getId()).isNotNull();
    }

    @Test
    void devePermitirBuscarRestaurante(){
        var restauranteDTO = RestauranteUtils.gerarRestauranteDTO();

        var restauranteCadastrado = restauranteService.cadastrarRestaurante(restauranteDTO);

        var restauranteObtido = restauranteService.buscaRestaurante(restauranteCadastrado.getId());

        assertThat(restauranteObtido).isInstanceOf(Restaurante.class).isNotNull();
        assertThat(restauranteObtido.getId()).isNotNull();
    }

    @Test
    void devePermitirBuscarTodosRestaurantes(){
        Pageable pageable = PageRequest.of(0, 10);
        var restauranteDTO = RestauranteUtils.gerarRestauranteDTO();

        restauranteService.cadastrarRestaurante(restauranteDTO);

        var restaurantesLista = restauranteService.buscarTodosRestaurantes(pageable);


        assertNotNull(restaurantesLista);
        assertTrue(restaurantesLista.hasContent());
    }

    @Test
    void devePermitirBuscarRestaurante_IDNaoExiste(){
        var id = 1L;
        assertThatThrownBy(() -> restauranteService.buscaRestaurante(id)).isInstanceOf(RestauranteNotFoundException.class).hasMessage("Restaurante não foi encontrado");
    }

    @Test
    public void testBuscaRestaurantePorIdFiltro_PorNome() {
        String tipoDeBusca = "Pizzaria Roma";
        Pageable pageable = PageRequest.of(0, 10);
        var restauranteDTO = RestauranteUtils.gerarRestauranteDTO();

        restauranteService.cadastrarRestaurante(restauranteDTO);

        Page<RestauranteDTO> resultPage = restauranteService.buscaRestaurantePorIdFiltro(1, tipoDeBusca, pageable);

        assertFalse(resultPage.isEmpty(), "A página de resultados não deve estar vazia");

    }

    @Test
    public void testBuscaRestaurantePorIdFiltro_PorTipoCozinha() {
        String tipoDeBusca = "Rua Testando 262";
        Pageable pageable = PageRequest.of(0, 10);
        var restauranteDTO = RestauranteUtils.gerarRestauranteDTO();

        restauranteService.cadastrarRestaurante(restauranteDTO);

        Page<RestauranteDTO> resultPage = restauranteService.buscaRestaurantePorIdFiltro(2, tipoDeBusca, pageable);

        assertFalse(resultPage.isEmpty(), "A página de resultados não deve estar vazia");
    }

    @Test
    public void testBuscaRestaurantePorIdFiltro_PorLocalizacao() {
        String tipoDeBusca = "Pizza";
        Pageable pageable = PageRequest.of(0, 10); // Paginação inicial
        var restauranteDTO = RestauranteUtils.gerarRestauranteDTO();

        restauranteService.cadastrarRestaurante(restauranteDTO);

        Page<RestauranteDTO> resultPage = restauranteService.buscaRestaurantePorIdFiltro(3, tipoDeBusca, pageable);

        assertFalse(resultPage.isEmpty(), "A página de resultados não deve estar vazia");

    }




}
