package com.orktek.quebragalho.repository;

import com.orktek.quebragalho.model.Agendamento;
import com.orktek.quebragalho.model.Servico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByUsuarioId(Long usuarioId);

    List<Agendamento> findByServico_IdAndDataHoraBetween(Long servicoId, LocalDateTime inicio, LocalDateTime fim);

    List<Agendamento> findByServicoPrestadorId(Long prestadorId);

    boolean existsByDataHoraAndServico(LocalDateTime dataHora, Servico servico);

    List<Agendamento> findByStatusAceitoIsNull();

    @Query("SELECT a FROM Agendamento a " +
            "JOIN a.servico s " +
            "WHERE s.prestador.id = :idPrestador " +
            "AND a.dataHora BETWEEN :inicio AND :fim " +
            "AND a.statusAceito = true")
    List<Agendamento> listarPorPrestadorEntre(
            @Param("idPrestador") Long idPrestador,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

}