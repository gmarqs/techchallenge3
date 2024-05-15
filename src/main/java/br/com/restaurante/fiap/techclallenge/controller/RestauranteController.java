package br.com.restaurante.fiap.techclallenge.controller;

import br.com.restaurante.fiap.techclallenge.entities.FiltroDeBuscaRequest;
import br.com.restaurante.fiap.techclallenge.entities.Restaurante;
import br.com.restaurante.fiap.techclallenge.entities.RestauranteDTO;
import br.com.restaurante.fiap.techclallenge.service.RestauranteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("restaurante")
@RequiredArgsConstructor
public class RestauranteController {

    private final RestauranteService restauranteService;

    @PostMapping
    public ResponseEntity<Restaurante> cadastraRestaurante(@RequestBody @Valid RestauranteDTO restauranteDTO){
        var restauranteCadastrado = restauranteService.cadastrarRestaurante(restauranteDTO);

        return new ResponseEntity<>(restauranteCadastrado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<RestauranteDTO>> buscarTodosRestaurantes(@RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size){

        var lista = restauranteService.buscarTodosRestaurantes(PageRequest.of(page, size));

        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Page<RestauranteDTO>> buscaRestaurantePorId(@PathVariable int id, @RequestBody FiltroDeBuscaRequest filtro,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size){

        var restaurante = restauranteService.buscaRestaurantePorIdFiltro(id, filtro.getFiltro(), PageRequest.of(page, size));

        return new ResponseEntity<>(restaurante, HttpStatus.OK);
    }
}
