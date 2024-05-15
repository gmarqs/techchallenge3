package br.com.restaurante.fiap.techclallenge.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime periodoDe;

    @Column(nullable = false)
    private LocalDateTime periodoAte;

    @ManyToOne
    @JoinColumn(name = "restaurante_id", referencedColumnName = "id", nullable = false)
    private Restaurante restaurante;

    @ManyToOne
    private Mesa mesa;

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

}

