package br.rpgguilda.rpg_guilda.operacoes.entity;

import br.rpgguilda.rpg_guilda.operacoes.enums.EspecieCompanheiro;
import jakarta.persistence.*;

@Entity
@Table(name = "companheiro", schema = "operacoes")
public class CompanheiroEntity {

    @Id
    @Column(name = "aventureiro_id")
    private Long aventureiroId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "aventureiro_id")
    private AventureiroEntity aventureiro;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "especie", nullable = false, length = 40)
    private EspecieCompanheiro especie;

    @Column(name = "indice_lealdade", nullable = false)
    private Integer indiceLealdade;

    public CompanheiroEntity() {}

    public Long getAventureiroId() { return aventureiroId; }
    public void setAventureiroId(Long aventureiroId) { this.aventureiroId = aventureiroId; }
    public AventureiroEntity getAventureiro() { return aventureiro; }
    public void setAventureiro(AventureiroEntity aventureiro) { this.aventureiro = aventureiro; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public EspecieCompanheiro getEspecie() { return especie; }
    public void setEspecie(EspecieCompanheiro especie) { this.especie = especie; }
    public Integer getIndiceLealdade() { return indiceLealdade; }
    public void setIndiceLealdade(Integer indiceLealdade) { this.indiceLealdade = indiceLealdade; }
}
