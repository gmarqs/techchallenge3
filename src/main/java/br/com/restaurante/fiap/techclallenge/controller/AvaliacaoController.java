package br.com.restaurante.fiap.techclallenge.controller;

import br.com.restaurante.fiap.techclallenge.entities.Avaliacao;
import br.com.restaurante.fiap.techclallenge.entities.AvaliacaoDTO;
import br.com.restaurante.fiap.techclallenge.service.AvaliacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("avaliacao")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @PostMapping
    private ResponseEntity<?> cadastrarAvaliacao(@RequestBody @Valid AvaliacaoDTO avaliacaoDTO){
        var avaliacao = avaliacaoService.cadastrarAvaliacao(avaliacaoDTO);

        return new ResponseEntity<>(avaliacao, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    private ResponseEntity<Avaliacao> visualizarAvaliacao(@PathVariable Long id){
        var avaliacao = avaliacaoService.visualizarAvaliacao(id);

        return new ResponseEntity<>(avaliacao, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Avaliacao> atualizarAvaliacao(@PathVariable Long id, @RequestBody @Valid AvaliacaoDTO avaliacao) {
        Avaliacao avaliacaoAtualizada = avaliacaoService.alterar(avaliacao, id);
        return new ResponseEntity<>(avaliacaoAtualizada, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirAvaliacao(@PathVariable Long id) {
        avaliacaoService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Avaliacao>> listarAvaliacoes(Pageable pageable) {
        Page<Avaliacao> avaliacoes = avaliacaoService.listar(pageable);
        return ResponseEntity.ok(avaliacoes);
    }


}
