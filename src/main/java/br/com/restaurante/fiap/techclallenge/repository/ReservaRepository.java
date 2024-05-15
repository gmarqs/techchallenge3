package br.com.restaurante.fiap.techclallenge.repository;

import br.com.restaurante.fiap.techclallenge.entities.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("SELECT r FROM Reserva r JOIN r.mesa m WHERE r.restaurante.id = :idRestaurante AND m.numero = :numeroMesa AND (r.periodoDe < :periodoAte AND r.periodoAte > :periodoDe)")
    Reserva verificarSePeriodoEstaDisponivelParaReserva(Long idRestaurante, Long numeroMesa, LocalDateTime periodoDe, LocalDateTime periodoAte);

    @Query("SELECT r FROM Reserva r ORDER BY r.id ASC")
    Page<Reserva> listarReservas(Pageable pageable);
}
