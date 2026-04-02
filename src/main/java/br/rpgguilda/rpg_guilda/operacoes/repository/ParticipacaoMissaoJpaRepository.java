package br.rpgguilda.rpg_guilda.operacoes.repository;

import br.rpgguilda.rpg_guilda.operacoes.entity.ParticipacaoMissaoEntity;
import br.rpgguilda.rpg_guilda.operacoes.entity.ParticipacaoMissaoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipacaoMissaoJpaRepository extends JpaRepository<ParticipacaoMissaoEntity, ParticipacaoMissaoId> {

    @org.springframework.data.jpa.repository.Query("SELECT p.aventureiro.id AS aventureiroId, p.aventureiro.nome AS nome, COUNT(p) AS totalParticipacoes, SUM(p.recompensaOuro) AS somaRecompensas, SUM(CASE WHEN p.destaque = true THEN 1 ELSE 0 END) AS quantidadeDestaques FROM ParticipacaoMissaoEntity p JOIN p.missao m WHERE (CAST(:dataInicio AS java.time.LocalDateTime) IS NULL OR p.dataRegistro >= :dataInicio) AND (CAST(:dataFim AS java.time.LocalDateTime) IS NULL OR p.dataRegistro <= :dataFim) AND (:statusMissao IS NULL OR m.status = :statusMissao) GROUP BY p.aventureiro.id, p.aventureiro.nome ORDER BY totalParticipacoes DESC, somaRecompensas DESC")
    java.util.List<RankingParticipacaoProjection> getRankingParticipacao(@org.springframework.data.repository.query.Param("dataInicio") java.time.LocalDateTime dataInicio, @org.springframework.data.repository.query.Param("dataFim") java.time.LocalDateTime dataFim, @org.springframework.data.repository.query.Param("statusMissao") br.rpgguilda.rpg_guilda.operacoes.enums.StatusMissao statusMissao);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(p) FROM ParticipacaoMissaoEntity p WHERE p.aventureiro.id = :aventureiroId")
    Long countByAventureiroId(@org.springframework.data.repository.query.Param("aventureiroId") Long aventureiroId);

    @org.springframework.data.jpa.repository.Query("SELECT p.missao FROM ParticipacaoMissaoEntity p WHERE p.aventureiro.id = :aventureiroId ORDER BY p.dataRegistro DESC LIMIT 1")
    br.rpgguilda.rpg_guilda.operacoes.entity.MissaoEntity findUltimaMissaoByAventureiroId(@org.springframework.data.repository.query.Param("aventureiroId") Long aventureiroId);

}
