package br.com.restaurante.fiap.techclallenge.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Avaliacao {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Restaurante restaurante;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuario usuario;
    @Column(nullable = false)
    private int avaliacao;
    private String comentarios;
}
