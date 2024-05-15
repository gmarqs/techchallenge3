package br.com.restaurante.fiap.techclallenge.repository;

import br.com.restaurante.fiap.techclallenge.entities.Restaurante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    @Query("SELECT r FROM Restaurante r WHERE r.nome like %:nome% OR r.localizacao like %:localizacao% OR r.tipoCozinha like %:tipoCozinha%")
    Page<Restaurante> listarRestaurantes(@Param("nome") String nome, @Param("localizacao") String localizacao, @Param("tipoCozinha") String tipoCozinha, Pageable pageable);

}
