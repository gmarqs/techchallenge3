package br.com.restaurante.fiap.techclallenge.repository;

import br.com.restaurante.fiap.techclallenge.entities.Avaliacao;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static br.com.restaurante.fiap.techclallenge.utils.AvaliacaoUtils.gerarAvaliacao;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class AvaliacaoRepositoryIT {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void devePermitirRegistrarAvaliacoes(){
        var avaliacao = geraRestauranteEUsuario();

        var avaliacaoRecebida = avaliacaoRepository.save(avaliacao);

        assertThat(avaliacaoRecebida).isInstanceOf(Avaliacao.class).isNotNull();
    }

    @Test
    void devePermitirBuscarAvaliacao(){
        var avaliacao = geraRestauranteEUsuario();

        var avaliacaoSalva = avaliacaoRepository.save(avaliacao);

        var avaliacaoOptional = avaliacaoRepository.findById(avaliacaoSalva.getId());

        assertThat(avaliacaoOptional).isPresent();
    }

    @Test
    void devePermitirExcluirAvaliacao(){
        var avaliacao = geraRestauranteEUsuario();

        var avaliacaoSalva = avaliacaoRepository.save(avaliacao);

        avaliacaoRepository.deleteById(avaliacaoSalva.getId());

        var avaliacaoRecebidaOpcional = avaliacaoRepository.findById(avaliacaoSalva.getId());

        assertThat(avaliacaoRecebidaOpcional).isEmpty();
    }

    @Test
    void devePermitirListarAvaliacoes(){
        var avaliacao = geraRestauranteEUsuario();

        avaliacaoRepository.save(avaliacao);
        var resultadosObtidos = avaliacaoRepository.findAll();

        assertThat(resultadosObtidos).hasSizeGreaterThan(0);
    }

    private Avaliacao geraRestauranteEUsuario() {
        var avaliacao = gerarAvaliacao();
        restauranteRepository.save(avaliacao.getRestaurante());
        usuarioRepository.save(avaliacao.getUsuario());

        return avaliacao;
    }


}
