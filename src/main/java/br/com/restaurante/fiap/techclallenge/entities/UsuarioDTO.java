package br.com.restaurante.fiap.techclallenge.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    @NotBlank
    private String nome;
    @NotBlank
    private String telefone;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @CPF
    private String cpf;
}
