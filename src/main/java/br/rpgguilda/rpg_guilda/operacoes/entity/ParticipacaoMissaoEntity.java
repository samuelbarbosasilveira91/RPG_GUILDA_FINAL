package br.rpgguilda.rpg_guilda.operacoes.entity;

import br.rpgguilda.rpg_guilda.operacoes.enums.PapelMissao;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "participacao_missao", schema = "operacoes")
public class ParticipacaoMissaoEntity {

    @EmbeddedId
    private ParticipacaoMissaoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("missaoId")
    @JoinColumn(name = "missao_id")
    private MissaoEntity missao;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("aventureiroId")
    @JoinColumn(name = "aventureiro_id")
    private AventureiroEntity aventureiro;

    @Enumerated(EnumType.STRING)
    @Column(name = "papel", nullable = false, length = 40)
    private PapelMissao papel;

    @Column(name = "recompensa_ouro", precision = 10, scale = 2)
    private BigDecimal recompensaOuro;

    @Column(name = "destaque")
    private Boolean destaque = false;

    @Column(name = "data_registro", updatable = false)
    private LocalDateTime dataRegistro;

    public ParticipacaoMissaoEntity() {}
    public ParticipacaoMissaoEntity(MissaoEntity missao, AventureiroEntity aventureiro) {
        this.missao = missao;
        this.aventureiro = aventureiro;
        this.id = new ParticipacaoMissaoId(missao.getId(), aventureiro.getId());
    }

    public ParticipacaoMissaoId getId() { return id; }
    public void setId(ParticipacaoMissaoId id) { this.id = id; }
    public MissaoEntity getMissao() { return missao; }
    public void setMissao(MissaoEntity missao) { this.missao = missao; }
    public AventureiroEntity getAventureiro() { return aventureiro; }
    public void setAventureiro(AventureiroEntity aventureiro) { this.aventureiro = aventureiro; }
    public PapelMissao getPapel() { return papel; }
    public void setPapel(PapelMissao papel) { this.papel = papel; }
    public BigDecimal getRecompensaOuro() { return recompensaOuro; }
    public void setRecompensaOuro(BigDecimal recompensaOuro) { this.recompensaOuro = recompensaOuro; }
    public Boolean getDestaque() { return destaque; }
    public void setDestaque(Boolean destaque) { this.destaque = destaque; }
    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }
}
