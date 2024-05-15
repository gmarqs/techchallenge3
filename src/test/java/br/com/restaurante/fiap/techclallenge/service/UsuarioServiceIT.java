package br.com.restaurante.fiap.techclallenge.service;

import br.com.restaurante.fiap.techclallenge.entities.Restaurante;
import br.com.restaurante.fiap.techclallenge.entities.Usuario;
import br.com.restaurante.fiap.techclallenge.utils.RestauranteUtils;
import br.com.restaurante.fiap.techclallenge.utils.UsuarioUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor
public class UsuarioServiceIT {

    @Autowired
    private UsuarioService usuarioService;


    @Test
    void devePermitirRegistrarUsuario() {
        var usuarioDTO = UsuarioUtils.gerarUsuarioDTO();

        var usuarioObtido = usuarioService.criarUsuario(usuarioDTO);

        assertThat(usuarioObtido).isNotNull().isInstanceOf(Usuario.class);

        assertThat(usuarioObtido.getId()).isNotNull();
    }

    @Test
    void devePermitirBuscarUsuario(){
        var usuarioDTO = UsuarioUtils.gerarUsuarioDTO();

        var usuarioCadastrado = usuarioService.criarUsuario(usuarioDTO);

        var usuarioObtido = usuarioService.buscaUsuario(usuarioCadastrado.getId());

        assertThat(usuarioObtido).isInstanceOf(Usuario.class).isNotNull();
        assertThat(usuarioObtido.getId()).isNotNull();
    }
}
