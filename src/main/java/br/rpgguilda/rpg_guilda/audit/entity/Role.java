package br.rpgguilda.rpg_guilda.audit.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles", schema = "audit",
       uniqueConstraints = @UniqueConstraint(name = "uq_roles_nome_por_org", columnNames = {"organizacao_id", "nome"}))
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false, foreignKey = @ForeignKey(name = "fk_roles_org"))
    private Organizacao organizacao;

    @Column(name = "nome", nullable = false, length = 60)
    private String nome;

    @Column(name = "descricao", length = 255)
    private String descricao;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<Usuario> usuarios = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_permissions", schema = "audit",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions = new HashSet<>();

    public Role() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Organizacao getOrganizacao() { return organizacao; }
    public void setOrganizacao(Organizacao organizacao) { this.organizacao = organizacao; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public Set<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(Set<Usuario> usuarios) { this.usuarios = usuarios; }
    public Set<Permission> getPermissions() { return permissions; }
    public void setPermissions(Set<Permission> permissions) { this.permissions = permissions; }
}
