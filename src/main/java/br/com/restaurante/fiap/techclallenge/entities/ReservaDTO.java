package br.com.restaurante.fiap.techclallenge.entities;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDTO {

    @FutureOrPresent
    private LocalDateTime periodoDe;
    @Future
    private LocalDateTime periodoAte;

    private Long restaurante;

    private Long mesa;

    private Long usuario;
}
