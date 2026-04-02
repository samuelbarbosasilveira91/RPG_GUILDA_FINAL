package br.rpgguilda.rpg_guilda.operacoes.repository;

import br.rpgguilda.rpg_guilda.operacoes.entity.MissaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissaoJpaRepository extends JpaRepository<MissaoEntity, Long> {
    @org.springframework.data.jpa.repository.Query("SELECT m FROM MissaoEntity m WHERE " +
           "(:status IS NULL OR m.status = :status) AND " +
           "(:nivelPerigo IS NULL OR m.nivelPerigo = :nivelPerigo) AND " +
           "(CAST(:dataInicio AS java.time.LocalDateTime) IS NULL OR m.dataInicio >= :dataInicio) AND " +
           "(CAST(:dataFim AS java.time.LocalDateTime) IS NULL OR m.dataFim <= :dataFim)")
    org.springframework.data.domain.Page<MissaoEntity> findComFiltros(
            @org.springframework.data.repository.query.Param("status") br.rpgguilda.rpg_guilda.operacoes.enums.StatusMissao status,
            @org.springframework.data.repository.query.Param("nivelPerigo") br.rpgguilda.rpg_guilda.operacoes.enums.NivelPerigo nivelPerigo,
            @org.springframework.data.repository.query.Param("dataInicio") java.time.LocalDateTime dataInicio,
            @org.springframework.data.repository.query.Param("dataFim") java.time.LocalDateTime dataFim,
            org.springframework.data.domain.Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT m FROM MissaoEntity m LEFT JOIN FETCH m.participacoes p LEFT JOIN FETCH p.aventureiro WHERE m.id = :id")
    java.util.Optional<MissaoEntity> findByIdWithParticipacoes(@org.springframework.data.repository.query.Param("id") Long id);

    @org.springframework.data.jpa.repository.Query("SELECT m.id AS id, m.titulo AS titulo, m.status AS status, m.nivelPerigo AS nivelPerigo, COUNT(p) AS quantidadeParticipantes, SUM(p.recompensaOuro) AS totalRecompensas FROM MissaoEntity m LEFT JOIN m.participacoes p " +
           "WHERE (CAST(:dataInicio AS java.time.LocalDateTime) IS NULL OR m.dataInicio >= :dataInicio) AND " +
           "(CAST(:dataFim AS java.time.LocalDateTime) IS NULL OR m.dataInicio <= :dataFim) " +
           "GROUP BY m.id, m.titulo, m.status, m.nivelPerigo ORDER BY m.dataInicio DESC")
    java.util.List<MissaoMetricaProjection> getRelatorioMissoesMetricas(
            @org.springframework.data.repository.query.Param("dataInicio") java.time.LocalDateTime dataInicio,
            @org.springframework.data.repository.query.Param("dataFim") java.time.LocalDateTime dataFim);

}
