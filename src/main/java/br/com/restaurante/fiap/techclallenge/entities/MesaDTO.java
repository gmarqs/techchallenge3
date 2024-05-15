package br.com.restaurante.fiap.techclallenge.entities;

import br.com.restaurante.fiap.techclallenge.entities.enums.MesaStatus;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MesaDTO {

    @Column(nullable = false)
    private Integer numero;

    @Column(nullable = false)
    private MesaStatus status;

    @ManyToOne
    @JoinColumn(name = "restaurante_id", referencedColumnName = "id", nullable = false)
    private Restaurante restaurante;


}
