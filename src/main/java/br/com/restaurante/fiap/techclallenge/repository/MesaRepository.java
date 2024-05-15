package br.com.restaurante.fiap.techclallenge.repository;

import br.com.restaurante.fiap.techclallenge.entities.Mesa;
import br.com.restaurante.fiap.techclallenge.entities.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {


    Mesa findByIdAndRestauranteId(Long idMesa, Long idRestaurante);
}
