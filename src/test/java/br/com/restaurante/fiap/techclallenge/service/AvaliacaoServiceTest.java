package br.com.restaurante.fiap.techclallenge.service;

import br.com.restaurante.fiap.techclallenge.entities.Avaliacao;
import br.com.restaurante.fiap.techclallenge.entities.AvaliacaoDTO;
import br.com.restaurante.fiap.techclallenge.entities.Restaurante;
import br.com.restaurante.fiap.techclallenge.entities.Usuario;
import br.com.restaurante.fiap.techclallenge.repository.*;
import br.com.restaurante.fiap.techclallenge.utils.AvaliacaoUtils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static br.com.restaurante.fiap.techclallenge.utils.AvaliacaoUtils.gerarAvaliacao;
import static br.com.restaurante.fiap.techclallenge.utils.AvaliacaoUtils.gerarAvaliacaoDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AvaliacaoServiceTest {

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @InjectMocks
    private RestauranteServiceImpl restauranteService;

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AvaliacaoRepository avaliacaoRepository;
    @Mock
    private AvaliacaoService avaliacaoService;

    Avaliacao avaliacaoCompleta;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        restauranteService = new RestauranteServiceImpl(modelMapper, restauranteRepository);
        usuarioService = new UsuarioServiceImpl(usuarioRepository, modelMapper);
        avaliacaoService = new AvaliacaoServiceImpl(avaliacaoRepository, modelMapper, usuarioService, restauranteService);
        avaliacaoCompleta = geraRestauranteEUsuario();
    }

    @Test
    void devePermitirRegistrarAvaliacao(){
        AvaliacaoDTO avaliacaoDTO = gerarAvaliacaoDTO();
        Avaliacao avaliacao = gerarAvaliacao();

        Usuario usuario = avaliacao.getUsuario();
        Restaurante restaurante = avaliacao.getRestaurante();

        restaurante.setId(1L);
        usuario.setId(1L);
        avaliacaoDTO.setRestaurante(restaurante.getId());
        avaliacaoDTO.setUsuario(usuario.getId());

        when(restauranteRepository.findById(any())).thenReturn(Optional.of(restaurante));
        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuario));
        when(modelMapper.map(avaliacaoDTO, Avaliacao.class)).thenReturn(avaliacao);

        when(avaliacaoRepository.save(any(Avaliacao.class))).thenAnswer(invocation -> {
            Avaliacao avaliacaoSalva = invocation.getArgument(0);
            avaliacaoSalva.setId(1L);
            return avaliacaoSalva;
        });

        var avaliacaoRegistrada = avaliacaoService.cadastrarAvaliacao(avaliacaoDTO);

        assertThat(avaliacaoRegistrada).isInstanceOf(Avaliacao.class).isNotNull();
        assertThat(avaliacaoRegistrada.getId()).isNotNull();
    }

    @Test
    void deveVisualizarAvaliacao(){
        var id = 1L;
        avaliacaoCompleta.setId(id);

        when(avaliacaoRepository.findById(id)).thenReturn(Optional.of(avaliacaoCompleta));

        var avaliacaoObtida = avaliacaoService.visualizarAvaliacao(id);

        assertThat(avaliacaoObtida).isEqualTo(avaliacaoCompleta);

        verify(avaliacaoRepository,times(1)).findById(any(Long.class));

    }

    @Test
    void deveVisualizarAvaliacao_QuandoNaoExisteId(){
        var id = 1L;

        when(avaliacaoRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> avaliacaoService.visualizarAvaliacao(id)).isInstanceOf(EntityNotFoundException.class).hasMessage("Avaliação não encontrada");

        verify(avaliacaoRepository, times(1)).findById(id);

    }

    @Test
    void devePermitirExcluirAvaliacao(){
        var id = 1L;
        avaliacaoCompleta.setId(id);

        when(avaliacaoRepository.findById(id)).thenReturn(Optional.of(avaliacaoCompleta));
        doNothing().when(avaliacaoRepository).deleteById(id);

        var avaliacaoFoiRemovida = avaliacaoService.remover(id);

        assertThat(avaliacaoFoiRemovida).isTrue();
        verify(avaliacaoRepository, times(1)).findById(any(Long.class));
        verify(avaliacaoRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    void devePermitirBuscarTodasAvaliacoes(){
        Page<Avaliacao> listaDeAvaliacoes = new PageImpl<>(Arrays.asList(
                avaliacaoCompleta,
                avaliacaoCompleta
        ));

        when(avaliacaoRepository.findAll(any(Pageable.class))).thenReturn(listaDeAvaliacoes);

        var resultadoObtido = avaliacaoService.listar(Pageable.unpaged());

        assertThat(resultadoObtido).hasSize(2);
        assertThat(resultadoObtido.getContent()).asList().allSatisfy(avaliacao -> {
            assertThat(avaliacao).isNotNull().isInstanceOf(Avaliacao.class);
        });
    }

    @Test
    void devePermitirAlterarAvaliacao(){
        var id = 1L;
        var avaliacaoDTO = AvaliacaoUtils.gerarAvaliacaoDTO();
        var avaliacao = avaliacaoCompleta;
        avaliacao.setId(id);

        var avaliacaoNova = new Avaliacao();
        avaliacaoNova.setAvaliacao(2);
        avaliacaoNova.setComentarios("Não gostei do atendimento");

        when(avaliacaoRepository.findById(id)).thenReturn(Optional.of(avaliacao));
        when(modelMapper.map(avaliacaoDTO, Avaliacao.class)).thenReturn(avaliacaoNova);

        when(avaliacaoRepository.save(avaliacaoNova)).thenAnswer(i -> i.getArgument(0));

        var avaliacaoObtida = avaliacaoService.alterar(avaliacaoDTO, id);

        assertThat(avaliacaoObtida).isInstanceOf(Avaliacao.class).isNotNull();
        assertThat(avaliacaoObtida.getId()).isEqualTo(avaliacaoNova.getId());
        assertThat(avaliacao.getAvaliacao()).isNotEqualTo(avaliacaoNova.getAvaliacao());
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
