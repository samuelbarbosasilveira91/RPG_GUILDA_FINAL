package br.rpgguilda.rpg_guilda.operacoes.repository;

import br.rpgguilda.rpg_guilda.operacoes.enums.NivelPerigo;
import br.rpgguilda.rpg_guilda.operacoes.enums.StatusMissao;
import java.math.BigDecimal;

public interface MissaoMetricaProjection {
    Long getId();
    String getTitulo();
    StatusMissao getStatus();
    NivelPerigo getNivelPerigo();
    Long getQuantidadeParticipantes();
    BigDecimal getTotalRecompensas();
}
