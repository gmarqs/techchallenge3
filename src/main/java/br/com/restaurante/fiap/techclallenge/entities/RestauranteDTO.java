package br.com.restaurante.fiap.techclallenge.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestauranteDTO {

    @NotBlank(message = "O nome não pode estar vazio")
    private String nome;
    @NotBlank
    private String localizacao;
    @NotBlank
    private String tipoCozinha;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horarioAbertura;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horarioFechamento;
    @Min(value = 1)
    private int qntdMesas;

}
