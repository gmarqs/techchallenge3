package br.com.restaurante.fiap.techclallenge.entities;

import br.com.restaurante.fiap.techclallenge.entities.enums.MesaStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long numero;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MesaStatus status;

    @ManyToOne
    @JoinColumn(name = "restaurante_id", referencedColumnName = "id", nullable = false)
    private Restaurante restaurante;

    @Column(nullable = false)
    private LocalDateTime periodoDe;

    @Column(nullable = false)
    private LocalDateTime periodoAte;
}
