package br.com.restaurante.fiap.techclallenge.utils;

import br.com.restaurante.fiap.techclallenge.entities.Usuario;
import br.com.restaurante.fiap.techclallenge.entities.UsuarioDTO;

public class UsuarioUtils {


    public static UsuarioDTO gerarUsuarioDTO() {
        return UsuarioDTO.builder()
                .nome("Gabriel")
                .telefone("13123456789")
                .email("teste@teste.com.br")
                .cpf("43497256013")
                .build();
    }

    public static Usuario gerarUsuario() {
        return Usuario.builder()
                .nome("Gabriel")
                .telefone("13123456789")
                .email("teste@teste.com.br")
                .cpf("43497256013")
                .build();
    }
}
