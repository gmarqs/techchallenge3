package br.com.restaurante.fiap.techclallenge.entities;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvaliacaoDTO {

    @NotNull
    @Positive
    private Long restaurante;
    @NotNull
    @Positive
    private Long usuario;
    @Range(min = 0, max = 5)
    private int avaliacao;
    private String comentarios;
}
