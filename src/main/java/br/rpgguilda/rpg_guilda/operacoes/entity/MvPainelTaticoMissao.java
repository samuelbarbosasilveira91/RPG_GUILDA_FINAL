package br.rpgguilda.rpg_guilda.operacoes.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade JPA mapeando a MATERIALIZED VIEW operacoes.mv_painel_tatico_missao.
 *
 * Esta view consolida informações estratégicas sobre missões,
 * como quantidade de participantes, nível médio da equipe, recompensas,
 * MVPs, companheiros e índice de prontidão tática.
 *
 * A anotação @Immutable indica ao Hibernate que esta entidade é somente leitura.
 */
@Entity
@Immutable
@Table(name = "mv_painel_tatico_missao", schema = "operacoes")
public class MvPainelTaticoMissao {

    @Id
    @Column(name = "missao_id")
    private Long missaoId;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "status")
    private String status;

    @Column(name = "nivel_perigo")
    private String nivelPerigo;

    @Column(name = "organizacao_id")
    private Long organizacaoId;

    @Column(name = "total_participantes")
    private Long totalParticipantes;

    @Column(name = "nivel_medio_equipe")
    private BigDecimal nivelMedioEquipe;

    @Column(name = "total_recompensa")
    private BigDecimal totalRecompensa;

    @Column(name = "total_mvps")
    private Long totalMvps;

    @Column(name = "participantes_com_companheiro")
    private Long participantesComCompanheiro;

    @Column(name = "ultima_atualizacao")
    private LocalDateTime ultimaAtualizacao;

    @Column(name = "indice_prontidao")
    private BigDecimal indiceProntidao;

    // Construtor padrão exigido pelo JPA
    public MvPainelTaticoMissao() {}

    // GETTERS

    public Long getMissaoId() {
        return missaoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getStatus() {
        return status;
    }

    public String getNivelPerigo() {
        return nivelPerigo;
    }

    public Long getOrganizacaoId() {
        return organizacaoId;
    }

    public Long getTotalParticipantes() {
        return totalParticipantes;
    }

    public BigDecimal getNivelMedioEquipe() {
        return nivelMedioEquipe;
    }

    public BigDecimal getTotalRecompensa() {
        return totalRecompensa;
    }

    public Long getTotalMvps() {
        return totalMvps;
    }

    public Long getParticipantesComCompanheiro() {
        return participantesComCompanheiro;
    }

    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public BigDecimal getIndiceProntidao() {
        return indiceProntidao;
    }
}
