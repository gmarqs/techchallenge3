package br.com.restaurante.fiap.techclallenge.utils;

import br.com.restaurante.fiap.techclallenge.entities.Avaliacao;
import br.com.restaurante.fiap.techclallenge.entities.AvaliacaoDTO;
import br.com.restaurante.fiap.techclallenge.entities.Restaurante;
import br.com.restaurante.fiap.techclallenge.entities.Usuario;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@RequiredArgsConstructor
public abstract class AvaliacaoUtils {

    public static AvaliacaoDTO gerarAvaliacaoDTO(){
        return AvaliacaoDTO.builder()
                .avaliacao(5)
                .restaurante(1L)
                .usuario(1L)
                .comentarios("Melhor Restaurante")
                .build();
    }

    public static Avaliacao gerarAvaliacao(){

        Restaurante restaurante = Restaurante.builder()
                // .id(1L)
                .horarioAbertura(LocalTime.now())
                .horarioFechamento(LocalTime.now().plusHours(8))
                .localizacao("Rua Teste testando")
                .lotacaoAtual(20)
                .nome("Restaurante")
                .tipoCozinha("Hamburguer")
                .qntdMesas(300)
                .build();

        Usuario usuario = Usuario.builder()
                .nome("Nome Teste")
                .cpf("123456789")
                .email("teste@teste.com")
                .telefone("13123456789")
                .build();

        return Avaliacao.builder()
                .avaliacao(5)
                .comentarios("Melhor Restaurante")
                 .restaurante(restaurante)
                 .usuario(usuario)
                .build();

    }


}
