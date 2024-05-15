package br.com.restaurante.fiap.techclallenge.service;

import br.com.restaurante.fiap.techclallenge.entities.Restaurante;
import br.com.restaurante.fiap.techclallenge.entities.RestauranteDTO;
import br.com.restaurante.fiap.techclallenge.exceptions.IdFilterNotExistException;
import br.com.restaurante.fiap.techclallenge.exceptions.RestauranteNotFoundException;
import br.com.restaurante.fiap.techclallenge.repository.RestauranteRepository;
import br.com.restaurante.fiap.techclallenge.utils.RestauranteUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RestauranteServiceTest {

    @InjectMocks
    private RestauranteServiceImpl restauranteService;

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        restauranteService = new RestauranteServiceImpl(modelMapper, restauranteRepository);
    }

    @Test
    void devePermitirRegistrarRestaurante(){
        RestauranteDTO restauranteDTO = RestauranteUtils.gerarRestauranteDTO();
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);

        when(modelMapper.map(restauranteDTO, Restaurante.class)).thenReturn(restaurante);
        when(restauranteRepository.save(restaurante)).thenAnswer(i -> i.getArgument(0));

        var restauranteRegistrado = restauranteService.cadastrarRestaurante(restauranteDTO);

        assertThat(restauranteRegistrado).isInstanceOf(Restaurante.class).isNotNull();
        assertThat(restauranteRegistrado.getId()).isNotNull();

        verify(restauranteRepository, times(1)).save(any(Restaurante.class));
    }

    @Test
    void devePermitirBuscarRestaurante(){
        var id = 1L;
        Restaurante restaurante = new Restaurante();
        restaurante.setId(id);
        when(restauranteRepository.findById(id)).thenReturn(Optional.of(restaurante));

        var restauranteObtido = restauranteService.buscaRestaurante(id);

        assertThat(restauranteObtido).isInstanceOf(Restaurante.class).isNotNull();
        assertThat(restauranteObtido.getId()).isNotNull();

        verify(restauranteRepository, times(1)).findById(id);
    }

    @Test
    void devePermitirBuscarTodosRestaurantes(){
        List<Restaurante> restaurantes = Arrays.asList(new Restaurante(), new Restaurante());

        when(restauranteRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(restaurantes));

        Page<RestauranteDTO> restaurantePage = restauranteService.buscarTodosRestaurantes(Pageable.unpaged());

        assertEquals(restaurantes.size(), restaurantePage.getContent().size());
    }

    @Test
    void devePermitirBuscarRestaurante_IDNaoExiste(){
        var id = 1L;

        when(restauranteRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> restauranteService.buscaRestaurante(id)).isInstanceOf(RestauranteNotFoundException.class).hasMessage("Restaurante não foi encontrado");

        verify(restauranteRepository, times(1)).findById(id);
    }

    @Test
    void devePermitirBuscarRestaurantePorFiltroNome(){
        Pageable pageable = PageRequest.of(0, 10);
        List<Restaurante> restaurantes = Arrays.asList(
                new Restaurante(),
                new Restaurante()
        );
        Page<Restaurante> page = new PageImpl<>(restaurantes);
        when(restauranteRepository.listarRestaurantes(any(), any(), any(), eq(pageable))).thenReturn(page);

        Page<RestauranteDTO> resultPage = restauranteService.buscaRestaurantePorIdFiltro(1, "Pizzaria", pageable);

        assertFalse(resultPage.isEmpty(), "A página de resultados não deve estar vazia");

        verify(restauranteRepository).listarRestaurantes("Pizzaria", null, null, pageable);
    }

    @Test
    void devePermitirBuscarRestaurantePorFiltroLocalizacao(){
        Pageable pageable = PageRequest.of(0, 10);
        List<Restaurante> restaurantes = Arrays.asList(
                new Restaurante(),
                new Restaurante()
        );
        Page<Restaurante> page = new PageImpl<>(restaurantes);
        when(restauranteRepository.listarRestaurantes(any(), any(), any(), eq(pageable))).thenReturn(page);

        Page<RestauranteDTO> resultPage = restauranteService.buscaRestaurantePorIdFiltro(2, "Santos", pageable);

        assertFalse(resultPage.isEmpty(), "A página de resultados não deve estar vazia");

        verify(restauranteRepository).listarRestaurantes(null, "Santos", null, pageable);
    }

    @Test
    void devePermitirBuscarRestaurantePorFiltroTipoCozinha(){
        Pageable pageable = PageRequest.of(0, 10);
        List<Restaurante> restaurantes = Arrays.asList(
                new Restaurante(),
                new Restaurante()
        );
        Page<Restaurante> page = new PageImpl<>(restaurantes);
        when(restauranteRepository.listarRestaurantes(any(), any(), any(), eq(pageable))).thenReturn(page);

        Page<RestauranteDTO> resultPage = restauranteService.buscaRestaurantePorIdFiltro(3, "Hamburgueria", pageable);

        assertFalse(resultPage.isEmpty(), "A página de resultados não deve estar vazia");

        verify(restauranteRepository).listarRestaurantes(null, null, "Hamburgueria", pageable);
    }

    @Test
    void devePermitirBuscarRestaurantePorFiltroException_RestauranteNotFound(){
        Pageable pageable = PageRequest.of(0, 10);
        List<Restaurante> restaurantes = Arrays.asList(
                new Restaurante(),
                new Restaurante()
        );
        Page<Restaurante> page = new PageImpl<>(restaurantes);
        when(restauranteRepository.listarRestaurantes(any(), any(), any(), eq(pageable))).thenReturn(Page.empty());

        assertThatThrownBy(() -> restauranteService.buscaRestaurantePorIdFiltro(3, "Hamburgueria", pageable))
                .isInstanceOf(RestauranteNotFoundException.class)
                .hasMessage("Restaurante filtrado não existe na base de dados.");

        verify(restauranteRepository).listarRestaurantes(null, null, "Hamburgueria", pageable);
    }

    @Test
    void devePermitirBuscarRestaurantePorFiltroException_IdFilterNotExist(){
        Pageable pageable = PageRequest.of(0, 10);

        assertThatThrownBy(() -> restauranteService.buscaRestaurantePorIdFiltro(4, "Hamburgueria", pageable))
                .isInstanceOf(IdFilterNotExistException.class)
                .hasMessage("Valor numérico inválido: " + 4);

    }

}
