package br.rpgguilda.rpg_guilda.operacoes.entity;

import br.rpgguilda.rpg_guilda.audit.entity.Organizacao;
import br.rpgguilda.rpg_guilda.operacoes.enums.NivelPerigo;
import br.rpgguilda.rpg_guilda.operacoes.enums.StatusMissao;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "missao", schema = "operacoes")
public class MissaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_perigo", nullable = false, length = 20)
    private NivelPerigo nivelPerigo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusMissao status;

    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    @OneToMany(mappedBy = "missao", fetch = FetchType.LAZY)
    private List<ParticipacaoMissaoEntity> participacoes = new ArrayList<>();

    public MissaoEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Organizacao getOrganizacao() { return organizacao; }
    public void setOrganizacao(Organizacao organizacao) { this.organizacao = organizacao; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public NivelPerigo getNivelPerigo() { return nivelPerigo; }
    public void setNivelPerigo(NivelPerigo nivelPerigo) { this.nivelPerigo = nivelPerigo; }
    public StatusMissao getStatus() { return status; }
    public void setStatus(StatusMissao status) { this.status = status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }
    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }
    public List<ParticipacaoMissaoEntity> getParticipacoes() { return participacoes; }
    public void setParticipacoes(List<ParticipacaoMissaoEntity> participacoes) { this.participacoes = participacoes; }
}
