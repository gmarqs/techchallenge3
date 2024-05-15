package br.com.restaurante.fiap.techclallenge.controller;

import br.com.restaurante.fiap.techclallenge.entities.Reserva;
import br.com.restaurante.fiap.techclallenge.entities.ReservaDTO;
import br.com.restaurante.fiap.techclallenge.repository.MesaRepository;
import br.com.restaurante.fiap.techclallenge.repository.ReservaRepository;
import br.com.restaurante.fiap.techclallenge.repository.RestauranteRepository;
import br.com.restaurante.fiap.techclallenge.repository.UsuarioRepository;
import br.com.restaurante.fiap.techclallenge.utils.ReservaUtils;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static br.com.restaurante.fiap.techclallenge.utils.ReservaUtils.gerarReserva;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class ReservaControllerIT {
    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private MesaRepository mesaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ReservaRepository reservaRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup(){
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Nested
    class RegistrarReserva{
        @Test
        void devePermitirRegistrarReserva(){
            var reservaDTO = ReservaUtils.gerarReservaDTO();
            reservaDTO.setMesa(1L);
            reservaDTO.setRestaurante(1L);
            reservaDTO.setUsuario(1L);

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(reservaDTO)
                    .when()
                    .post("/reserva")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.CREATED.value())
                    .body(matchesJsonSchemaInClasspath("schemas/reservaRegistrar.schema.json"));

        }
    }

    @Nested
    class BuscarReserva{

        @Test
        void devePermitirBuscarReservaPorId() throws Exception {
            var id = 1L;
            var reserva = geraRestauranteMesaEUsuario();

            reservaRepository.save(reserva);

            RestAssured.when().get("/reserva/{id}", id).then().statusCode(HttpStatus.OK.value());
        }

        @Test
        void devePermitirBuscarReserva() throws Exception {

            given()
                    .queryParams("page", "0")
                    .queryParams("size", "10")

                    .when().get("/reserva").then().log().all().statusCode(HttpStatus.OK.value()).body(matchesJsonSchemaInClasspath("schemas/buscarReserva.schema.json"));
        }
    }

    @Nested
    class atualizarReserva{

        @Test
        void devePermitirAtualizarReserva() throws Exception {
            ReservaDTO reservaDTO = ReservaUtils.gerarReservaDTO();
            var id = 1L;
            reservaDTO.setMesa(1L);
            reservaDTO.setRestaurante(1L);
            reservaDTO.setUsuario(1L);
            Reserva reserva = geraRestauranteMesaEUsuario();

            reservaRepository.save(reserva);

            given().
                    when()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(reservaDTO)
                    .put("/reserva/{id}", id)
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.ACCEPTED.value());

        }
    }
    private Reserva geraRestauranteMesaEUsuario() {
        var reserva = gerarReserva();
        restauranteRepository.save(reserva.getRestaurante());
        mesaRepository.save(reserva.getMesa());
        usuarioRepository.save(reserva.getUsuario());

        return reserva;
    }

}
