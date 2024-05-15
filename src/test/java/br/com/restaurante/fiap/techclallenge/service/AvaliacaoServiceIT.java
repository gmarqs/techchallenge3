package br.com.restaurante.fiap.techclallenge.service;

import br.com.restaurante.fiap.techclallenge.entities.Avaliacao;
import br.com.restaurante.fiap.techclallenge.entities.Restaurante;
import br.com.restaurante.fiap.techclallenge.entities.Usuario;
import br.com.restaurante.fiap.techclallenge.repository.RestauranteRepository;
import br.com.restaurante.fiap.techclallenge.repository.UsuarioRepository;
import br.com.restaurante.fiap.techclallenge.utils.AvaliacaoUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static br.com.restaurante.fiap.techclallenge.utils.AvaliacaoUtils.gerarAvaliacao;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor
public class AvaliacaoServiceIT {

    @Autowired
    private AvaliacaoService avaliacaoService;
    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void devePermitirRegistrarAvaliacao(){
        var avaliacaoDTO = AvaliacaoUtils.gerarAvaliacaoDTO();
        var avaliacao = geraRestauranteEUsuario();
        avaliacaoDTO.setUsuario(avaliacao.getUsuario().getId());
        avaliacaoDTO.setRestaurante(avaliacao.getRestaurante().getId());

        var resultadoObtido = avaliacaoService.cadastrarAvaliacao(avaliacaoDTO);

        assertThat(resultadoObtido).isNotNull().isInstanceOf(Avaliacao.class);

        assertThat(resultadoObtido.getId()).isNotNull();
        assertThat(resultadoObtido.getAvaliacao()).isNotNegative();

    }

    @Test
    void deveVisualizarAvaliacao(){
        var avaliacaoDTO = AvaliacaoUtils.gerarAvaliacaoDTO();
        var avaliacao = geraRestauranteEUsuario();
        avaliacaoDTO.setUsuario(avaliacao.getUsuario().getId());
        avaliacaoDTO.setRestaurante(avaliacao.getRestaurante().getId());

        var avaliacaoCadastrada = avaliacaoService.cadastrarAvaliacao(avaliacaoDTO);

        var resultadoObtido = avaliacaoService.visualizarAvaliacao(avaliacaoCadastrada.getId());

        assertThat(resultadoObtido).isNotNull().isInstanceOf(Avaliacao.class);
        assertThat(resultadoObtido.getAvaliacao()).isNotNegative();
    }

    @Test
    void deveVisualizarAvaliacao_IdNaoExiste(){
        var id = 1L;

        assertThatThrownBy(() -> avaliacaoService.visualizarAvaliacao(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void devePermitirExcluirAvaliacao(){
        var id = 1L;
        var avaliacaoDTO = AvaliacaoUtils.gerarAvaliacaoDTO();
        var avaliacao = geraRestauranteEUsuario();
        avaliacaoDTO.setUsuario(avaliacao.getUsuario().getId());
        avaliacaoDTO.setRestaurante(avaliacao.getRestaurante().getId());

        var avaliacaoCadastrada = avaliacaoService.cadastrarAvaliacao(avaliacaoDTO);


        var avaliacaoFoiRemovida = avaliacaoService.remover(avaliacaoCadastrada.getId());

        assertThat(avaliacaoFoiRemovida).isTrue();
    }

    @Test
    void devePermitirBuscarTodasAvaliacoes(){
        var avaliacaoDTO = AvaliacaoUtils.gerarAvaliacaoDTO();
        var avaliacao = geraRestauranteEUsuario();
        avaliacaoDTO.setUsuario(avaliacao.getUsuario().getId());
        avaliacaoDTO.setRestaurante(avaliacao.getRestaurante().getId());

        avaliacaoService.cadastrarAvaliacao(avaliacaoDTO);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Avaliacao> avaliacaoPage = avaliacaoService.listar(pageable);

        assertNotNull(avaliacaoPage);
        assertTrue(avaliacaoPage.hasContent());
    }

    @Test
    void devePermitirAlterarAvaliacao(){
        var avaliacaoDTO = AvaliacaoUtils.gerarAvaliacaoDTO();
        var avaliacao = geraRestauranteEUsuario();
        avaliacaoDTO.setUsuario(avaliacao.getUsuario().getId());
        avaliacaoDTO.setRestaurante(avaliacao.getRestaurante().getId());
        var avaliacaoNova = AvaliacaoUtils.gerarAvaliacaoDTO();

        avaliacaoNova.setAvaliacao(2);
        avaliacaoNova.setComentarios("Não gostei do atendimento");

        var avaliacaoCadastrada = avaliacaoService.cadastrarAvaliacao(avaliacaoDTO);

        var mensagemAlterada = avaliacaoService.alterar(avaliacaoNova, avaliacaoCadastrada.getId());

        assertThat(avaliacaoCadastrada.getId()).isEqualTo(mensagemAlterada.getId());
        assertThat(avaliacaoCadastrada.getUsuario()).isEqualTo(mensagemAlterada.getUsuario());
        assertThat(avaliacaoCadastrada.getAvaliacao()).isNotEqualTo(avaliacaoDTO.getAvaliacao());
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
