package br.rpgguilda.rpg_guilda.audit.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organizacoes", schema = "audit",
       uniqueConstraints = @UniqueConstraint(name = "organizacoes_nome_key", columnNames = "nome"))
public class Organizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 120, unique = true)
    private String nome;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY)
    private List<Usuario> usuarios = new ArrayList<>();

    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY)
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY)
    private List<ApiKey> apiKeys = new ArrayList<>();

    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY)
    private List<AuditEntry> auditEntries = new ArrayList<>();

    public Organizacao() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }
    public List<Role> getRoles() { return roles; }
    public void setRoles(List<Role> roles) { this.roles = roles; }
    public List<ApiKey> getApiKeys() { return apiKeys; }
    public void setApiKeys(List<ApiKey> apiKeys) { this.apiKeys = apiKeys; }
    public List<AuditEntry> getAuditEntries() { return auditEntries; }
    public void setAuditEntries(List<AuditEntry> auditEntries) { this.auditEntries = auditEntries; }
}
