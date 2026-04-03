package br.rpgguilda.rpg_guilda.operacoes.repository;

import br.rpgguilda.rpg_guilda.operacoes.entity.MvPainelTaticoMissao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository responsável pelas consultas na materialized view
 * operacoes.mv_painel_tatico_missao.
 *
 * Este repositório é somente leitura.
 */
@Repository
public interface MvPainelTaticoMissaoRepository extends JpaRepository<MvPainelTaticoMissao, Long> {

    /**
     * Busca missões cuja ultima_atualizacao seja posterior à data informada,
     * ordenando por indice_prontidao de forma decrescente.
     *
     * @param data    Data limite (últimos N dias)
     * @param pageable Paginação (ex: PageRequest.of(0, 10) para top 10)
     * @return Lista de missões filtradas e ordenadas
     */
    List<MvPainelTaticoMissao> findByUltimaAtualizacaoAfterOrderByIndiceProntidaoDesc(
            LocalDateTime data, Pageable pageable);
}
