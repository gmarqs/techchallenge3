package br.com.restaurante.fiap.techclallenge.service;

import br.com.restaurante.fiap.techclallenge.entities.Restaurante;
import br.com.restaurante.fiap.techclallenge.entities.Usuario;
import br.com.restaurante.fiap.techclallenge.entities.UsuarioDTO;
import br.com.restaurante.fiap.techclallenge.repository.UsuarioRepository;
import br.com.restaurante.fiap.techclallenge.utils.UsuarioUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private UsuarioServiceImpl usuarioService;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        usuarioService = new UsuarioServiceImpl(usuarioRepository, modelMapper);
    }

    @Test
    void devePermitirRegistrarUsuario(){
        UsuarioDTO usuarioDTO = UsuarioUtils.gerarUsuarioDTO();
        Usuario usuario = UsuarioUtils.gerarUsuario();
        usuario.setId(1L);
        when(modelMapper.map(usuarioDTO, Usuario.class)).thenReturn(usuario);
        Mockito.when(usuarioRepository.save(usuario)).thenAnswer(i -> i.getArgument(0));

        var usuarioCadastrado = usuarioService.criarUsuario(usuarioDTO);

        assertThat(usuarioCadastrado).isInstanceOf(Usuario.class).isNotNull();
        assertThat(usuarioCadastrado.getId()).isNotNull();

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void devePermitirBuscarUsuario(){
        var id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        var usuarioObtido = usuarioService.buscaUsuario(id);

        assertThat(usuarioObtido).isInstanceOf(Usuario.class).isNotNull();
        assertThat(usuarioObtido.getId()).isNotNull();

        verify(usuarioRepository, times(1)).findById(id);
    }
}
