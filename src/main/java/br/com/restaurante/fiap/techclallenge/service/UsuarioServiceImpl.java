package br.com.restaurante.fiap.techclallenge.service;

import br.com.restaurante.fiap.techclallenge.entities.Usuario;
import br.com.restaurante.fiap.techclallenge.entities.UsuarioDTO;
import br.com.restaurante.fiap.techclallenge.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService{

    private final UsuarioRepository usuarioRepository;

    private final ModelMapper modelMapper;
    @Override
    public Usuario criarUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario buscaUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId).orElseThrow(()-> new EntityNotFoundException("Usuário não encontrado na base de dados"));
    }
}
