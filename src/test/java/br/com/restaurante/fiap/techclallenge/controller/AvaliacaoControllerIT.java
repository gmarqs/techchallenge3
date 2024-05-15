package br.com.restaurante.fiap.techclallenge.controller;

import br.com.restaurante.fiap.techclallenge.entities.Avaliacao;
import br.com.restaurante.fiap.techclallenge.entities.AvaliacaoDTO;
import br.com.restaurante.fiap.techclallenge.repository.AvaliacaoRepository;
import br.com.restaurante.fiap.techclallenge.repository.RestauranteRepository;
import br.com.restaurante.fiap.techclallenge.repository.UsuarioRepository;
import br.com.restaurante.fiap.techclallenge.utils.AvaliacaoUtils;
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

import static br.com.restaurante.fiap.techclallenge.utils.AvaliacaoUtils.gerarAvaliacao;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class AvaliacaoControllerIT {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @LocalServerPort
    private int port;

    private Avaliacao avaliacaoCompleta;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        avaliacaoCompleta = geraRestauranteEUsuario();
    }

    @Nested
    class RegistrarAvaliacao {
        @Test
        void devePermitirRegistrarAvaliacao() {
            var avaliacaoDTO = AvaliacaoUtils.gerarAvaliacaoDTO();

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(avaliacaoDTO)
                    .when()
                    .post("/avaliacao")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.CREATED.value());
            //.body(matchesJsonSchemaInClasspath("schemas/reservaRegistrar.schema.json"));

        }
    }

    @Nested
    class BuscarAvaliacao {

        @Test
        void devePermitirBuscarAvaliacaoPorId() throws Exception {
            var id = 1L;

            var avaliacaoSalva = avaliacaoRepository.save(avaliacaoCompleta);

            RestAssured.when().get("/avaliacao/{id}", avaliacaoSalva.getId()).then().statusCode(HttpStatus.OK.value());
        }

        @Test
        void devePermitirBuscarAvaliacao() throws Exception {

            given()
                    .queryParams("page", "0")
                    .queryParams("size", "10")

                    .when().get("/avaliacao").then().log().all().statusCode(HttpStatus.OK.value());//.body(matchesJsonSchemaInClasspath("schemas/buscarReserva.schema.json"));
        }
    }

    @Nested
    class atualizarAvaliacao {

        @Test
        void devePermitirAtualizarAvaliacao() throws Exception {
            AvaliacaoDTO avaliacaoDTO = AvaliacaoUtils.gerarAvaliacaoDTO();
            var id = 1L;
            avaliacaoDTO.setRestaurante(avaliacaoCompleta.getRestaurante().getId());
            avaliacaoDTO.setUsuario(avaliacaoCompleta.getUsuario().getId());
            avaliacaoDTO.setAvaliacao(2);
            avaliacaoDTO.setComentarios("Não gostei do atendimento");

            avaliacaoRepository.save(avaliacaoCompleta);

            given().
                    when()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(avaliacaoDTO)
                    .put("/avaliacao/{id}", id)
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.ACCEPTED.value());

        }

        @Nested
        class excluirAvaliacao {

            @Test
            void devePermitirExcluirAvaliacao() throws Exception {
                AvaliacaoDTO avaliacaoDTO = AvaliacaoUtils.gerarAvaliacaoDTO();
                var id = 1L;
                avaliacaoRepository.save(avaliacaoCompleta);

                given().
                        when()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(avaliacaoDTO)
                        .delete("/avaliacao/{id}", id)
                        .then()
                        .log().all()
                        .statusCode(HttpStatus.NO_CONTENT.value());

            }
        }
    }

    private Avaliacao geraRestauranteEUsuario() {
        var avaliacao = gerarAvaliacao();
        var restaurante = restauranteRepository.save(avaliacao.getRestaurante());
        var usuario = usuarioRepository.save(avaliacao.getUsuario());
        avaliacao.setRestaurante(restaurante);
        avaliacao.setUsuario(usuario);
        return avaliacao;
    }
}
