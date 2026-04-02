package br.rpgguilda.rpg_guilda.audit.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "audit_entries", schema = "audit")
public class AuditEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false, foreignKey = @ForeignKey(name = "fk_audit_org"))
    private Organizacao organizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_user_id", foreignKey = @ForeignKey(name = "fk_audit_actor_user"))
    private Usuario actorUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_api_key_id", foreignKey = @ForeignKey(name = "fk_audit_actor_api_key"))
    private ApiKey actorApiKey;

    @Column(name = "action", nullable = false, length = 30)
    private String action;

    @Column(name = "entity_schema", nullable = false, length = 60)
    private String entitySchema;

    @Column(name = "entity_name", nullable = false, length = 80)
    private String entityName;

    @Column(name = "entity_id", length = 80)
    private String entityId;

    @Column(name = "occurred_at", nullable = false, updatable = false)
    private OffsetDateTime occurredAt;

    @Column(name = "ip", columnDefinition = "inet")
    private String ip;

    @Column(name = "user_agent", length = 255)
    private String userAgent;

    @Column(name = "diff", columnDefinition = "jsonb")
    private String diff;

    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;

    @Column(name = "success", nullable = false)
    private Boolean success = true;

    public AuditEntry() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Organizacao getOrganizacao() { return organizacao; }
    public void setOrganizacao(Organizacao organizacao) { this.organizacao = organizacao; }
    public Usuario getActorUser() { return actorUser; }
    public void setActorUser(Usuario actorUser) { this.actorUser = actorUser; }
    public ApiKey getActorApiKey() { return actorApiKey; }
    public void setActorApiKey(ApiKey actorApiKey) { this.actorApiKey = actorApiKey; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getEntitySchema() { return entitySchema; }
    public void setEntitySchema(String entitySchema) { this.entitySchema = entitySchema; }
    public String getEntityName() { return entityName; }
    public void setEntityName(String entityName) { this.entityName = entityName; }
    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    public OffsetDateTime getOccurredAt() { return occurredAt; }
    public void setOccurredAt(OffsetDateTime occurredAt) { this.occurredAt = occurredAt; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    public String getDiff() { return diff; }
    public void setDiff(String diff) { this.diff = diff; }
    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }
    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
}
