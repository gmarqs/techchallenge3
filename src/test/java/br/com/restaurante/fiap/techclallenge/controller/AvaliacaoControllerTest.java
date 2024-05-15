package br.com.restaurante.fiap.techclallenge.controller;

import br.com.restaurante.fiap.techclallenge.entities.Avaliacao;
import br.com.restaurante.fiap.techclallenge.entities.AvaliacaoDTO;
import br.com.restaurante.fiap.techclallenge.service.AvaliacaoService;
import br.com.restaurante.fiap.techclallenge.utils.AvaliacaoUtils;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AvaliacaoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AvaliacaoService avaliacaoService;

    @InjectMocks
    private AvaliacaoController avaliacaoController;


    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        AvaliacaoController avaliacaoController = new AvaliacaoController(avaliacaoService);
        mockMvc = MockMvcBuilders.standaloneSetup(avaliacaoController)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class RegistrarAvaliacao {

        @Test
        void devePermitirRegistrarAvaliacao() throws Exception {
            AvaliacaoDTO reservaDTO = AvaliacaoUtils.gerarAvaliacaoDTO();
            Avaliacao avaliacao = AvaliacaoUtils.gerarAvaliacao();

            when(avaliacaoService.cadastrarAvaliacao(reservaDTO)).thenReturn(avaliacao);

            mockMvc.perform(post("/avaliacao")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(asJsonString(reservaDTO)))
                    .andExpect(status().isCreated());

            verify(avaliacaoService, times(1)).cadastrarAvaliacao(any(AvaliacaoDTO.class));
        }
    }

    @Nested
    class BuscarAvaliacao {

        @Test
        void devePermitirBuscarAvaliacaoPorId() throws Exception {
            Avaliacao avaliacao = AvaliacaoUtils.gerarAvaliacao();
            var id = 1L;
            avaliacao.setId(id);

            when(avaliacaoService.visualizarAvaliacao(id)).thenReturn(avaliacao);

            mockMvc.perform(get("/avaliacao/{id}", id))
                    .andExpect(status().isOk());

            verify(avaliacaoService, times(1)).visualizarAvaliacao(any(Long.class));
        }

        @Test
        void devePermitirBuscarAvaliacoes() throws Exception {


            List<Avaliacao> avaliacoes = new ArrayList<>();
            avaliacoes.add(new Avaliacao());
            avaliacoes.add(new Avaliacao());
            Page<Avaliacao> paginaAvaliacoes = new PageImpl<>(avaliacoes);

            PageRequest pageable = PageRequest.of(0, 10);

            when(avaliacaoService.listar(pageable)).thenReturn(paginaAvaliacoes);

            ResponseEntity<Page<Avaliacao>> response = avaliacaoController.listarAvaliacoes(pageable);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(paginaAvaliacoes, response.getBody());
            verify(avaliacaoService, times(1)).listar(pageable);
        }
    }

    @Nested
    class atualizarAvaliacao {

        @Test
        void devePermitirAtualizarAvaliacao() throws Exception {
            AvaliacaoDTO avaliacaoDTO = AvaliacaoUtils.gerarAvaliacaoDTO();
            Avaliacao avaliacao = AvaliacaoUtils.gerarAvaliacao();
            var id = 1L;
            when(avaliacaoService.alterar(avaliacaoDTO, id)).thenReturn(avaliacao);

            mockMvc.perform(put("/avaliacao/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(asJsonString(avaliacaoDTO)))
                    .andExpect(status().isAccepted());

            verify(avaliacaoService, times(1)).alterar(any(AvaliacaoDTO.class), any(Long.class));


        }

    }

    @Nested
    class excluirAvaliacao {

        @Test
        void devePermitirExcluirAvaliacao() throws Exception {
            mockMvc.perform(delete("/avaliacao/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            verify(avaliacaoService).remover(1L);
        }

    }
    public static String asJsonString(final Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper.writeValueAsString(object);

    }
}
