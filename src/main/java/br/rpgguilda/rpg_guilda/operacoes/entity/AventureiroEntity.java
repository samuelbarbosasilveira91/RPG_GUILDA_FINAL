package br.rpgguilda.rpg_guilda.operacoes.entity;

import br.rpgguilda.rpg_guilda.audit.entity.Organizacao;
import br.rpgguilda.rpg_guilda.audit.entity.Usuario;
import br.rpgguilda.rpg_guilda.operacoes.enums.ClasseAventureiro;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aventureiro", schema = "operacoes")
public class AventureiroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_cadastro_id", nullable = false)
    private Usuario usuarioCadastro;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "classe", nullable = false, length = 30)
    private ClasseAventureiro classe;

    @Column(name = "nivel", nullable = false)
    private Integer nivel;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @OneToOne(mappedBy = "aventureiro", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CompanheiroEntity companheiro;

    @OneToMany(mappedBy = "aventureiro", fetch = FetchType.LAZY)
    private List<ParticipacaoMissaoEntity> participacoes = new ArrayList<>();

    public AventureiroEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Organizacao getOrganizacao() { return organizacao; }
    public void setOrganizacao(Organizacao organizacao) { this.organizacao = organizacao; }
    public Usuario getUsuarioCadastro() { return usuarioCadastro; }
    public void setUsuarioCadastro(Usuario usuarioCadastro) { this.usuarioCadastro = usuarioCadastro; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public ClasseAventureiro getClasse() { return classe; }
    public void setClasse(ClasseAventureiro classe) { this.classe = classe; }
    public Integer getNivel() { return nivel; }
    public void setNivel(Integer nivel) { this.nivel = nivel; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    public CompanheiroEntity getCompanheiro() { return companheiro; }
    public void setCompanheiro(CompanheiroEntity companheiro) { this.companheiro = companheiro; }
    public List<ParticipacaoMissaoEntity> getParticipacoes() { return participacoes; }
    public void setParticipacoes(List<ParticipacaoMissaoEntity> participacoes) { this.participacoes = participacoes; }
}
