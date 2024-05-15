package br.com.restaurante.fiap.techclallenge.utils;

import br.com.restaurante.fiap.techclallenge.entities.*;
import br.com.restaurante.fiap.techclallenge.entities.enums.MesaStatus;
import br.com.restaurante.fiap.techclallenge.repository.MesaRepository;
import br.com.restaurante.fiap.techclallenge.repository.ReservaRepository;
import br.com.restaurante.fiap.techclallenge.repository.RestauranteRepository;
import br.com.restaurante.fiap.techclallenge.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RequiredArgsConstructor
public abstract class ReservaUtils {

    private final RestauranteRepository restauranteRepository;
    private final MesaRepository mesaRepository;
    private final UsuarioRepository usuarioRepository;


    public static Reserva gerarReserva(){

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

        Mesa mesa = Mesa.builder()
               // .id(2L)
                .periodoDe(LocalDateTime.now().plusDays(1))
                .periodoAte(LocalDateTime.now().plusDays(1))
                .status(MesaStatus.OCUPADO)
                .numero(2L)
                .restaurante(restaurante)
                .build();

        Usuario usuario = Usuario.builder()
                .nome("Nome Teste")
                .cpf("123456789")
                .email("teste@teste.com")
                .telefone("13123456789")
                .build();


        return Reserva.builder()
                .restaurante(restaurante)
                .mesa(mesa)
                .usuario(usuario)
                .periodoDe(LocalDateTime.now())
                .periodoAte(LocalDateTime.now().plusHours(2))
                .build();
    }


    public static ReservaDTO gerarReservaDTO(){
        return ReservaDTO.builder()
                //.mesa(1L)
                //.usuario(1L)
                //.restaurante(1L)
                .periodoDe(LocalDateTime.now().plusHours(1))
                .periodoAte(LocalDateTime.now().plusHours(2))
                .build();

    }

}
