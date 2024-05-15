package br.com.restaurante.fiap.techclallenge.service;

import br.com.restaurante.fiap.techclallenge.entities.Avaliacao;
import br.com.restaurante.fiap.techclallenge.entities.AvaliacaoDTO;
import br.com.restaurante.fiap.techclallenge.entities.Restaurante;
import br.com.restaurante.fiap.techclallenge.entities.Usuario;
import br.com.restaurante.fiap.techclallenge.repository.AvaliacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvaliacaoServiceImpl implements AvaliacaoService{

    private final AvaliacaoRepository avaliacaoRepository;

    private final ModelMapper modelMapper;

    private final UsuarioService usuarioService;

    private final RestauranteService restauranteService;

    @Override
    public Avaliacao cadastrarAvaliacao(AvaliacaoDTO avaliacaoDTO) {
        Usuario usuario = usuarioService.buscaUsuario(avaliacaoDTO.getUsuario());
        Restaurante restaurante = restauranteService.buscaRestaurante(avaliacaoDTO.getRestaurante());

        var avaliacao = modelMapper.map(avaliacaoDTO, Avaliacao.class);

        avaliacao.setUsuario(usuario);
        avaliacao.setRestaurante(restaurante);

        return avaliacaoRepository.save(avaliacao);
    }

    @Override
    public Avaliacao visualizarAvaliacao(Long id) {
        return avaliacaoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada"));
    }

    @Override
    public Avaliacao alterar(AvaliacaoDTO avaliacaoDTO, Long id) {
        var avaliacaoAntiga = visualizarAvaliacao(id);

        var avaliacao = modelMapper.map(avaliacaoDTO, Avaliacao.class);


        avaliacao.setId(avaliacaoAntiga.getId());
        avaliacao.setRestaurante(avaliacaoAntiga.getRestaurante());
        avaliacao.setUsuario(avaliacaoAntiga.getUsuario());


        return avaliacaoRepository.save(avaliacao);
    }

    @Override
    public boolean remover(Long id) {
        visualizarAvaliacao(id);

        avaliacaoRepository.deleteById(id);

        return true;
    }

    @Override
    public Page<Avaliacao> listar(Pageable pageable) {
        return avaliacaoRepository.findAll(pageable);
    }

}
