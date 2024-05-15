package br.com.restaurante.fiap.techclallenge.service;

import br.com.restaurante.fiap.techclallenge.entities.Avaliacao;
import br.com.restaurante.fiap.techclallenge.entities.AvaliacaoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AvaliacaoService {
    Avaliacao cadastrarAvaliacao(AvaliacaoDTO avaliacaoDTO);

    Avaliacao visualizarAvaliacao(Long id);

    Avaliacao alterar(AvaliacaoDTO avaliacao, Long id);

    boolean remover(Long id);

    Page<Avaliacao> listar(Pageable pageable);
}
