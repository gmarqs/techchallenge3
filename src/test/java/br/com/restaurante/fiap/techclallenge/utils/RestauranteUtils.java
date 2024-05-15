package br.com.restaurante.fiap.techclallenge.utils;

import br.com.restaurante.fiap.techclallenge.entities.AvaliacaoDTO;
import br.com.restaurante.fiap.techclallenge.entities.Restaurante;
import br.com.restaurante.fiap.techclallenge.entities.RestauranteDTO;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
@RequiredArgsConstructor
public class RestauranteUtils {

    public static RestauranteDTO gerarRestauranteDTO(){
        return RestauranteDTO.builder()
                .nome("Pizzaria Roma")
                .tipoCozinha("Pizza")
                .horarioFechamento(LocalTime.of(22,30))
                .horarioAbertura(LocalTime.of(9, 30))
                .localizacao("Rua Testando 262")
                .qntdMesas(300)
                .build();
    }

    public static Restaurante gerarRestaurante(){
        return Restaurante.builder()
                .nome("Pizzaria Roma")
                .tipoCozinha("Pizza")
                .horarioFechamento(LocalTime.of(22,30))
                .horarioAbertura(LocalTime.of(9, 30))
                .localizacao("Rua Testando 262")
                .qntdMesas(300)
                .build();
    }
}
