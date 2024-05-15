package br.com.restaurante.fiap.techclallenge.repository;

import br.com.restaurante.fiap.techclallenge.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
