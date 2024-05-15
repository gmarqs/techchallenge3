package br.com.restaurante.fiap.techclallenge.service;

import br.com.restaurante.fiap.techclallenge.entities.Usuario;
import br.com.restaurante.fiap.techclallenge.entities.UsuarioDTO;

public interface UsuarioService {

    Usuario criarUsuario(UsuarioDTO usuarioDTO);

    Usuario buscaUsuario(Long usuario);
}
