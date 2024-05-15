package br.com.restaurante.fiap.techclallenge.controller;

import br.com.restaurante.fiap.techclallenge.entities.FiltroDeBuscaRequest;
import br.com.restaurante.fiap.techclallenge.entities.Restaurante;
import br.com.restaurante.fiap.techclallenge.entities.RestauranteDTO;
import br.com.restaurante.fiap.techclallenge.service.RestauranteService;
import br.com.restaurante.fiap.techclallenge.utils.RestauranteUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestauranteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RestauranteService restauranteService;

    @InjectMocks
    private RestauranteController restauranteController;


    AutoCloseable mock;

    @BeforeEach
    void setup(){
        mock = MockitoAnnotations.openMocks(this);
        RestauranteController restauranteController = new RestauranteController(restauranteService);
        mockMvc = MockMvcBuilders.standaloneSetup(restauranteController)
                .build();
    }
    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class RegistrarRestaurante{

        @Test
        void devePermitirRegistrarRestaurante() throws Exception {
            RestauranteDTO restauranteDTO = RestauranteUtils.gerarRestauranteDTO();
            Restaurante restaurante = RestauranteUtils.gerarRestaurante();

            when(restauranteService.cadastrarRestaurante(restauranteDTO)).thenReturn(restaurante);

            mockMvc.perform(post("/restaurante")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(asJsonString(restauranteDTO)))
                    .andExpect(status().isCreated());

            verify(restauranteService, times(1)).cadastrarRestaurante(any(RestauranteDTO.class));
        }
    }

    @Nested
    class BuscarRestaurante {

        @Test
        void devePermitirBuscarRestaurantePorId() throws Exception {
            int idRestaurante = 1;
            FiltroDeBuscaRequest filtro = new FiltroDeBuscaRequest();
            filtro.setFiltro("Pizzaria");
            int page = 0;
            int size = 10;
            Page<RestauranteDTO> paginaDeRestaurantes = criarPaginaDeRestaurantes();

            when(restauranteService.buscaRestaurantePorIdFiltro(eq(idRestaurante), eq(filtro.getFiltro()), any(PageRequest.class)))
                    .thenReturn(paginaDeRestaurantes);

            ResponseEntity<Page<RestauranteDTO>> resposta = restauranteController.buscaRestaurantePorId(idRestaurante, filtro, page, size);

            assertEquals(HttpStatus.OK, resposta.getStatusCode());
            assertEquals(paginaDeRestaurantes, resposta.getBody());
        }

        @Test
        void devePermitirBuscarRestauranteTodos() throws Exception {


            List<RestauranteDTO> restaurante = new ArrayList<>();
            restaurante.add(new RestauranteDTO());
            restaurante.add(new RestauranteDTO());
            Page<RestauranteDTO> paginaRestaurantes = new PageImpl<>(restaurante);

            PageRequest pageable = PageRequest.of(0, 10);

            when(restauranteService.buscarTodosRestaurantes(pageable)).thenReturn(paginaRestaurantes);

            ResponseEntity<Page<RestauranteDTO>> restaurantes = restauranteController.buscarTodosRestaurantes(pageable.getPageNumber(), pageable.getPageSize());

            assertEquals(HttpStatus.OK, restaurantes.getStatusCode());
            assertEquals(paginaRestaurantes, restaurantes.getBody());
            verify(restauranteService, times(1)).buscarTodosRestaurantes(pageable);
        }



    }

    public static String asJsonString(final Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper.writeValueAsString(object);
    }

    private Page<RestauranteDTO> criarPaginaDeRestaurantes() {
        List<RestauranteDTO> restaurantes = new ArrayList<>();
        return new PageImpl<>(restaurantes);
    }


}
