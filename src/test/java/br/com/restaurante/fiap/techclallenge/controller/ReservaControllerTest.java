package br.com.restaurante.fiap.techclallenge.controller;

import br.com.restaurante.fiap.techclallenge.entities.Reserva;
import br.com.restaurante.fiap.techclallenge.entities.ReservaDTO;
import br.com.restaurante.fiap.techclallenge.utils.ReservaUtils;
import br.com.restaurante.fiap.techclallenge.service.ReservaService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReservaService reservaService;

    @InjectMocks
    private ReservaController reservaController;


    AutoCloseable mock;

    @BeforeEach
    void setup(){
        mock = MockitoAnnotations.openMocks(this);
        ReservaController reservaController = new ReservaController(reservaService);
        mockMvc = MockMvcBuilders.standaloneSetup(reservaController)
                .build();
    }
    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class RegistrarReserva{

        @Test
        void devePermitirRegistrarReserva() throws Exception {
            ReservaDTO reservaDTO = ReservaUtils.gerarReservaDTO();
            Reserva reserva = ReservaUtils.gerarReserva();

            when(reservaService.reservarMesa(reservaDTO)).thenReturn(reserva);

            mockMvc.perform(post("/reserva")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(asJsonString(reservaDTO)))
                    .andExpect(status().isCreated());

            verify(reservaService, times(1)).reservarMesa(any(ReservaDTO.class));
        }
    }

    @Nested
    class BuscarReserva{

        @Test
        void devePermitirBuscarReservaPorId() throws Exception {
            ReservaDTO reservaDTO = ReservaUtils.gerarReservaDTO();
            Reserva reserva = ReservaUtils.gerarReserva();
            var id = 1L;

            when(reservaService.obterReserva(any(Long.class))).thenReturn(reserva);

            mockMvc.perform(get("/reserva/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(asJsonString(reservaDTO)))
                    .andExpect(status().isOk());

            verify(reservaService, times(1)).obterReserva(any(Long.class));
        }

        @Test
        void devePermitirBuscarReserva() throws Exception {


            List<Reserva> reservas = new ArrayList<>();
            reservas.add(new Reserva());
            reservas.add(new Reserva());
            Page<Reserva> paginaReservas = new PageImpl<>(reservas);

            PageRequest pageable = PageRequest.of(0, 10);

            when(reservaService.listarReservas(pageable)).thenReturn(paginaReservas);

            ResponseEntity<Page<Reserva>> response = reservaController.listarReservas(pageable);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(paginaReservas, response.getBody());
            verify(reservaService, times(1)).listarReservas(pageable);
        }
    }

    @Nested
    class atualizarReserva{

        @Test
        void devePermitirAtualizarReserva() throws Exception {
            ReservaDTO reservaDTO = ReservaUtils.gerarReservaDTO();
            Reserva reserva = ReservaUtils.gerarReserva();
            var id = 1L;
            when(reservaService.alterarReserva(id, reservaDTO)).thenReturn(reserva);

            mockMvc.perform(put("/reserva/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(asJsonString(reservaDTO)))
                    .andExpect(status().isAccepted());

            verify(reservaService, times(1)).alterarReserva(any(Long.class), any(ReservaDTO.class));


        }




    }

    public static String asJsonString(final Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper.writeValueAsString(object);

    }
}
