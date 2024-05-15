package br.com.restaurante.fiap.techclallenge.repository;

import br.com.restaurante.fiap.techclallenge.entities.Avaliacao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static br.com.restaurante.fiap.techclallenge.utils.AvaliacaoUtils.gerarAvaliacao;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


public class AvaliacaoRepositoryTest {

    @Mock
    private AvaliacaoRepository avaliacaoRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setup(){
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception{
        openMocks.close();
    }
    @Test
    void devePermitirRegistrarAvaliacao(){
        var avaliacao = gerarAvaliacao();

        when(avaliacaoRepository.save(any(Avaliacao.class))).thenReturn(avaliacao);

        var avaliacaoRegistrada = avaliacaoRepository.save(avaliacao);

        assertThat(avaliacaoRegistrada).isNotNull().isEqualTo(avaliacao);

        verify(avaliacaoRepository, times(1)).save(any(Avaliacao.class));
    }

    @Test
    void devePermitirVisualizarAvaliacao(){
        var avaliacao = gerarAvaliacao();
        var id = 1L;
        when(avaliacaoRepository.findById(any(Long.class))).thenReturn(Optional.of(avaliacao));

        var avaliacaoObtida = avaliacaoRepository.findById(id);

        assertThat(avaliacaoObtida).isPresent().isNotNull();

        verify(avaliacaoRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void devePermitirExcluirAvaliacao(){
        var id = 1L;

        doNothing().when(avaliacaoRepository).deleteById(any(Long.class));

        avaliacaoRepository.deleteById(id);

        verify(avaliacaoRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    void devePermitirListarTodasAvaliacoes(){
        var avaliacoesList = Arrays.asList(gerarAvaliacao(), gerarAvaliacao());

        when(avaliacaoRepository.findAll()).thenReturn(avaliacoesList);

        var listaAvaliacoes = avaliacaoRepository.findAll();

        assertThat(listaAvaliacoes).hasSize(2);
        verify(avaliacaoRepository, times(1)).findAll();
    }

}
