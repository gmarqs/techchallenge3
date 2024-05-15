package br.com.restaurante.fiap.techclallenge.entities;

import br.com.restaurante.fiap.techclallenge.exceptions.CapacidadeMaximaException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String localizacao;
    @Column(nullable = false)
    private String tipoCozinha;
    @Column(nullable = false)
    private LocalTime horarioAbertura;
    @Column(nullable = false)
    private LocalTime horarioFechamento;
    @Column(nullable = false)
    private int qntdMesas;

    private int lotacaoAtual;
}
