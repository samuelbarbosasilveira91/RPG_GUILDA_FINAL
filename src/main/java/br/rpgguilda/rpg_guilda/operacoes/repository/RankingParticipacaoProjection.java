package br.rpgguilda.rpg_guilda.operacoes.repository;

import java.math.BigDecimal;

public interface RankingParticipacaoProjection {
    Long getAventureiroId();
    String getNome();
    Long getTotalParticipacoes();
    BigDecimal getSomaRecompensas();
    Long getQuantidadeDestaques();
}
